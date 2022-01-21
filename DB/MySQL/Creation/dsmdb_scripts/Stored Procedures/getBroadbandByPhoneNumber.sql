USE `ac1`;
DROP procedure IF EXISTS `getBroadbandByPhoneNumber`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getBroadbandByPhoneNumber`(
IN officeCodeId INT,
IN mcdu CHAR(4))
BEGIN
	SELECT Wiring.id AS wiringId, Wiring.distributorId, Wiring.broadbandId,
    Broadband.username AS broadbandUsername, Broadband.password AS broadbandPassword, Broadband.portId AS broadbandPortId, Broadband.modemId AS modemModelId,
    ModemsModels.name AS modemModelName, ModemsTypes.id AS modemTypeId ,ModemsTypes.name AS modemTypeName,
    BroadbandPorts.port AS broadbandPortsPort, BroadbandPorts.dslamId AS broadbandDSLAMId, BroadbandPorts.boardId AS broadbandBoardId,
    DSLAMs.name AS dslamName, DSLAMs.IPAddress AS dslamIpAddress, DSLAMs.siteId AS dslamSiteId, DSLAMs.routerId AS dslamRouterId,
    Routers.name AS dslamRouterName, Routers.IPAddress AS dslamRouterIpAddress,
    DSLAMsBoards.slot AS dslamBoardSlot, DSLAMsBoards.dslamId AS boardDSLAMId,
    boardDSLAM.name AS boardDSLAMName, boardDSLAM.IPAddress AS boardDSLAMIpAddress, boardDSLAM.siteId AS boardDSLAMSiteId, boardDSLAM.routerId AS boardDSLAMRouterId,
    boardDSLAMRouter.name AS boardDSLAMRouterName, boardDSLAMRouter.IPAddress AS boardDSLAMRouterIpAddress,
    SubscriberBroadbandStates.id AS broadbandStateId, SubscriberBroadbandStates.name AS broadbandStateName,
    PhoneNumbers.id AS phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId,
    OfficeCodes.code AS officeCode, OfficeCodes.areaId
		FROM Wiring
			LEFT JOIN Distributor ON Distributor.id = Wiring.distributorId
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = Broadband.portId
            LEFT JOIN ModemsModels ON ModemsModels.id = Broadband.modemId
            LEFT JOIN ModemsTypes ON ModemsTypes.id = ModemsModels.typeId
            LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
            LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs boardDSLAM ON boardDSLAM.id = DSLAMsBoards.dslamId
            LEFT JOIN Routers boardDSLAMRouter ON boardDSLAMRouter.id = boardDSLAM.routerId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Distributor.id
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
            LEFT JOIN SubscribersData ON SubscribersData.id = Subscribers.dataId
            LEFT JOIN SubscriberBroadbandStates ON SubscriberBroadbandStates.id = SubscribersData.broadbandStateId
				WHERE OfficeCodes.id = officeCodeId AND PhoneNumbers.number = mcdu;
END$$

DELIMITER ;
