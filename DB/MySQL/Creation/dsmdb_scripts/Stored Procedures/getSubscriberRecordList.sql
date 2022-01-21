USE `ac1`;
DROP procedure IF EXISTS `getSubscriberRecordList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberRecordList`(
IN countryCode INT,
IN areaCode INT,
IN officeCode INT,
IN number CHAR(4)
)
BEGIN
		SELECT SubscribersRecord.remarks,
			NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
			SigmaFrames.name AS sigmaFrameName, SigmaLineModules.name AS sigmaLineModuleName,
			SigmaL3Addr.l3addr AS sigmal3addr,
			NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway,
			NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, NEAX61E.switch, NEAX61E.level,
			EFrames.name AS eFrameName, ELineModules.name AS eLineModuleName,
			Zhone.cable AS zhoneCable, Zhone.port AS zhonePort,
            Sites.code AS siteCode, SwitchBlocks.name AS switchBlockName, BlockPositions.position AS blockPosition,
			Addresses.streetId, Addresses.number AS addressNumber, Addresses.floor, Addresses.apartment, Addresses.street1Id, Addresses.street2Id, Addresses.zipCode, 
            Locations.name AS location, Cities.name AS city, States.name AS state, AddressCountries.name AS country,
            Own.firstName AS ownerFirstName, Own.middleName AS ownerMiddleName, Own.lastName AS ownerLastName, OwnIdentificationsTypes.name AS ownerIdentificationType, Own.identificationNumber AS ownerIdentificationNumber,
            Ass.firstName AS assigneeFirstName, Ass.middleName AS assigneeMiddleName, Ass.lastName AS assigneeLastName, AssIdentificationsTypes.name AS assigneeIdentificationType, Ass.identificationNumber AS assigneeIdentificationNumber,
			Users.name AS username, People.firstName, People.lastName,
			SubscribersRecord.time
				FROM SubscribersRecord
					LEFT JOIN Addresses ON Addresses.id = SubscribersRecord.addressId
                    LEFT JOIN Locations ON Locations.id = Addresses.locationId
                    LEFT JOIN Cities ON Cities.id = Locations.cityId
                    LEFT JOIN States ON States.id = Cities.stateId
                    LEFT JOIN Countries AddressCountries ON AddressCountries.id = States.countryId
                    LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
                    LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
                    LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
                    LEFT JOIN Countries ON Countries.id = Areas.countryId
                    LEFT JOIN Distributor ON Distributor.id = SubscribersRecord.distributorId
					LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
					LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
					LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
					LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
					LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
					LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
					LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
					LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
					LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
                    LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
                    LEFT JOIN SwitchBlocks ON SwitchBlocks.id = BlockPositions.switchBlockId
                    LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
                    LEFT JOIN People Own ON Own.id = SubscribersRecord.ownerId
                    LEFT JOIN IdentificationsTypes OwnIdentificationsTypes ON OwnIdentificationsTypes.id = Own.identificationTypeId
                    LEFT JOIN People Ass ON Ass.id = SubscribersRecord.assigneeId
                    LEFT JOIN IdentificationsTypes AssIdentificationsTypes ON AssIdentificationsTypes.id = Ass.identificationTypeId
					LEFT JOIN Users ON Users.id = SubscribersRecord.userId
					LEFT JOIN People ON People.id = Users.personId
						WHERE Countries.code = countryCode AND Areas.code = areaCode AND OfficeCodes.code = officeCode AND PhoneNumbers.number = number
							ORDER BY SubscribersRecord.time;

END$$

DELIMITER ;
