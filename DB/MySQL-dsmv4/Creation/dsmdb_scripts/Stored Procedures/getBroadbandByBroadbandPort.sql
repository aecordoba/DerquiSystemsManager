USE `dsmv4`;
DROP procedure IF EXISTS `getBroadbandByBroadbandPort`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getBroadbandByBroadbandPort` (
IN broadbandPortId INT)
BEGIN
	SELECT Wiring.id AS wiringId, Wiring.distributorId, Wiring.broadbandId,
    Broadband.username AS broadbandUsername, Broadband.password AS broadbandPassword, Broadband.portId AS broadbandPortId,
    Broadband.modemId AS modemModelId, ModemsModels.name AS modemModelName, ModemsTypes.id AS modemTypeId ,ModemsTypes.name AS modemTypeName,
    BroadbandPorts.port AS broadbandPort, BroadbandPorts.boardId AS boardId,
    DSLAMsBoards.slot AS boardSlot, DSLAMsBoards.dslamId AS dslamId,
    DSLAMs.name AS dslamName, DSLAMs.IPAddress AS dslamIpAddress, DSLAMs.siteId AS dslamSiteId,
    SubscriberBroadbandStates.id AS broadbandStateId, SubscriberBroadbandStates.name AS broadbandStateName,
    PhoneNumbers.id AS phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId,
    OfficeCodes.code AS officeCode, OfficeCodes.areaId
		FROM Wiring
			LEFT JOIN Distributor ON Distributor.id = Wiring.distributorId
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = Broadband.portId
            LEFT JOIN ModemsModels ON ModemsModels.id = Broadband.modemId
            LEFT JOIN ModemsTypes ON ModemsTypes.id = ModemsModels.typeId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Distributor.id
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
            LEFT JOIN SubscribersData ON SubscribersData.id = Subscribers.dataId
            LEFT JOIN SubscriberBroadbandStates ON SubscriberBroadbandStates.id = SubscribersData.broadbandStateId
				WHERE BroadbandPorts.id = broadbandPortId;
END$$

DELIMITER ;
