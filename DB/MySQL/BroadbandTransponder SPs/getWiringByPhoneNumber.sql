USE `ac1`;
DROP procedure IF EXISTS `getWiringByPhoneNumber`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getWiringByPhoneNumber` (
IN officeCodeCode INT,
IN number CHAR(4))
BEGIN
SELECT Wiring.id AS wiringId, Wiring.distributorId, Wiring.streetPairId, Wiring.secondStreetPairId, Wiring.remarks
	FROM Wiring
        LEFT JOIN Subscribers ON Subscribers.distributorId = Wiring.distributorId
        LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
        LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
        LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
			WHERE OfficeCodes.code = officeCodeCode AND PhoneNumbers.number = number;
END$$

DELIMITER ;
 
