USE `dsmv4`;
DROP procedure IF EXISTS `getBroadbandPortsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getBroadbandPortsList`()
BEGIN
	SELECT BroadbandPorts.id AS broadbandPortId, BroadbandPorts.port AS broadbandPort, BroadbandPorts.boardId AS boardId, BroadbandPorts.available AS BroadbandPortAvailable,
        DSLAMsBoards.slot AS boardSlot, DSLAMsBoards.dslamId AS dslamId,
        DSLAMs.siteId AS dslamSiteId, DSLAMs.name AS dslamName
			FROM BroadbandPorts
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
			ORDER BY DSLAMs.name, DSLAMsBoards.slot, BroadbandPorts.port;
END$$

DELIMITER ;
