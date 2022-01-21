USE `ac1`;
DROP procedure IF EXISTS `getPairId`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getPairId` (
IN frame CHAR,
IN cable CHAR,
IN pair INT)
BEGIN
	SELECT StreetPairs.id AS pairId
		FROM StreetPairs
			LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
				WHERE StreetFrames.name = frame
					AND StreetCables.name = cable
                    AND StreetPairs.pair = pair;
END$$

DELIMITER ;
 
