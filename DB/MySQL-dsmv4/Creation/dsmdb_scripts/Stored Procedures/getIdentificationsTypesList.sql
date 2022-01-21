USE `dsmv4`;
DROP procedure IF EXISTS `getIdentificationsTypesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getIdentificationsTypesList`()
BEGIN
	SELECT id, name
		FROM IdentificationsTypes
			ORDER BY name;
END$$

DELIMITER ;
