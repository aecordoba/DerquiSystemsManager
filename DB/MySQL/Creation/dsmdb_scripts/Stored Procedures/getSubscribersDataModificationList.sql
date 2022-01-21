USE `ac1`;
DROP procedure IF EXISTS `getSubscribersDataModificationList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscribersDataModificationList`(
IN startDate DATE,
IN endDate DATE)
BEGIN
	CREATE TEMPORARY TABLE LastModificationOnPeriod ENGINE = MEMORY
		SELECT Subscribers.id AS subscriberId, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number,
			SubscriberLineClasses.type AS lineClassType, 
			TerminationRestrictions.name AS terminationRestrictionName,
            OriginationRestrictions.name AS originationRestrictionName,
			SubscriberStates.name AS stateName, SubscriberBroadbandStates.name AS broadbandStateName,
            SubscribersDataRecord.services, SubscribersDataRecord.information,
            Distributor.neax61sigmaId, Distributor.sigmal3addrId, Distributor.neax61eId, Distributor.zhoneId,
			Users.name AS username, People.firstName, People.lastName,
			SubscribersDataRecord.time
				FROM SubscribersDataRecord
					LEFT JOIN Subscribers ON Subscribers.dataId = SubscribersDataRecord.dataId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
					LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
					LEFT JOIN Countries ON Countries.id = Areas.countryId
					LEFT JOIN SubscriberLineClasses ON SubscriberLineClasses.id = SubscribersDataRecord.lineClassId
					LEFT JOIN SubscriberRestrictions ON SubscriberRestrictions.id = SubscribersDataRecord.restrictionId
                    LEFT JOIN TerminationRestrictions ON TerminationRestrictions.id = SubscriberRestrictions.terminationRestrictionId
                    LEFT JOIN OriginationRestrictions ON OriginationRestrictions.id = SubscriberRestrictions.originationRestrictionId
					LEFT JOIN SubscriberStates ON SubscriberStates.id = SubscribersDataRecord.stateId
                    LEFT JOIN SubscriberBroadbandStates ON SubscriberBroadbandStates.id = SubscribersDataRecord.broadbandStateId
                    LEFT JOIN Distributor ON Subscribers.distributorId = Distributor.id
                    LEFT JOIN Users ON Users.id = SubscribersDataRecord.userId
					LEFT JOIN People ON People.id = Users.personId
						WHERE SubscribersDataRecord.time = (SELECT MAX(SDR.time)
																FROM SubscribersDataRecord SDR
																	LEFT JOIN Subscribers S ON S.dataId = SDR.dataId
																		WHERE SDR.time > startDate 
																			AND SDR.time < endDate
																			AND S.dataId = Subscribers.dataId);

	CREATE TEMPORARY TABLE LastModificationBeforePeriod ENGINE = MEMORY
		SELECT Subscribers.id AS subscriberId, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number,
			SubscriberLineClasses.type AS lineClassType, 
			TerminationRestrictions.name AS terminationRestrictionName,
            OriginationRestrictions.name AS originationRestrictionName,
			SubscriberStates.name AS stateName, SubscriberBroadbandStates.name AS broadbandStateName,
            SubscribersDataRecord.services, SubscribersDataRecord.information,
            Distributor.neax61sigmaId, Distributor.sigmal3addrId, Distributor.neax61eId, Distributor.zhoneId,
			Users.name AS username, People.firstName, People.lastName,
			SubscribersDataRecord.time
				FROM SubscribersDataRecord
					LEFT JOIN Subscribers ON Subscribers.dataId = SubscribersDataRecord.dataId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
					LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
					LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
					LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
					LEFT JOIN Countries ON Countries.id = Areas.countryId
					LEFT JOIN SubscriberLineClasses ON SubscriberLineClasses.id = SubscribersDataRecord.lineClassId
					LEFT JOIN SubscriberRestrictions ON SubscriberRestrictions.id = SubscribersDataRecord.restrictionId
                    LEFT JOIN TerminationRestrictions ON TerminationRestrictions.id = SubscriberRestrictions.terminationRestrictionId
                    LEFT JOIN OriginationRestrictions ON OriginationRestrictions.id = SubscriberRestrictions.originationRestrictionId
					LEFT JOIN SubscriberStates ON SubscriberStates.id = SubscribersDataRecord.stateId
                    LEFT JOIN SubscriberBroadbandStates ON SubscriberBroadbandStates.id = SubscribersDataRecord.broadbandStateId
                    LEFT JOIN Distributor ON Subscribers.distributorId = Distributor.id
					LEFT JOIN Users ON Users.id = SubscribersDataRecord.userId
					LEFT JOIN People ON People.id = Users.personId
						WHERE SubscribersDataRecord.time = (SELECT MAX(SDR.time)
																FROM SubscribersDataRecord SDR
																	LEFT JOIN Subscribers S ON S.dataId = SDR.dataId
																		WHERE SDR.time < startDate 
																			AND S.dataId = Subscribers.dataId)
							AND Subscribers.id IN (SELECT subscriberId FROM LastModificationOnPeriod);

	CREATE TEMPORARY TABLE LastBroadbandModificationOnPeriod ENGINE = MEMORY
		SELECT Subscribers.id AS subscriberId, Routers.name AS routerName, BoardDSLAMsRouters.name AS boardDSLAMRouterName, BroadbandRecord.username AS broadbandUsername
			FROM Subscribers
				LEFT JOIN SubscribersRecord ON SubscribersRecord.addressId = Subscribers.addressId
				LEFT JOIN WiringRecord ON WiringRecord.distributorId = SubscribersRecord.distributorId
				LEFT JOIN BroadbandRecord ON BroadbandRecord.id = WiringRecord.broadbandRecordId
				LEFT JOIN BroadbandPorts ON BroadbandPorts.id = BroadbandRecord.portId
				LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
				LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs BoardDSLAMs ON BoardDSLAMs.id = DSLAMsBoards.dslamId
				LEFT JOIN Routers BoardDSLAMsRouters ON BoardDSLAMsRouters.id = BoardDSLAMs.routerId
					WHERE BroadbandRecord.time = (SELECT MAX(BR.time)
													FROM BroadbandRecord BR
														LEFT JOIN WiringRecord WR ON WR.broadbandRecordId = BR.id
														LEFT JOIN Subscribers S ON S.distributorId = WR.distributorId
															WHERE S.id = Subscribers.id
																AND BR.time > startDate
																AND BR.time < endDate)
						AND Subscribers.id IN (SELECT subscriberId FROM LastModificationOnPeriod);
    
	CREATE TEMPORARY TABLE LastBroadbandModificationBeforePeriod ENGINE = MEMORY
		SELECT Subscribers.id AS subscriberId, Routers.name AS routerName, BoardDSLAMsRouters.name AS boardDSLAMRouterName, BroadbandRecord.username AS broadbandUsername
			FROM Subscribers
				LEFT JOIN SubscribersRecord ON SubscribersRecord.addressId = Subscribers.addressId
				LEFT JOIN WiringRecord ON WiringRecord.distributorId = SubscribersRecord.distributorId
				LEFT JOIN BroadbandRecord ON BroadbandRecord.id = WiringRecord.broadbandRecordId
				LEFT JOIN BroadbandPorts ON BroadbandPorts.id = BroadbandRecord.portId
				LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
				LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs BoardDSLAMs ON BoardDSLAMs.id = DSLAMsBoards.dslamId
				LEFT JOIN Routers BoardDSLAMsRouters ON BoardDSLAMsRouters.id = BoardDSLAMs.routerId
					WHERE BroadbandRecord.time = (SELECT MAX(BR.time)
													FROM BroadbandRecord BR
														LEFT JOIN WiringRecord WR ON WR.broadbandRecordId = BR.id
														LEFT JOIN Subscribers S ON S.distributorId = WR.distributorId
															WHERE S.id = Subscribers.id
																AND BR.time < startDate)
						AND Subscribers.id IN (SELECT subscriberId FROM LastModificationBeforePeriod);
    
    
		SELECT LastModificationBeforePeriod.subscriberId, countryCode, areaCode, officeCode, number, lineClassType, terminationRestrictionName, originationRestrictionName,
			stateName, broadbandStateName, services, information,
            neax61sigmaId, sigmal3addrId, neax61eId, zhoneId,
			routerName, boardDSLAMRouterName, broadbandUsername,
			username, firstName, lastName,
			time
				FROM LastModificationBeforePeriod
					LEFT JOIN LastBroadbandModificationBeforePeriod ON LastBroadbandModificationBeforePeriod.subscriberId = LastModificationBeforePeriod.subscriberId
		UNION ALL
		SELECT LastModificationOnPeriod.subscriberId, countryCode, areaCode, officeCode, number, lineClassType, terminationRestrictionName, originationRestrictionName,
			stateName, broadbandStateName, services, information,
            neax61sigmaId, sigmal3addrId, neax61eId, zhoneId,
			routerName, boardDSLAMRouterName, broadbandUsername,
			username, firstName, lastName,
			time
				FROM LastModificationOnPeriod
					LEFT JOIN LastBroadbandModificationOnPeriod ON LastBroadbandModificationOnPeriod.subscriberId = LastModificationOnPeriod.subscriberId
		ORDER BY countryCode, areaCode, officeCode, number, time;

	DROP TEMPORARY TABLE IF EXISTS LastModificationOnPeriod;
	DROP TEMPORARY TABLE IF EXISTS LastModificationBeforePeriod;
	DROP TEMPORARY TABLE IF EXISTS LastBroadbandModificationOnPeriod;
	DROP TEMPORARY TABLE IF EXISTS LastBroadbandModificationBeforePeriod;

END$$

DELIMITER ;
