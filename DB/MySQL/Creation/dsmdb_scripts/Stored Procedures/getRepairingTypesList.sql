USE `ac1`;
DROP procedure IF EXISTS `getRepairingTypesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRepairingTypesList`()
BEGIN
	SELECT id, name, description
		FROM RepairingTypes
			ORDER BY id;
END$$

DELIMITER ;
