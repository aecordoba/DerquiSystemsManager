USE `ac1`;
DROP procedure IF EXISTS `getBroadbandUninstallationsList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getBroadbandUninstallationsList`(
IN startDate DATE,
IN endDate DATE)
BEGIN

	CREATE TEMPORARY TABLE LastWiredOnPeriod ENGINE = MEMORY
		SELECT Subscribers.id, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number, WiringRecord.time
			FROM WiringRecord
				LEFT JOIN Subscribers ON Subscribers.distributorId = WiringRecord.distributorId
				LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
				LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
				LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
				LEFT JOIN Countries ON Countries.id = Areas.countryId
					WHERE WiringRecord.time = (SELECT MAX(WR.time)
													FROM WiringRecord WR
														LEFT JOIN  Subscribers S ON S.distributorId = WR.distributorId
															WHERE WR.time > startDate 
																AND WR.time < endDate 
																AND S.id = Subscribers.id)
						AND WiringRecord.broadbandRecordId IS NULL;

	CREATE TEMPORARY TABLE LastWiredBeforePeriod ENGINE = MEMORY
		SELECT Subscribers.id, WiringRecord.id AS wiringRecordId
			FROM WiringRecord
				LEFT JOIN Subscribers ON Subscribers.distributorId = WiringRecord.distributorId
					WHERE WiringRecord.time = (SELECT MAX(WR.time)
													FROM WiringRecord WR
														LEFT JOIN  Subscribers S ON S.distributorId = WR.distributorId
															WHERE WR.time < startDate 
																AND S.id = Subscribers.id)
						AND WiringRecord.broadbandRecordId IS NOT NULL;

	SELECT countryCode, areaCode, officeCode, number, Sites.name AS site, SwitchBlocks.name AS switchBlock, BlockPositions.position AS switchBlockPosition,
		BroadbandRecord.username AS broadbandUsername, BroadbandRecord.password AS broadbandPassword,
        Routers.name AS routerName, boardDSLAMRouter.name AS boardDslamRouterName,
		DSLAMs.name AS dslamName, boardDSLAM.name AS boardDSLAMName, DSLAMsBoards.slot AS dslamBoardSlot, BroadbandPorts.port AS broadbandPortsPort,
        StreetFrames.name AS streetFrameName, StreetCables.name AS streetCableName, StreetPairs.pair AS streetPairsPair,
		LastWiredOnPeriod.time
			FROM LastWiredOnPeriod
				INNER JOIN LastWiredBeforePeriod ON LastWiredBeforePeriod.id = LastWiredOnPeriod.id
				LEFT JOIN WiringRecord ON WiringRecord.id = LastWiredBeforePeriod.wiringRecordId
				LEFT JOIN Distributor ON Distributor.id = WiringRecord.distributorId
				LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
				LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
				LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
				LEFT JOIN BroadbandRecord ON BroadbandRecord.id = WiringRecord.broadbandRecordId
				LEFT JOIN BroadbandPorts ON BroadbandPorts.id = BroadbandRecord.portId
				LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
                LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs boardDSLAM ON boardDSLAM.id = DSLAMsBoards.dslamId
                LEFT JOIN Routers boardDSLAMRouter ON boardDSLAMRouter.id = boardDSLAM.routerId
				LEFT JOIN StreetPairs ON StreetPairs.id = WiringRecord.streetPairId
				LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
				LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId;

	DROP TEMPORARY TABLE IF EXISTS LastWiredOnPeriod;
	DROP TEMPORARY TABLE IF EXISTS LastWiredBeforePeriod;
    
END$$

DELIMITER ;
