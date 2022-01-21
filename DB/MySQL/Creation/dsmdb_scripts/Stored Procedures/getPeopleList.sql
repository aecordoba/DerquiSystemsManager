USE `ac1`;
DROP procedure IF EXISTS `getPeopleList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPeopleList`()
BEGIN
	SELECT People.id personId, People.firstName, People.middleName, People.lastName,
    IdentificationsTypes.id identificationTypeId, People.identificationNumber,
    PhoneNumbers1.id cellPhoneNumberId, OfficeCodes1.id cellPhoneOfficeCodeId, PhoneNumbers1.number cellPhoneNumber,
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
				ORDER BY People.lastName, People.firstName, People.middleName, People.identificationNumber;
END$$

DELIMITER ;
