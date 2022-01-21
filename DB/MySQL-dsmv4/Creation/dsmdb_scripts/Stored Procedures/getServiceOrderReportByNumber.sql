USE `dsmv4`;
DROP procedure IF EXISTS `getServiceOrderReportByNumber`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getServiceOrderReportByNumber`(
IN serviceOrderNumber INT)
BEGIN
    
		SELECT Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number AS phoneNumber, 
			ServiceOrders.repairingTypeId, ServiceOrders.remarks AS serviceOrderRemarks, ServiceOrders.contact, ServiceOrders.creationTime, 
			CreatorPeople.firstName AS creatorFirstName, CreatorPeople.middleName AS creatorMiddleName, CreatorPeople.lastName AS creatorLastName,
			Repairings.repairmanRemarks, Repairings.assignmentTime, Repairings.repairedDate,
			RepairmanPeople.firstName AS repairmanFirstName, RepairmanPeople.middleName AS repairmanMiddleName, RepairmanPeople.lastName AS repairmanLastName,
            RepairsCompanies.name AS repairsCompany,
            RepairingChecks.remarks AS repairingCheckRemarks, RepairingChecks.approved, RepairingChecks.time AS repairingCheckTime,
			CheckPeople.firstName AS checkerFirstName, CheckPeople.middleName AS checkerMiddleName, CheckPeople.lastName AS checkerLastName
				FROM ServiceOrders
					LEFT JOIN Users CreatorUsers ON CreatorUsers.id = ServiceOrders.userId
                    LEFT JOIN People CreatorPeople ON CreatorPeople.id = CreatorUsers.personId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
                    LEFT JOIN Repairmen ON Repairmen.id = Repairings.repairmanId
                    LEFT JOIN People RepairmanPeople ON RepairmanPeople.id = Repairmen.personId
                    LEFT JOIN RepairsCompanies ON RepairsCompanies.id = Repairmen.repairsCompanyId
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
					LEFT JOIN Users CheckUsers ON CheckUsers.id = RepairingChecks.userId
                    LEFT JOIN People CheckPeople ON CheckPeople.id = CheckUsers.personId
                    LEFT JOIN Subscribers ON Subscribers.id = ServiceOrders.subscriberId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
					LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
					LEFT JOIN Countries ON Countries.id = Areas.countryId
						WHERE  ServiceOrders.id = serviceOrderNumber
							ORDER BY Repairings.assignmentTime, RepairingChecks.time;
                                                            
END$$

DELIMITER ;
