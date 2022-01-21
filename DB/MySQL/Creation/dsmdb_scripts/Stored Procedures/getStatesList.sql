USE `ac1`;
DROP procedure IF EXISTS `ac1`.`getStatesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStatesList`()
BEGIN
	SELECT id, name
		FROM States
			ORDER BY name;
END$$

DELIMITER ;

 
