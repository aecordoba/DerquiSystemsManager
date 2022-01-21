USE `ac1`;
DROP procedure IF EXISTS `getServiceOrdersReportByPhone`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getServiceOrdersReportByPhone`(
IN officeCodeId INT,
IN number CHAR(4)
)
BEGIN
		SELECT ServiceOrders.id AS serviceOrderId, ServiceOrders.repairingTypeId, ServiceOrders.remarks AS serviceOrderRemarks, ServiceOrders.contact, ServiceOrders.creationTime, 
			CreatorPeople.firstName AS creatorFirstName, CreatorPeople.middleName AS creatorMiddleName, CreatorPeople.lastName AS creatorLastName,
			Repairings.repairmanRemarks, Repairings.assignmentTime, Repairings.repairedDate,
			RepairmanPeople.firstName AS repairmanFirstName, RepairmanPeople.middleName AS repairmanMiddleName, RepairmanPeople.lastName AS repairmanLastName,
            RepairsCompanies.name AS repairsCompany,
            RepairingChecks.remarks AS repairingCheckRemarks, RepairingChecks.approved, RepairingChecks.time AS repairingCheckTime,
			CheckPeople.firstName AS checkerFirstName, CheckPeople.middleName AS checkerMiddleName, CheckPeople.lastName AS checkerLastName
				FROM Subscribers
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
                    LEFT JOIN ServiceOrders ON ServiceOrders.subscriberId = Subscribers.id
					LEFT JOIN Users CreatorUsers ON CreatorUsers.id = ServiceOrders.userId
                    LEFT JOIN People CreatorPeople ON CreatorPeople.id = CreatorUsers.personId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
                    LEFT JOIN Repairmen ON Repairmen.id = Repairings.repairmanId
                    LEFT JOIN People RepairmanPeople ON RepairmanPeople.id = Repairmen.personId
                    LEFT JOIN RepairsCompanies ON RepairsCompanies.id = Repairmen.repairsCompanyId
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
					LEFT JOIN Users CheckUsers ON CheckUsers.id = RepairingChecks.userId
                    LEFT JOIN People CheckPeople ON CheckPeople.id = CheckUsers.personId
						WHERE  PhoneNumbers.number = number AND OfficeCodes.id = officeCodeId
							ORDER BY ServiceOrders.creationTime, Repairings.assignmentTime, RepairingChecks.time;
END$$

DELIMITER ;
