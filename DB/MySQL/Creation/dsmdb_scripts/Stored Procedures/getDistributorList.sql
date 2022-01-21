USE `ac1`;
DROP procedure IF EXISTS `getDistributorList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDistributorList`(
IN switchBlockId INT)
BEGIN
	SELECT Distributor.id AS distributorId, Distributor.available,
    BlockPositions.switchBlockId,
    NEAX61Sigma.id AS neax61sigmaId, NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, 
    SigmaLineModules.id AS sigmaLineModuleId, SigmaLineModules.name AS sigmaLineModuleName,
    SigmaFrames.id AS sigmaFrameId, SigmaFrames.name AS sigmaFrameName, SigmaFrames.siteId AS sigmaFrameSiteId,
    NEAX61SigmaELUs.id AS sigmaeluId, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, GROUP_CONCAT(DISTINCT SigmaDTIs.dti ORDER BY dti) AS dtisList, NEAX61SigmaELUs.siteId AS sigmaeluSiteId,
    NEAX61E.id AS neax61eId, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, 
    ELineModules.id AS eLineModuleId, ELineModules.name AS eLineModuleName,
    EFrames.id AS eFrameId, EFrames.name AS eFrameName, EFrames.siteId AS eFrameSiteId,
    Zhone.id AS zhoneId, Zhone.cable AS zhoneCable, Zhone.siteId AS zhoneSiteId
		FROM Distributor
		LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
        LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
        LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
        LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
        LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
        LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
        LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
        LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
		LEFT JOIN SigmaDTIs ON SigmaDTIs.eluId = NEAX61SigmaELUs.id
        LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
        LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
        LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
        LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
			WHERE BlockPositions.switchBlockId = switchBlockId
				GROUP BY NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61SigmaELUs.timeSwitch, NEAX61SigmaELUs.kHighway, NEAX61SigmaELUs.pHighway, NEAX61SigmaELUs.name, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, Zhone.cable
					ORDER BY NEAX61Sigma.id, NEAX61SigmaELUs.id, NEAX61E.id, Zhone.id;
END$$

DELIMITER ;
