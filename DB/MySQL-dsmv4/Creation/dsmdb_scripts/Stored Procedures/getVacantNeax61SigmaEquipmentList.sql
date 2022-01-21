USE `dsmv4`;
DROP procedure IF EXISTS `getVacantNeax61SigmaEquipmentList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getVacantNeax61SigmaEquipmentList`(
IN siteId INT)
BEGIN
	SELECT NEAX61Sigma.id AS neax61sigmaId, NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
		SigmaLineModules.id AS sigmaLineModuleId, SigmaLineModules.name AS sigmaLineModuleName,
        SigmaFrames.id AS sigmaFrameId, SigmaFrames.name AS sigmaFrameName, SigmaFrames.siteId AS sigmaFrameSiteId
			FROM NEAX61Sigma
				LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
				LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
				LEFT JOIN Distributor ON Distributor.neax61sigmaId = NEAX61Sigma.id
					WHERE SigmaFrames.siteId = siteId
                        AND Distributor.available = TRUE
						AND Distributor.id NOT IN (SELECT distributorId
														FROM Subscribers
															WHERE distributorId IS nOT NULL);

END$$

DELIMITER ;
