USE `dsmv4`;
DROP procedure IF EXISTS `getModemsModelsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getModemsModelsList`()
BEGIN
	SELECT ModemsModels.id AS modemModelId, ModemsModels.name AS modemModelName, ModemsModels.typeId AS modemTypeId, ModemsModels.manufacturerId AS modemManufacturerId,
		ModemsTypes.name AS modemTypeName,
        ModemsManufacturers.name AS modemManufacturerName
		FROM ModemsModels
			LEFT JOIN ModemsTypes ON ModemsTypes.id = ModemsModels.typeId
            LEFT JOIN ModemsManufacturers ON ModemsManufacturers.id = ModemsModels.manufacturerId
		ORDER BY ModemsManufacturers.name, ModemsModels.name, ModemsTypes.name;
END$$

DELIMITER ;
