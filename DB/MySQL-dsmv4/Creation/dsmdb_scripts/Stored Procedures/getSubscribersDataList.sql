USE `dsmv4`;
DROP procedure IF EXISTS `getSubscribersDataList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscribersDataList`()
BEGIN
		SELECT Subscribers.id AS subscriberId, Subscribers.dataId,
            Addresses.phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId
				FROM Subscribers
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
                    LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
                    LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				ORDER BY OfficeCodes.code, PhoneNumbers.number;
END$$

DELIMITER ;
