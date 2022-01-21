USE `ac1`;
DROP procedure IF EXISTS `getVacantNeax61EEquipmentList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getVacantNeax61EEquipmentList`(
 IN siteId INT)
BEGIN
	SELECT NEAX61E.id AS neax61eId, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.group, NEAX61E.switch, NEAX61E.level,
		ELineModules.id AS eLineModuleId, ELineModules.name AS eLineModuleName,
		EFrames.id AS eFrameId, EFrames.name AS eFrameName, EFrames.siteId AS eFrameSiteId
			FROM NEAX61E
				LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
				LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
				LEFT JOIN Distributor ON Distributor.neax61eId = NEAX61E.id
					WHERE EFrames.siteId = siteId
                        AND Distributor.available = TRUE
						AND Distributor.id NOT IN (SELECT distributorId
														FROM Subscribers);
END$$

DELIMITER ;
