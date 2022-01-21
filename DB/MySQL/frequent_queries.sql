CALL getBroadbandByPhoneNumber(1, '6535');
CALL getBroadbandInstallationsList("2017-12-01", "2017-12-28");
CALL getBroadbandUninstallationsList("2017-12-14", "2017-12-15");
CALL getBroadbandPortsList();
CALL getDistributionWiringByPhoneNumber(1, '6407');
CALL getDistributionWiringByBlockPosition(1, '01AB');
CALL getDistributionWiringByBroadbandPort(5);
CALL getDistributionWiringByStreetPair(5);
CALL getDistributor(1, '0001');
CALL getDistributorList(102);
CALL getDSLAMsBoardsModelsList();
CALL getDSLAMsList();
CALL getDSLAMsModelsList();
CALL getFeaturesList();
CALL getFreeBroadbandPortsList();
CALL getInstalledBroadbandList("2017-12-15");
CALL getLocationsList();
CALL getModemsModelsList();
CALL getModifiedSubscribersDataList("2017-12-28", "2017-12-29");
CALL getNotInstalledBroadbandList("2017-07-01");
CALL getOfficeCodesDataList();
CALL getOwnNumerationList();
CALL getPeopleList();
CALL getRepairing(2018);
CALL getRepairmenDataList();
CALL getRepairingTypesList();
CALL getRouter(1);
CALL getRolesList();
CALL getRoutersList();
CALL getRoutersModelsList();
CALL getServiceOrdersList();
CALL getSitesList();
CALL getStreetsList();
CALL getStreetPairsList();
CALL getSubscriberBroadbandStatesList();
CALL getSubscriberData(5);
CALL getSubscriberDataRecordList(54, 230, 448, '0008');
CALL getSubscriberLineClassesList();
CALL getSubscriberLineClassesList();
CALL getSubscriberRecordList(54, 230, 448, '4950');
CALL getSubscriberRestrictionsList();
CALL getSubscriberServicesList();
CALL getSubscribersDataList();
CALL getSubscribersDataModificationList("2017-12-26", "2017-12-28");
CALL getSubscribersList();
CALL getSubscriberStatesList();
CALL getSwitchBlocksList();
CALL getTime();
CALL getVacantSigmaL3AddrEquipmentList(3);
CALL getWiring(14);
CALL getWiringList();
CALL getWiringListByStreetCable(35);
CALL getWiringRecordList(54, 230, 448, '4535');

DROP TEMPORARY TABLE IF EXISTS LastWiredOnPeriod;
DROP TEMPORARY TABLE IF EXISTS LastWiredBeforePeriod;
DROP TEMPORARY TABLE IF EXISTS LastModificationOnPeriod;
DROP TEMPORARY TABLE IF EXISTS LastModificationBeforePeriod;
DROP TEMPORARY TABLE IF EXISTS LastBroadbandModificationOnPeriod;
DROP TEMPORARY TABLE IF EXISTS LastBroadbandModificationBeforePeriod;

SELECT * FROM Addresses;
SELECT * FROM Areas;
SELECT * FROM Broadband;
SELECT * FROM BroadbandRecord;
SELECT * FROM BroadbandPorts;
SELECT * FROM Distributor;
SELECT * FROM DSLAMs;
SELECT * FROM Features;
SELECT * FROM Locations;
SELECT * FROM OfficeCodes;
SELECT * FROM OriginationRestrictions;
SELECT * FROM OwnPhoneNumbers;
SELECT * FROM People;
SELECT * FROM PhoneNumbers;
SELECT * FROM Repairings;
SELECT * FROM RepairingTypes;
SELECT * FROM Roles;
SELECT * FROM Routers;
SELECT * FROM Streets;
SELECT * FROM StreetCables;
SELECT * FROM StreetFrames;
SELECT * FROM StreetPairs;
SELECT * FROM Streets;
SELECT * FROM SubscriberLineClasses;
SELECT * FROM SubscriberRestrictions;
SELECT * FROM Subscribers;
SELECT * FROM SubscribersData;
SELECT * FROM SubscribersDataRecord;
SELECT * FROM SubscribersDataServices;
SELECT * FROM SubscriberBroadbandStates;
SELECT * FROM SubscriberServices;
SELECT * FROM SubscriberStates;
SELECT * FROM SubscribersRecord;
SELECT * FROM SwitchBlocks;
SELECT * FROM TerminationRestrictions;
SELECT * FROM Users;
SELECT * FROM Wiring;
SELECT * FROM Zhone;

