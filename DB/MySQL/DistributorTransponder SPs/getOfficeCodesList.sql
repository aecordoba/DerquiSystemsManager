USE `ac1`;
DROP procedure IF EXISTS `getOfficeCodesList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getOfficeCodesList` (
IN countryCode INT,
IN areaCode INT)
BEGIN
	SELECT OfficeCodes.id AS id, OfficeCodes.code AS code 
		FROM ac1.OfficeCodes
			LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
			LEFT JOIN Countries ON Countries.id = Areas.countryId
				WHERE Areas.code = areaCode
					AND Countries.code = countryCode;
END$$

DELIMITER ;
 
