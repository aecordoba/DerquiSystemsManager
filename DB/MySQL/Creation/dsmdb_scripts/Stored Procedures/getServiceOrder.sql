USE `ac1`;
DROP procedure IF EXISTS `getServiceOrder`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getServiceOrder`(
IN serviceOrderId INT)
BEGIN
    
		SELECT ServiceOrders.id AS serviceOrderId, ServiceOrders.subscriberId, ServiceOrders.repairingTypeId, ServiceOrders.remarks AS serviceOrderRemarks, ServiceOrders.contact, ServiceOrders.creationTime, 
			Users.id AS userId, UserPeople.firstName AS userFirstName, UserPeople.middleName AS userMiddleName, UserPeople.lastName AS userLastName,
			Repairings.id AS repairingId, Repairings.repairmanId, Repairings.repairmanRemarks, Repairings.assignmentTime, Repairings.repairedDate,
			RepairingChecks.id AS repairingCheckId, RepairingChecks.userId, RepairingChecks.remarks AS repairingCheckRemarks, RepairingChecks.approved, RepairingChecks.time AS repairingCheckTime,
			PhoneNumbers.id AS phoneNumberId,
			OfficeCodes.id AS phoneOfficeCodeId, PhoneNumbers.number AS phoneNumber,
			Addresses.id addressId, Addresses.streetId, Addresses.number AS addressNumber,
			Addresses.floor, Addresses.apartment, Addresses.zipCode,
			Addresses.street1Id, Addresses.street2Id, 
			Addresses.locationId,
			Own.id AS ownerPersonId, Own.firstName AS ownerFirstName, Own.middleName AS ownerMiddleName, Own.lastName AS ownerLastName, Own.identificationTypeId AS ownerIdentificationTypeId, Own.identificationNumber AS ownerIdentificationNumber,
			Ass.id AS assigneePersonId, Ass.firstName AS assigneeFirstName, Ass.middleName AS assigneeMiddleName, Ass.lastName AS assigneeLastName, Ass.identificationTypeId AS assigneeIdentificationTypeId, Ass.identificationNumber AS assigneeIdentificationNumber,
			Wiring.broadbandId
				FROM ServiceOrders
					LEFT JOIN Users ON Users.id = ServiceOrders.userId
                    LEFT JOIN People UserPeople ON UserPeople.id = Users.personId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
                    LEFT JOIN Subscribers ON Subscribers.id = ServiceOrders.subscriberId
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
						WHERE  ServiceOrders.id = serviceOrderId
							ORDER BY Repairings.assignmentTime, RepairingChecks.time;
                                                            
END$$

DELIMITER ;
