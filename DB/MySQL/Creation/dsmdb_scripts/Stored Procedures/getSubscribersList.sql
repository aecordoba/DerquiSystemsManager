USE `ac1`;
DROP procedure IF EXISTS `getSubscribersList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscribersList`()
BEGIN

		SELECT Subscribers.id AS subscriberId, Subscribers.remarks, 
			Subscribers.addressId, Addresses.streetId, Addresses.number AS addressNumber, Addresses.floor, Addresses.apartment, Addresses.street1Id, Addresses.street2Id, Addresses.zipCode, Addresses.locationId,
            Addresses.phoneNumberId, PhoneNumbers.number AS phoneNumber, PhoneNumbers.officeCodeId AS phoneOfficeCodeId,
            OfficeCodes.code AS officeCode, OfficeCodes.areaId,
			Areas.code AS areaCode, Areas.name AS areaName, Areas.countryId,
			Countries.code AS countryCode, Countries.name AS countryName,
            Subscribers.distributorId,																				
			NEAX61Sigma.id AS neax61sigmaId, NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
			SigmaLineModules.id AS sigmaLineModuleId, SigmaLineModules.name AS sigmaLineModuleName,
			SigmaFrames.id AS sigmaFrameId, SigmaFrames.name AS sigmaFrameName, SigmaFrames.siteId AS sigmaFrameSiteId,
			SigmaL3Addr.id AS sigmal3addrId, SigmaL3Addr.l3addr AS sigmal3addr,
			NEAX61SigmaELUs.id AS sigmaeluId, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, NEAX61SigmaELUs.siteId AS sigmaeluSiteId,
			NEAX61E.id AS neax61eId, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, NEAX61E.switch, NEAX61E.level,
			ELineModules.id AS eLineModuleId, ELineModules.name AS eLineModuleName,
			EFrames.id AS eFrameId, EFrames.name AS eFrameName, EFrames.siteId AS eFrameSiteId,
			Zhone.id AS zhoneId, Zhone.cable AS zhoneCable, Zhone.port AS zhonePort, Zhone.siteId AS zhoneSiteId,
            Own.id AS ownerPersonId, Own.firstName AS ownerFirstName, Own.middleName AS ownerMiddleName, Own.lastName AS ownerLastName, Own.identificationTypeId AS ownerIdentificationTypeId, Own.identificationNumber AS ownerIdentificationNumber,
            Ass.id AS assigneePersonId, Ass.firstName AS assigneeFirstName, Ass.middleName AS assigneeMiddleName, Ass.lastName AS assigneeLastName, Ass.identificationTypeId AS assigneeIdentificationTypeId, Ass.identificationNumber AS assigneeIdentificationNumber,
            Wiring.id AS wiringId
				FROM Subscribers
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
                    LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
                    LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
                    LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
                    LEFT JOIN Countries ON Countries.id = Areas.countryId
                    LEFT JOIN Distributor ON Distributor.id = Subscribers.distributorId
                    LEFT JOIN Wiring ON Wiring.distributorId = Distributor.id
					LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
					LEFT JOIN SigmaLineModules ON SigmaLineModules.id = NEAX61Sigma.lineModuleId
					LEFT JOIN SigmaFrames ON SigmaFrames.id = SigmaLineModules.frameId
					LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
					LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
					LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
					LEFT JOIN ELineModules ON ELineModules.id = NEAX61E.lineModuleId
					LEFT JOIN EFrames ON EFrames.id = ELineModules.frameId
					LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
                    LEFT JOIN Owners ON  Owners.subscriberId = Subscribers.id
                    LEFT JOIN People Own ON Own.id = Owners.personId
                    LEFT JOIN Assignees ON  Assignees.subscriberId = Subscribers.id
                    LEFT JOIN People Ass ON Ass.id = Assignees.personId
				ORDER BY PhoneNumbers.officeCodeId, PhoneNumbers.number;
                
END$$

DELIMITER ;
