 
USE `dsmv4`;
DROP procedure IF EXISTS `updateStreetPair`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateStreetPair`(
IN pairId INT,
IN available BOOLEAN,
IN remarks VARCHAR(100))
BEGIN
	UPDATE StreetPairs
		SET available = available,
			remarks = remarks
				WHERE id = pairId;
END$$

DELIMITER ;
