USE `ac1`;
DROP procedure IF EXISTS `getSubscriberId`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberId`(
IN officeCodeId INT,
IN mcdu CHAR(4))
BEGIN
	SELECT Subscribers.id, Subscribers.distributorId
		FROM Subscribers
			LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
				WHERE PhoneNumbers.officeCodeId = officeCodeId AND PhoneNumbers.number = mcdu;
END$$

DELIMITER ;