SELECT Subscribers.id
	FROM Subscribers
		LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
        LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
        LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
        LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
        LEFT JOIN Countries ON Countries.id = Areas.countryId
			WHERE Countries.code = 54 AND Areas.code = 230 AND OfficeCodes.code = 448 AND PhoneNumbers.number = '4020';
            
SELECT StreetPairs.id, StreetPairs.pair
	FROM StreetPairs
		LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
        LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
        LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
			WHERE StreetCables.name = 'P' AND StreetFrames.name = 'C' AND Sites.code = 'DRQ';

SELECT Wiring.id AS wiringId, Wiring.distributorId, Wiring.streetPairId, Wiring.secondStreetPairId,
    StreetPairs.pair AS streetPairsPair, StreetPairs.cableId AS streetCableId, StreetCables.name AS streetCableName,
    StreetCables.frameId AS streetFrameId, StreetFrames.name AS streetFrameName, StreetFrames.siteId,
    SecondStreetPairs.pair AS secondStreetPairsPair, SecondStreetPairs.cableId AS secondStreetCableId, SecondStreetCables.name AS secondStreetCableName,
    SecondStreetCables.frameId AS secondStreetFrameId, SecondStreetFrames.name AS secondStreetFrameName,
    PhoneNumbers.id AS phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId,
    OfficeCodes.code AS officeCode, OfficeCodes.areaId,
    Wiring.remarks AS wiringRemarks
		FROM Wiring
            LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN StreetPairs SecondStreetPairs ON SecondStreetPairs.id = Wiring.secondStreetPairId
            LEFT JOIN StreetCables SecondStreetCables ON SecondStreetCables.id = SecondStreetPairs.cableId
            LEFT JOIN StreetFrames SecondStreetFrames ON SecondStreetFrames.id = SecondStreetCables.frameId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Wiring.distributorId
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				WHERE OfficeCodes.code = 448 AND PhoneNumbers.number = '4020';

-- Same pair with more than one phone number.
SELECT OfficeCodes.code AS officeCode, PhoneNumbers.number AS phoneNumber,
	Sites.name AS site, StreetFrames.name AS streetFrameName, StreetCables.name AS streetCableName, StreetPairs.pair AS streetPairsPair
		FROM Wiring
            LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Wiring.distributorId
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				WHERE StreetPairs.id IN (SELECT streetPairId
											FROM Wiring
												GROUP BY Wiring.streetPairId
													HAVING count(Wiring.Id) > 1)
					ORDER BY Wiring.streetPairId;

-- Same broadband with more than one phone number.
SELECT OfficeCodes.code AS officeCode, PhoneNumbers.number AS phoneNumber,
	Sites.name AS site, StreetFrames.name AS streetFrameName, StreetCables.name AS streetCableName, StreetPairs.pair AS streetPairsPair
		FROM Wiring
            LEFT JOIN Broadband ON Broadband.id = Wiring.broadbandId
            LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
            LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
            LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
            LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
            LEFT JOIN Subscribers ON Subscribers.distributorId = Wiring.distributorId
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
				WHERE Broadband.id IN (SELECT broadbandId
											FROM Wiring
												GROUP BY Wiring.broadbandId
													HAVING count(Wiring.Id) > 1)
					ORDER BY Wiring.broadbandId;


SELECT count(Wiring.id), Wiring.streetPairId, Sites.name, StreetFrames.name, StreetCables.name, StreetPairs.pair
	FROM Wiring
		LEFT JOIN StreetPairs ON StreetPairs.id = Wiring.streetPairId
        LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
        LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
        LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
			GROUP BY Wiring.streetPairId
				HAVING count(Wiring.id) > 1;


