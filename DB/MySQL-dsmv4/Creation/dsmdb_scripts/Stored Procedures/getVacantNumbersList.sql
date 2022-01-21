USE `dsmv4`;
DROP procedure IF EXISTS `getVacantNumbersList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getVacantNumbersList`(
officeCodeId INT)
BEGIN

    SELECT OwnPhoneNumbers.number
		FROM OwnPhoneNumbers
			WHERE  OwnPhoneNumbers.officeCodeId = officeCodeId
				AND number NOT IN (SELECT PhoneNumbers.number
										FROM PhoneNumbers
											LEFT JOIN Addresses ON Addresses.phoneNumberId = PhoneNumbers.id
                                            LEFT JOIN Subscribers ON Subscribers.addressId = Addresses.id
												WHERE PhoneNumbers.officeCodeId = officeCodeId
													AND Subscribers.id IS NOT NULL
                                                    AND Subscribers.distributorId IS NOT NULL);
        
END$$

DELIMITER ;
