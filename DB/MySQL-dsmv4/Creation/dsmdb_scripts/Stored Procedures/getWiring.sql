USE `dsmv4`;
DROP procedure IF EXISTS `getWiring`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getWiring`(
IN subscriberId INT)
BEGIN
	SELECT Distributor.id AS distributorId, Distributor.neax61sigmaId, Distributor.sigmal3addrId, Distributor.neax61eId, Distributor.zhoneId,
		SwitchBlocks.id AS switchBlockId, SwitchBlocks.name AS switchBlockName, BlockPositions.id AS blockPositionId, BlockPositions.position AS blockPosition,
        Wiring.id AS wiringId,
        Broadband.id AS broadbandId,
		DSLAMs.id AS dslamId, DSLAMs.name AS dslamName, DSLAMsBoards.id AS boardId, DSLAMsBoards.slot AS boardSlot, BroadbandPorts.id AS broadbandPortId, BroadbandPorts.port AS broadbandPort, 
		StreetFrames.siteId AS siteId, StreetFrames.id AS streetFrameId, StreetFrames.name AS streetFrameName, StreetCables.id AS streetCableId, StreetCables.name AS streetCableName, StreetPairs.id AS streetPairId, StreetPairs.pair AS streetPair
		FROM Distributor
			LEFT JOIN Subscribers ON Subscribers.distributorId = Distributor.id
			LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
            LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
            LEFT JOIN Wiring ON Wiring.distributorId = Distributor.id
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = Broadband.portId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
            LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
				WHERE Subscribers.id = subscriberId;
END$$

DELIMITER ;