SELECT *
	FROM Wiring
		WHERE streetPairId = 11090;

SELECT *
	FROM Wiring
		WHERE id = 3286;

SELECT *
	FROM SubscribersRecord;
    
SELECT *
	FROM WiringRecord
		GROUP BY distributorId
			HAVING MAX(time) AND broadbandRecordId IS NULL;

SELECT *
	FROM WiringRecord
		WHERE distributorId = 9746;

SELECT Subscribers.id, Routers.name AS routerName, BoardDSLAMsRouters.name AS boardDSLAMRouterName, BroadbandRecord.username
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
														AND BR.time > "2017-12-26"
                                                        AND BR.time < "2017-12-28");





SELECT Subscribers.id, WiringRecord.distributorId, WiringRecord.broadbandRecordId, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number, WiringRecord.time
	FROM WiringRecord
			LEFT JOIN Subscribers ON Subscribers.distributorId = WiringRecord.distributorId
            LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
            LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
            LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
            LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
            LEFT JOIN Countries ON Countries.id = Areas.countryId
				ORDER BY officeCode, number, time;

SELECT Subscribers.id, WiringRecord.broadbandRecordId, Countries.code AS countryCode, Areas.code AS areaCode, OfficeCodes.code AS officeCode, PhoneNumbers.number, WiringRecord.time
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
													WHERE WR.time > "2017-06-01" 
														AND WR.time < "2017-07-08" 
														AND S.distributorId = Subscribers.distributorId)
				AND WiringRecord.broadbandRecordId IS NOT NULL;

SELECT Subscribers.id
	FROM WiringRecord
		LEFT JOIN Subscribers ON Subscribers.distributorId = WiringRecord.distributorId
			WHERE WiringRecord.time = (SELECT MAX(WR.time)
										FROM WiringRecord WR
											WHERE WR.time < "2017-06-01" 
												AND WR.distributorId = WiringRecord.distributorId)
				AND WiringRecord.broadbandRecordId IS NOT NULL;

SELECT Sites.name AS site, StreetFrames.name AS frame, StreetCables.name AS cable, StreetPairs.pair AS pair
	FROM StreetPairs
		LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
		LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
		LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
			WHERE StreetPairs.id = 11090;

SELECT StreetPairs.id AS streetPairId, StreetFrames.name AS frame, StreetCables.name AS cable, StreetPairs.pair AS pair
	FROM StreetPairs
		LEFT JOIN StreetCables ON StreetCables.id = StreetPairs.cableId
		LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
		LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
			WHERE Sites.name = 'MUZILLI' AND StreetPairs.id NOT IN (SELECT streetPairId FROM Wiring);

SELECT *
	FROM StreetPairs
		WHERE cableId = 41;


SELECT *
	FROM Distributor
		WHERE id > 11345;

SELECT Distributor.id, NEAX61SigmaELUs.name AS eluName, sigmal3addrId, SigmaL3Addr.l3addr, SwitchBlocks.name AS switchBlockName, BlockPositions.position
	FROM Distributor
		LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
        LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
        LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
        LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
        LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
			WHERE SwitchBlocks.name = 'H07D' AND Sites.code = 'MZL';

	SELECT ServiceOrders.creationTime
		FROM ServiceOrders
			LEFT JOIN Repairings ON Repairings.serviceOrderId = ServiceOrders.id
				WHERE ServiceOrders.id = 2018;

--DELETE FROM Distributor
	WHERE id >= 11433 AND id <= 11448;
    
SELECT *
	FROM BlockPositions
		LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
        LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
			WHERE SwitchBlocks.name = 'H07D' AND Sites.code = 'MZL';

--DELETE FROM BlockPositions
	WHERE id >= 11537 AND id <= 11560;

SELECT *
	FROM SigmaL3Addr
		LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
			WHERE NEAX61SigmaELUs.name = 'elu03';
    
SELECT *
	FROM SigmaL3Addr
		LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId;
    

--DELETE FROM Distributor
	WHERE sigmal3addrId >= 105 AND sigmal3addrId <= 120;

--DELETE FROM SigmaL3Addr
		WHERE id >= 313 AND id <= 120;
        
