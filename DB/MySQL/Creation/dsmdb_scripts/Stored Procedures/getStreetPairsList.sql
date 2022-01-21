USE `ac1`;
DROP procedure IF EXISTS `getStreetPairsList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getStreetPairsList`()
BEGIN
	SELECT StreetPairs.id AS streetPairId, StreetPairs.pair, StreetPairs.available, StreetPairs.remarks, StreetPairs.cableId AS streetCableId,
		StreetCables.name AS streetCableName, StreetCables.pairs, StreetCables.description AS streetCableDescription, StreetCables.frameId AS streetFrameId,
		StreetFrames.name AS streetFrameName, StreetFrames.description AS streetFrameDescription, StreetFrames.siteId,
        COALESCE(Wiring.id, SecondWiring.id) AS wiringId
			FROM StreetPairs
				LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
				LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
                LEFT JOIN Wiring ON Wiring.streetPairId = StreetPairs.id
                LEFT JOIN Wiring  SecondWiring ON SecondWiring.secondStreetPairId = StreetPairs.id
					ORDER BY streetFrameName, streetCableName, pair;
END$$

DELIMITER ;
