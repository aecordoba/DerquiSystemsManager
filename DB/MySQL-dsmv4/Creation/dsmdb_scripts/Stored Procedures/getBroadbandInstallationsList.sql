USE `dsmv4`;
DROP procedure IF EXISTS `getBroadbandInstallationsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getBroadbandInstallationsList`(
IN startDate DATE,
IN endDate DATE)
BEGIN

	CREATE TEMPORARY TABLE LastWiredOnPeriod ENGINE = MEMORY
		SELECT Subscribers.id, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number, WiringRecord.time, WiringRecord.id AS wiringRecordId
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
						AND WiringRecord.broadbandRecordId IS NOT NULL;

	CREATE TEMPORARY TABLE LastWiredBeforePeriod ENGINE = MEMORY
		SELECT Subscribers.id
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
        Routers.name AS routerName,
		DSLAMs.name AS dslamName, DSLAMsBoards.slot AS boardSlot, BroadbandPorts.port AS broadbandPort,
        StreetFrames.name AS streetFrameName, StreetCables.name AS streetCableName, StreetPairs.pair AS streetPairsPair,
		LastWiredOnPeriod.time
			FROM LastWiredOnPeriod
				LEFT JOIN WiringRecord ON WiringRecord.id = LastWiredOnPeriod.wiringRecordId
				LEFT JOIN Distributor ON Distributor.id = WiringRecord.distributorId
				LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
				LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
				LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
				LEFT JOIN BroadbandRecord ON BroadbandRecord.id = WiringRecord.broadbandRecordId
				LEFT JOIN BroadbandPorts ON BroadbandPorts.id = BroadbandRecord.portId
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
                LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
				LEFT JOIN StreetPairs ON StreetPairs.id = WiringRecord.streetPairId
				LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
				LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
					WHERE LastWiredOnPeriod.id NOT IN (SELECT id
														FROM LastWiredBeforePeriod);

	DROP TEMPORARY TABLE IF EXISTS LastWiredOnPeriod;
	DROP TEMPORARY TABLE IF EXISTS LastWiredBeforePeriod;
    
END$$

DELIMITER ;
