USE `dsmv4`;
DROP procedure IF EXISTS `dsmv4`.`getStatesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStatesList`()
BEGIN
	SELECT id, name
		FROM States
			ORDER BY name;
END$$

DELIMITER ;

 
