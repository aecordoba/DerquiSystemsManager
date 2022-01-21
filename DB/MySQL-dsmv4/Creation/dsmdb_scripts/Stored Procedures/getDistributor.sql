USE `dsmv4`;
DROP procedure IF EXISTS `getDistributor`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDistributor`(
IN officeCodeId INT,
IN number CHAR(4))
BEGIN

	SELECT Distributor.id AS distributorId, Distributor.available AS distributorAvailable,
    BlockPositions.id AS blockPositionId, BlockPositions.position AS blockPosition, BlockPositions.switchBlockId,
    NEAX61Sigma.id AS neax61sigmaId, NEAX61Sigma.timeSwitch, NEAX61Sigma.kHighway, NEAX61Sigma.pHighway, NEAX61Sigma.row, NEAX61Sigma.`column`,
    SigmaL3Addr.id AS sigmal3addrId, SigmaL3Addr.l3addr AS sigmal3addr, NEAX61SigmaELUs.id AS sigmaeluId, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, NEAX61SigmaELUs.siteId AS sigmaeluSiteId,
    NEAX61E.id AS neax61eId, NEAX61E.spce, NEAX61E.highway, NEAX61E.subhighway, NEAX61E.`group`, NEAX61E.switch, NEAX61E.level,
    Zhone.id AS zhoneId, Zhone.cable AS zhoneCable, Zhone.port AS zhonePort
		FROM Distributor
		LEFT JOIN BlockPositions ON BlockPositions.id = Distributor.blockPositionId
        LEFT JOIN NEAX61Sigma ON NEAX61Sigma.id = Distributor.neax61sigmaId
		LEFT JOIN SigmaL3Addr ON SigmaL3Addr.id = Distributor.sigmal3addrId
		LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
		LEFT JOIN NEAX61E ON NEAX61E.id = Distributor.neax61eId
        LEFT JOIN Zhone ON Zhone.id = Distributor.zhoneId
        LEFT JOIN Subscribers ON Subscribers.distributorId = Distributor.id
        LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
        LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
			WHERE PhoneNumbers.number = number AND PhoneNumbers.officeCodeId = officeCodeId;

END$$

DELIMITER ;
