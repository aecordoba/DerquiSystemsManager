 
USE `ac1`;
DROP procedure IF EXISTS `updateStreetPair`;

DELIMITER $$
USE `ac1`$$
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
