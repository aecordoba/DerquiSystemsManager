USE `dsmv4`;
DROP procedure IF EXISTS `getWiringListByStreetCable`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getWiringListByStreetCable`(
IN cableId INT)
BEGIN
	SELECT COALESCE(Countries.code, SecondCountries.code) AS countryCode, COALESCE(Areas.code, SecondAreas.code) AS areaCode, COALESCE(OfficeCodes.code, SecondOfficeCodes.code) AS officeCode, COALESCE(PhoneNumbers.number, SecondPhoneNumbers.number) AS number,
		StreetFrames.siteId, StreetFrames.name AS frameName,StreetCables.name AS cableName, StreetPairs.pair as pair,
		DSLAMs.name AS dslamName, DSLAMsBoards.slot AS boardSlot, BroadbandPorts.port AS broadbandPort,
        COALESCE(SwitchBlocks.name, SecondSwitchBlocks.name) AS switchBlockName, COALESCE(BlockPositions.position, SecondBlockPositions.position) AS blockPosition
        FROM StreetPairs
			LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN Wiring ON Wiring.streetPairId = StreetPairs.id
            LEFT JOIN Wiring SecondWiring ON SecondWiring.secondStreetPairId = StreetPairs.id
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN BroadbandPorts ON BroadbandPorts.id = Broadband.portId
            LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
            LEFT JOIN DSLAMs ON DSLAMs.id = DSLAMsBoards.dslamId
            LEFT JOIN Distributor ON Distributor.id = Wiring.distributorId
            LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
            LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Wiring.distributorId
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
            LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
            LEFT JOIN Countries ON Countries.id = Areas.countryId
            LEFT JOIN Distributor SecondDistributor ON SecondDistributor.id = SecondWiring.distributorId
            LEFT JOIN BlockPositions SecondBlockPositions ON SecondBlockPositions.id = SecondDistributor.blockPositionId
            LEFT JOIN SwitchBlocks SecondSwitchBlocks ON SecondSwitchBlocks.id = SecondBlockPositions.switchBlockId
            LEFT JOIN Subscribers SecondSubscribers ON SecondSubscribers.distributorId = SecondWiring.distributorId
            LEFT JOIN Addresses SecondAddresses ON SecondAddresses.id = SecondSubscribers.addressId
            LEFT JOIN PhoneNumbers SecondPhoneNumbers ON SecondPhoneNumbers.id = SecondAddresses.phoneNumberId
            LEFT JOIN OfficeCodes SecondOfficeCodes ON SecondOfficeCodes.id = SecondPhoneNumbers.officeCodeId
            LEFT JOIN Areas SecondAreas ON SecondAreas.id = SecondOfficeCodes.areaId
            LEFT JOIN Countries SecondCountries ON SecondCountries.id = SecondAreas.countryId
				WHERE StreetCables.id = cableId
					ORDER BY StreetCables.name, StreetPairs.pair;
END$$

DELIMITER ;
