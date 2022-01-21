USE `ac1`;
DROP procedure IF EXISTS `getSubscriberInformation`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberInformation`(
IN officeCodeId INT,
IN number CHAR(4))
BEGIN
	SELECT Subscribers.id AS subscriberId,
		PhoneNumbers.id AS phoneNumberId,
		OfficeCodes.id AS phoneOfficeCodeId, PhoneNumbers.number AS phoneNumber,
		Addresses.id addressId, Addresses.streetId, Addresses.number AS addressNumber,
		Addresses.floor, Addresses.apartment, Addresses.zipCode,
		Addresses.street1Id, Addresses.street2Id, 
		Addresses.locationId,
		Own.id AS ownerPersonId, Own.firstName AS ownerFirstName, Own.middleName AS ownerMiddleName, Own.lastName AS ownerLastName, Own.identificationTypeId AS ownerIdentificationTypeId, Own.identificationNumber AS ownerIdentificationNumber,
		Ass.id AS assigneePersonId, Ass.firstName AS assigneeFirstName, Ass.middleName AS assigneeMiddleName, Ass.lastName AS assigneeLastName, Ass.identificationTypeId AS assigneeIdentificationTypeId, Ass.identificationNumber AS assigneeIdentificationNumber,
        Wiring.broadbandId
			FROM Subscribers
				LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
				LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
				LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
				LEFT JOIN Countries ON Countries.id = Areas.countryId
                LEFT JOIN Owners ON  Owners.subscriberId = Subscribers.id
				LEFT JOIN People Own ON Own.id = Owners.personId
                LEFT JOIN Assignees ON Assignees.subscriberId = Subscribers.id
				LEFT JOIN People Ass ON Ass.id = Assignees.personId
                LEFT JOIN Wiring ON Wiring.distributorId = Subscribers.distributorId
					WHERE OfficeCodes.id = officeCodeId AND PhoneNumbers.number = number;
END$$

DELIMITER ;
