USE `dsmv4`;
DROP procedure IF EXISTS `updateStreetCable`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateStreetCable`(
IN cableId INT,
IN description VARCHAR(100))
BEGIN
	UPDATE StreetCables
		SET description = description
			WHERE id = cableId;
END$$

DELIMITER ;
 
