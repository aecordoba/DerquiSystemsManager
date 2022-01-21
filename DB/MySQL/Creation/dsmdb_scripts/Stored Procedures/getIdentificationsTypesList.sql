USE `ac1`;
DROP procedure IF EXISTS `getIdentificationsTypesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getIdentificationsTypesList`()
BEGIN
	SELECT id, name
		FROM IdentificationsTypes
			ORDER BY name;
END$$

DELIMITER ;
