USE `ac1`;
DROP procedure IF EXISTS `updatePerson`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updatePerson`(
IN personId INT,
IN firstName VARCHAR(20),
IN middleName VARCHAR(20),
IN lastName VARCHAR(45),
IN identificationTypeId INT,
IN identificationNumber INT,
IN cellPhoneAreaId INT,
IN cellPhoneOfficeCode INT,
IN cellPhoneNumber CHAR(4),
IN addressId INT,
IN streetId INT,
IN addressNumber VARCHAR(15),
IN floor VARCHAR(10),
IN apartment VARCHAR(10),
IN street1Id INT,
IN street2Id INT,
IN zipCode VARCHAR(8),
IN locationId INT,
IN phoneAreaId INT,
IN phoneOfficeCode INT,
IN phoneNumber CHAR(4))
BEGIN

	DECLARE phoneOfficeCodeId INT DEFAULT NULL;
	DECLARE phoneNumberId INT DEFAULT NULL;
	DECLARE cellPhoneOfficeCodeId INT DEFAULT NULL;
    DECLARE cellPhoneNumberId INT DEFAULT NULL;
    DECLARE tempAddressId INT DEFAULT NULL;

	IF (phoneNumber IS NOT NULL) THEN
		SELECT id INTO phoneOfficeCodeId FROM OfficeCodes WHERE code = phoneOfficeCode AND areaId = phoneAreaId;
        
        IF phoneOfficeCodeId IS NOT NULL THEN
			SELECT id INTO phoneNumberId FROM PhoneNumbers WHERE PhoneNumbers.officeCodeId = phoneOfficeCodeId AND number = phoneNumber;
        ELSE
			INSERT INTO OfficeCodes (code, areaId)
				VALUES(phoneOfficeCode, phoneAreaId);
			SELECT LAST_INSERT_ID() INTO phoneOfficeCodeId;
        END IF;
        
        IF phoneNumberId IS NULL THEN
			INSERT INTO PhoneNumbers(officeCodeId, number)
				VALUE(phoneOfficeCodeId, phoneNumber);
			SELECT LAST_INSERT_ID() INTO phoneNumberId;
		END IF;
	END IF;

	IF (streetId = 0) THEN
		SET locationId = NULL;
        SET streetId = NULL;
	END IF;
	IF (street1Id = 0) THEN
       SET street1Id = NULL;
	END IF;
	IF (street2Id = 0) THEN
       SET street2Id = NULL;
	END IF;
    IF (addressId = 0) THEN
		IF (streetId IS NOT NULL) OR (phoneNumberId IS NOT NULL) THEN
			INSERT INTO Addresses(streetId, number, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId)
				VALUES(streetId, addressNumber, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId);
            SELECT LAST_INSERT_ID() INTO tempAddressId;
        END IF;
	ELSE
        IF (streetId IS NOT NULL) OR (phoneNumberId IS NOT NULL) THEN	
			UPDATE Addresses
				SET streetId = streetId,
					number = addressNumber,
					floor = floor,
					apartment = apartment,
                    street1Id = street1Id,
                    street2Id = street2Id,
                    zipCode = zipCode,
					locationId = locationId,
					phoneNumberId = phoneNumberId
						WHERE id = addressId;
			SET tempAddressId = addressId;
		ELSE
			UPDATE People
				SET addressId = NULL
					WHERE id = personId;
			DELETE FROM Addresses
				WHERE id = addressId;
        END IF;
    END IF;
    
    SET addressId = tempAddressId;

	IF (cellPhoneNumber IS NOT NULL) THEN
		SELECT id INTO cellPhoneOfficeCodeId FROM OfficeCodes WHERE code = cellPhoneOfficeCode AND areaId = cellPhoneAreaId;
        
        IF cellPhoneOfficeCodeId IS NOT NULL THEN
			SELECT id INTO cellPhoneNumberId FROM PhoneNumbers WHERE PhoneNumbers.officeCodeId = cellPhoneOfficeCodeId AND number = cellPhoneNumber;
        ELSE
			INSERT INTO OfficeCodes (code, areaId)
				VALUES(cellPhoneOfficeCode, cellPhoneAreaId);
			SELECT LAST_INSERT_ID() INTO cellPhoneOfficeCodeId;
        END IF;
        
        IF cellPhoneNumberId IS NULL THEN
			INSERT INTO PhoneNumbers(officeCodeId, number)
				VALUE(cellPhoneOfficeCodeId, cellPhoneNumber);
			SELECT LAST_INSERT_ID() INTO cellPhoneNumberId;
		END IF;
	END IF;

	IF (identificationNumber = 0) THEN		
		SET identificationTypeId = NULL;
        SET identificationNumber = NULL;
    END IF;


	UPDATE People
		SET firstName = firstName,
			middleName = middleName,
            lastName = lastName,
            identificationTypeId = identificationTypeId,
            identificationNumber = identificationNumber,
            addressId = addressId,
            phoneNumberId = cellPhoneNumberId
			WHERE id = personId;
END$$

DELIMITER ;
