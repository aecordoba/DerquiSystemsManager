USE `dsmv4`;
DROP procedure IF EXISTS `getPeopleListByCriteria`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPeopleListByCriteria`(
IN firstName VARCHAR(22),
IN middleName VARCHAR(22),
IN lastName VARCHAR(47),
IN idTypeId INT,
IN idNumber INT)
BEGIN

	CREATE TEMPORARY TABLE TempPeople (
    id INT PRIMARY KEY NOT NULL,
    identificationTypeId INT,
    identificationNumber INT) ENGINE = MEMORY;
    
	IF (middleName IS NULL) THEN
		INSERT INTO TempPeople
			SELECT id, identificationTypeId, identificationNumber
				FROM People
					WHERE People.firstName LIKE firstName AND People.lastName LIKE lastName;    
	ELSE
		INSERT INTO TempPeople
			SELECT id, identificationTypeId, identificationNumber
				FROM People
					WHERE People.firstName LIKE firstName AND People.middleName LIKE middleName AND People.lastName LIKE lastName;    
	END IF;

	IF (idTypeId > 0) THEN
		DELETE FROM TempPeople
			WHERE TempPeople.identificationTypeId != idTypeId
				OR TempPeople.identificationTypeId IS NULL;
    END IF;
    
    IF (idNumber > 0) THEN
		DELETE FROM TempPeople
			WHERE TempPeople.identificationNumber != idNumber
				OR TempPeople.identificationNumber IS NULL;
    END IF;
    
	SELECT People.id personId, People.firstName, People.middleName, People.lastName,
    IdentificationsTypes.id identificationTypeId, People.identificationNumber,
    PhoneNumbers1.id cellPhoneNumberId,OfficeCodes1.id cellPhoneOfficeCodeId, PhoneNumbers1.number cellPhoneNumber,
    Addresses.id addressId, Addresses.streetId, Addresses.number AS addressNumber,
    Addresses.floor, Addresses.apartment, Addresses.zipCode,
    Addresses.street1Id, Addresses.street2Id, 
    Addresses.locationId,
    PhoneNumbers2.id phoneNumberId, OfficeCodes2.id phoneOfficeCodeId, PhoneNumbers2.number phoneNumber
		FROM People
			LEFT JOIN IdentificationsTypes ON IdentificationsTypes.id = People.identificationTypeId
            LEFT JOIN PhoneNumbers PhoneNumbers1 ON PhoneNumbers1.id = People.phoneNumberId
            LEFT JOIN OfficeCodes OfficeCodes1 ON OfficeCodes1.id = PhoneNumbers1.officeCodeId
			LEFT JOIN Addresses ON Addresses.id = People.addressId
            LEFT JOIN PhoneNumbers PhoneNumbers2 ON PhoneNumbers2.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes OfficeCodes2 ON OfficeCodes2.id = PhoneNumbers2.officeCodeId
				WHERE People.id IN (SELECT id FROM TempPeople)
						ORDER BY People.lastName, People.firstName, People.middleName, People.identificationNumber;
                        
	DROP TEMPORARY TABLE TempPeople;
END$$

DELIMITER ;
