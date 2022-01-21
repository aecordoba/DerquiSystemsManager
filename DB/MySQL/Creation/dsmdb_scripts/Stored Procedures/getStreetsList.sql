USE `ac1`;
DROP procedure IF EXISTS `getStreetsList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStreetsList`()
BEGIN
	SELECT id, name, code, spare
		FROM Streets
			ORDER BY name;
END$$

DELIMITER ;
