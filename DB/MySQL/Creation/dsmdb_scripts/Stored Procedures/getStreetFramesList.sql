USE `ac1`;
DROP procedure IF EXISTS `getStreetFramesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStreetFramesList`()
BEGIN
	SELECT id AS streetFrameId, name AS streetFrameName, description streetFrameDescription, siteId 
		FROM StreetFrames
			ORDER BY name;
END$$

DELIMITER ;
