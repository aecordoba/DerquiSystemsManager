USE `ac1`;
DROP procedure IF EXISTS `getWiringRecordList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getWiringRecordList`(
IN countryCode INT,
IN areaCode INT,
IN officeCode INT,
IN number CHAR(4)
)
BEGIN
	SELECT distinct(Distributor.id), SwitchBlocks.name AS switchBlockName, BlockPositions.position AS blockPositionsPosition,
		NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
		SigmaFrames.name AS sigmaFrameName, SigmaFrames.siteId AS sigmaSiteId, SigmaLineModules.name AS sigmaLineModuleName, 
		NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.siteId AS sigmaeluSiteId, SigmaL3Addr.l3addr AS sigmal3addr,
		NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, NEAX61E.switch, NEAX61E.level,
		EFrames.name AS eFrameName, EFrames.siteId AS eSiteId, ELineModules.name AS eLineModuleName,
		Zhone.cable AS zhoneCable, Zhone.port AS zhonePort, Zhone.siteId AS zhoneSiteId,
        StreetFrames.name AS streetFrameName, StreetCables.name AS streetCableName, StreetPairs.pair AS streetPairsPair,
        SecondStreetFrames.name AS secondStreetFrameName, SecondStreetCables.name AS secondStreetCableName, SecondStreetPairs.pair AS secondStreetPairsPair,
		Routers.name AS routerName, boardDSLAMRouter.name AS boardDslamRouterName,
		BroadbandRecord.username AS broadbandUsername, BroadbandRecord.password AS broadbandPassword,
        ModemsTypes.name AS modemTypeName, ModemsModels.name AS modemModelName,
		DSLAMs.name AS dslamName,
		boardDSLAM.name AS boardDSLAMName,
		DSLAMsBoards.slot AS dslamBoardSlot,
		BroadbandPorts.port AS broadbandPortsPort,
		WiringRecord.remarks AS remarks, Users.name AS username, People.firstName, People.lastName,
        WiringRecord.time
		FROM WiringRecord
			LEFT JOIN Distributor ON Distributor.id = WiringRecord.distributorId
			LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
			LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
			LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
			LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
			LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
			LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
			LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
			LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
			LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
			LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
			LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
            LEFT JOIN StreetPairs ON StreetPairs.id = WiringRecord.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN StreetPairs SecondStreetPairs ON SecondStreetPairs.id = WiringRecord.secondStreetPairId
            LEFT JOIN StreetCables SecondStreetCables ON SecondStreetCables.id = SecondStreetPairs.cableId
            LEFT JOIN StreetFrames SecondStreetFrames ON SecondStreetFrames.id = SecondStreetCables.frameId
            LEFT JOIN BroadbandRecord ON BroadbandRecord.id = WiringRecord.broadbandRecordId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = BroadbandRecord.portId
            LEFT JOIN ModemsModels ON ModemsModels.id = BroadbandRecord.modemId
            LEFT JOIN ModemsTypes ON ModemsTypes.id = ModemsModels.typeId
            LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
            LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs boardDSLAM ON boardDSLAM.id = DSLAMsBoards.dslamId
			LEFT JOIN Routers boardDSLAMRouter ON boardDSLAMRouter.id = boardDSLAM.routerId
            LEFT JOIN SubscribersRecord ON SubscribersRecord.distributorId = Distributor.id
            LEFT JOIN Addresses ON Addresses.id = SubscribersRecord.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
            LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
            LEFT JOIN Countries ON Countries.id = Areas.countryId
            LEFT JOIN Users ON Users.id = WiringRecord.userId
            LEFT JOIN People ON People.id = Users.personId
				WHERE Countries.code = countryCode AND Areas.code = areaCode AND OfficeCodes.code = officeCode AND PhoneNumbers.number = number
					ORDER BY WiringRecord.time;

END$$

DELIMITER ;
