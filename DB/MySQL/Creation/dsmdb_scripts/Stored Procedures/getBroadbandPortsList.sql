USE `ac1`;
DROP procedure IF EXISTS `getBroadbandPortsList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getBroadbandPortsList` ()
BEGIN
	SELECT BroadbandPorts.id AS broadbandPortId, BroadbandPorts.port AS broadbandPort, BroadbandPorts.dslamId AS dslamId, BroadbandPorts.boardId AS boardId, BroadbandPorts.available AS BroadbandPortAvailable,
		DSLAMs.siteId AS dslamSiteId, DSLAMs.name AS dslamName,
        DSLAMsBoards.slot AS dslamBoardSlot, DSLAMsBoards.dslamId AS dslamBoardDslamId,
        DSLAMBoardDslam.siteId AS dslamBoardDslamSiteId, DSLAMBoardDslam.name AS dslamBoardDslamName
			FROM BroadbandPorts
				LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs DSLAMBoardDslam ON DSLAMBoardDslam.id = DSLAMsBoards.dslamId
			ORDER BY DSLAMBoardDslam.name, DSLAMs.name, DSLAMsBoards.slot, BroadbandPorts.port;
END$$

DELIMITER ;
 
