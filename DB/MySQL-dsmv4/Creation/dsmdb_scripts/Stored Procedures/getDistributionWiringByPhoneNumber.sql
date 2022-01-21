USE `dsmv4`;
DROP procedure IF EXISTS `getDistributionWiringByPhoneNumber`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDistributionWiringByPhoneNumber`(
IN officeCodeId INT,
IN mcdu CHAR(4))
BEGIN
	SELECT Wiring.id AS wiringId, Wiring.distributorId, Wiring.streetPairId, Wiring.secondStreetPairId, Wiring.broadbandId,
    BlockPositions.id AS blockPositionId, BlockPositions.position AS blockPositionsPosition, BlockPositions.switchBlockId,
    NEAX61Sigma.id AS neax61sigmaId, NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
    SigmaLineModules.id AS sigmaLineModuleId, SigmaLineModules.name AS sigmaLineModuleName,
    SigmaFrames.id AS sigmaFrameId, SigmaFrames.name AS sigmaFrameName, SigmaFrames.siteId AS sigmaFrameSiteId,
    SigmaL3Addr.id AS sigmal3addrId, SigmaL3Addr.l3addr AS sigmal3addr, NEAX61SigmaELUs.id AS sigmaeluId, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, NEAX61SigmaELUs.siteId AS sigmaeluSiteId,
    NEAX61E.id AS neax61eId, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, NEAX61E.switch, NEAX61E.level,
    ELineModules.id AS eLineModuleId, ELineModules.name AS eLineModuleName,
    EFrames.id AS eFrameId, EFrames.name AS eFrameName, EFrames.siteId AS eFrameSiteId,
    Zhone.id AS zhoneId, Zhone.cable AS zhoneCable, Zhone.port AS zhonePort, Zhone.siteId AS zhoneSiteId,
    StreetPairs.pair AS streetPairsPair, StreetPairs.cableId AS streetCableId, StreetCables.name AS streetCableName,
    StreetCables.frameId AS streetFrameId, StreetFrames.name AS streetFrameName, StreetFrames.siteId,
    SecondStreetPairs.pair AS secondStreetPairsPair, SecondStreetPairs.cableId AS secondStreetCableId, SecondStreetCables.name AS secondStreetCableName,
    SecondStreetCables.frameId AS secondStreetFrameId, SecondStreetFrames.name AS secondStreetFrameName,
    Broadband.username AS broadbandUsername, Broadband.password AS broadbandPassword, Broadband.portId AS broadbandPortId, Broadband.modemId AS modemModelId,
    ModemsModels.name AS modemModelName, ModemsTypes.id AS modemTypeId ,ModemsTypes.name AS modemTypeName,
    BroadbandPorts.port AS broadbandPort, BroadbandPorts.boardId AS boardId,
    DSLAMsBoards.slot AS boardSlot, DSLAMsBoards.dslamId AS dslamId,
    DSLAMs.name AS dslamName, DSLAMs.IPAddress AS dslamIpAddress, DSLAMs.siteId AS dslamSiteId, DSLAMs.routerId AS routerId,
    Routers.name AS routerName, Routers.IPAddress AS routerIpAddress,
    PhoneNumbers.id AS phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId,
    OfficeCodes.code AS officeCode, OfficeCodes.areaId,
    Wiring.remarks AS wiringRemarks
		FROM Wiring
			LEFT JOIN Distributor ON Distributor.id = Wiring.distributorId
			LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
			LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
			LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
			LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
			LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
			LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
			LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
			LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
			LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
			LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
            LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN StreetPairs SecondStreetPairs ON SecondStreetPairs.id = Wiring.secondStreetPairId
            LEFT JOIN StreetCables SecondStreetCables ON SecondStreetCables.id = SecondStreetPairs.cableId
            LEFT JOIN StreetFrames SecondStreetFrames ON SecondStreetFrames.id = SecondStreetCables.frameId
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = Broadband.portId
            LEFT JOIN ModemsModels ON ModemsModels.id = Broadband.modemId
            LEFT JOIN ModemsTypes ON ModemsTypes.id = ModemsModels.typeId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
            LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Distributor.id
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				WHERE OfficeCodes.id = officeCodeId AND PhoneNumbers.number = mcdu;
END$$

DELIMITER ;
