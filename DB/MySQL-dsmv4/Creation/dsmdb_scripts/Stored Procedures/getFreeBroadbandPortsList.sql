USE `dsmv4`;
DROP procedure IF EXISTS `getFreeBroadbandPortsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getFreeBroadbandPortsList`()
BEGIN
	SELECT BroadbandPorts.id AS broadbandPortId, BroadbandPorts.port AS broadbandPort, BroadbandPorts.boardId AS boardId, BroadbandPorts.available AS broadbandPortAvailable,
        DSLAMsBoards.slot AS boardSlot, DSLAMsBoards.dslamId AS dslamId,
        DSLAMs.siteId AS dslamSiteId, DSLAMs.name AS dslamName
			FROM BroadbandPorts
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
					WHERE BroadbandPorts.available = TRUE
						AND BroadbandPorts.id NOT IN (SELECT portId
															FROM Broadband
																WHERE portId IS NOT NULL)
			ORDER BY DSLAMs.name, DSLAMsBoards.slot, BroadbandPorts.port;
END$$

DELIMITER ;
