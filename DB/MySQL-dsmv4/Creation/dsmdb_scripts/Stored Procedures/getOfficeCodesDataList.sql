USE `dsmv4`;
DROP procedure IF EXISTS `getOfficeCodesDataList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getOfficeCodesDataList`()
BEGIN
	SELECT OfficeCodes.id AS officeCodeId, OfficeCodes.code AS officeCode,
		Areas.id AS areaId, Areas.code AS areaCode, Areas.name AS areaName,
        Countries.id AS countryId, Countries.code AS countryCode, Countries.name AS countryName,
        OwnNumeration.number
			FROM OfficeCodes
				RIGHT JOIN Areas ON Areas.id = OfficeCodes.areaId
                RIGHT JOIN Countries ON Countries.id = Areas.countryId
                LEFT JOIN OwnNumeration ON OwnNumeration.officeCodeId = OfficeCodes.id
			ORDER BY countryCode, AreaCode, officeCode, number;
END$$

DELIMITER ;
