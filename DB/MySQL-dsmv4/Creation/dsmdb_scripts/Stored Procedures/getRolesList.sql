USE `dsmv4`;
DROP procedure IF EXISTS `getRolesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRolesList`()
BEGIN
	SELECT id, name, description
		FROM Roles
			ORDER BY name;
END$$

DELIMITER ;
