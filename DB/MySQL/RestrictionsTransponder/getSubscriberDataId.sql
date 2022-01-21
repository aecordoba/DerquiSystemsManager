USE `ac1`;
DROP procedure IF EXISTS `getSubscriberDataId`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberDataId`(
IN officeCodeId INT,
IN mcdu CHAR(4))
BEGIN
	SELECT SubscribersData.id AS dataId, Wiring.broadbandId, SubscribersData.broadbandStateId
		FROM SubscribersData
			LEFT JOIN Subscribers ON Subscribers.dataId = SubscribersData.id
			LEFT JOIN Wiring ON Wiring.distributorId = Subscribers.distributorId
			LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
				WHERE PhoneNumbers.officeCodeId = officeCodeId AND PhoneNumbers.number = mcdu;
END$$

DELIMITER ;

