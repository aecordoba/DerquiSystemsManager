USE `dsmv4`;
DROP procedure IF EXISTS `getNoWiredSubscribersList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getNoWiredSubscribersList`(
IN officeCodeId INT,
IN siteId INT)
BEGIN
    SELECT OwnPhoneNumbers.number
		FROM OwnPhoneNumbers
			WHERE  OwnPhoneNumbers.officeCodeId = officeCodeId
				AND number IN (SELECT PhoneNumbers.number
										FROM PhoneNumbers
											LEFT JOIN Addresses ON Addresses.phoneNumberId = PhoneNumbers.id
                                            LEFT JOIN Subscribers ON Subscribers.addressId = Addresses.id
                                            LEFT JOIN Distributor ON Distributor.id = Subscribers.distributorId
                                            LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
                                            LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
												WHERE PhoneNumbers.officeCodeId = officeCodeId
													AND SwitchBlocks.siteId = siteId
                                                    AND Distributor.available = TRUE
													AND Subscribers.id IS NOT NULL
                                                    AND Subscribers.distributorId NOT IN (SELECT distributorId
																								FROM Wiring));
END$$

DELIMITER ;
