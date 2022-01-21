USE `dsmv4`;
DROP procedure IF EXISTS `getNotInstalledBroadbandList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getNotInstalledBroadbandList`(
IN targetDate DATE)
BEGIN
	SELECT Subscribers.id, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number, WiringRecord.time
		FROM WiringRecord
			LEFT JOIN Subscribers ON Subscribers.distributorId = WiringRecord.distributorId
			LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
			LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
			LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
			LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
			LEFT JOIN Countries ON Countries.id = Areas.countryId
				WHERE WiringRecord.time = (SELECT MAX(WR.time)
												FROM WiringRecord WR
													LEFT JOIN  Subscribers S ON S.distributorId = WR.distributorId
														WHERE WR.time < targetDate 
															AND S.id = Subscribers.id)
						AND WiringRecord.broadbandRecordId IS NULL;
END$$

DELIMITER ; 
