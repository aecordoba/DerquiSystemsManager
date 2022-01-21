USE `dsmv4`;
DROP procedure IF EXISTS `getServiceOrdersList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getServiceOrdersList`()
BEGIN
	CREATE TEMPORARY TABLE PendingServiceOrders ENGINE = MEMORY
		SELECT ServiceOrders.id AS serviceOrderId, ServiceOrders.subscriberId,
			PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId
				FROM ServiceOrders
					LEFT JOIN Subscribers ON Subscribers.id = ServiceOrders.subscriberId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
						WHERE Repairings.repairmanId IS NULL;
	
    INSERT INTO PendingServiceOrders
		SELECT ServiceOrders.id AS serviceOrderId, ServiceOrders.subscriberId,
			PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId
				FROM ServiceOrders
					LEFT JOIN Subscribers ON Subscribers.id = ServiceOrders.subscriberId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
						WHERE Repairings.id IN (SELECT MAX(id)
													FROM Repairings
														GROUP BY serviceOrderId)
							AND RepairingChecks.id IS NULL;

    INSERT INTO PendingServiceOrders
		SELECT ServiceOrders.id AS serviceOrderId, ServiceOrders.subscriberId,
			PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId
				FROM ServiceOrders
					LEFT JOIN Subscribers ON Subscribers.id = ServiceOrders.subscriberId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
					LEFT JOIN RepairingChecks ON RepairingChecks.repairingId = Repairings.id
						WHERE Repairings.id IN (SELECT MAX(id)
													FROM Repairings
														GROUP BY serviceOrderId)
							AND RepairingChecks.id IN (SELECT MAX(id)
															FROM RepairingChecks
																GROUP BY repairingId)
							AND (RepairingChecks.approved IS NULL OR RepairingChecks.approved = FALSE);
                            
	SELECT *
		FROM PendingServiceOrders
			ORDER BY serviceOrderId;
	
    DROP TEMPORARY TABLE IF EXISTS PendingServiceOrders;
END$$

DELIMITER ;