--UPDATE Distributor
	SET sigmal3addrId = 224
		WHERE id = 11344;
    
    
    
SELECT *
	FROM OfficeCodes;

SELECT *
	FROM BroadbandPorts
		WHERE dslamId = 4;

SELECT *
	FROM BroadbandPorts
		WHERE boardId = 6;

    
SELECT *
	FROM DSLAMsBoards;

SELECT *
	FROM Sites;
    
SELECT *
	FROM StreetFrames;

SELECT *
	FROM SigmaLineModules
		WHERE frameId = 1;

SELECT *
	FROM StreetCables;

SELECT * 
	FROM SigmaFrames;

SELECT * 
	FROM EFrames;

SELECT *
	FROM SwitchBlocks;

SELECT *
	FROM BlockPositions;
    
SELECT *
	FROM PhoneNumbers;

SELECT *
	FROM Users;

SELECT distributorId
	FROM WiringRecord
		WHERE time > "2017-06-01" AND time < "2017-06-23"
			AND broadbandRecordId IS NOT NULL;
            
--UPDATE SigmaFrames
	SET name = 'A-LTF A001'
		WHERE id = 2;
 
--UPDATE Zhone
	SET cable = '02'
		WHERE cable = '01';

--DELETE FROM DSLAMs
	WHERE id = 5;

--DELETE FROM Broadband
	WHERE portId = 1;

--DELETE FROM BroadbandRecord;

--DELETE FROM Wiring;

--DELETE FROM WiringRecord;

--DELETE FROM OwnNumeration;

--DELETE FROM StreetPairs
	WHERE cableId IN (SELECT StreetCables.id FROM StreetCables
							LEFT JOIN StreetFrames ON StreetFrames.id = StreetCables.frameId
                            LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
								WHERE Sites.code = 'SPD');
                                
--DELETE FROM StreetCables
	WHERE frameId IN (SELECT StreetFrames.id FROM StreetFrames
                            LEFT JOIN Sites ON Sites.id = StreetFrames.siteId
								WHERE Sites.code = 'SPD');

--DELETE FROM StreetPairs
	WHERE StreetPairs.id > 14250 AND StreetPairs.id < 14301;

--DELETE FROM UsersRoles;

--DELETE FROM Users;

--INSERT INTO Streets(id, name)
	VALUES(1, 'DORREGO'),
    (2, 'SARMIENTO'),
    (3, 'PERÓN, JUAN D.'),
    (4, 'PERÓN, EVA'),
    (5, 'LAS GARDENIAS'),
    (6, 'LAS MARGARITAS'),
    (7, 'BELGRANO'),
    (8, 'LAVALLE'),
    (9, 'GÜEMES'),
    (10, 'CROVARA'),
    (11, 'SAENZ PEÑA');
    
--INSERT INTO RepairsCompanies(name)
	VALUES('Lores'),
		('García');
        
--INSERT INTO People(firstName, middleName, lastName)
	VALUES('Juan', 'Ramón', 'Gomez'),
    ('Daniel', NULL, 'Valori'),
    ('Sunchi',NULL, 'Perez'),
    ('Severino', 'Artemio', 'Alí');
    
--INSERT INTO Repairmen(personId, repairsCompanyId, services)
	VALUES((SELECT id FROM People WHERE firstName = 'Juan' AND lastName = 'Gomez'), (SELECT id FROM RepairsCompanies WHERE name = 'Lores'), 'Especialista en banda ancha.'),
		((SELECT id FROM People WHERE firstName = 'Daniel' AND lastName = 'Valori'), (SELECT id FROM RepairsCompanies WHERE name = 'García'), NULL),
        ((SELECT id FROM People WHERE firstName = 'Sunchi' AND lastName = 'Perez'), (SELECT id FROM RepairsCompanies WHERE name = 'Lores'), 'Reparaciones'),
        ((SELECT id FROM People WHERE firstName = 'Severino' AND lastName = 'Alí'), (SELECT id FROM RepairsCompanies WHERE name = 'Lores'), 'Especialista en banda ancha.');


SELECT @@sql_mode;