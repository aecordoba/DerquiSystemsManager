USE `ac1`;
DROP procedure IF EXISTS `getRolesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRolesList`()
BEGIN
	SELECT id, name, description
		FROM Roles
			ORDER BY name;
END$$

DELIMITER ;
