USE `dsmv4`;
DROP procedure IF EXISTS `getStreetCablesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStreetCablesList`()
BEGIN
	SELECT StreetCables.id AS streetCableId, StreetCables.name AS streetCableName, StreetCables.pairs, StreetCables.description AS streetCableDescription, StreetCables.frameId AS streetFrameId
		FROM StreetCables
			ORDER BY streetCableName;
END$$

DELIMITER ;
