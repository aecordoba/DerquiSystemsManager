USE `ac1`;
DROP procedure IF EXISTS `getVacantSigmaL3AddrEquipmentList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getVacantSigmaL3AddrEquipmentList`(
IN siteId INT)
BEGIN
	SELECT SigmaL3Addr.id AS sigmal3addrId, SigmaL3Addr.l3addr AS sigmal3addr,
		NEAX61SigmaELUs.id AS sigmaeluId, NEAX61SigmaELUs.name AS sigmaeluName, NEAX61SigmaELUs.timeSwitch AS sigmaeluTimeSwitch, NEAX61SigmaELUs.kHighway AS sigmaeluKHighway, NEAX61SigmaELUs.pHighway AS sigmaeluPHighway, NEAX61SigmaELUs.siteId AS sigmaeluSiteId
			FROM SigmaL3Addr
				LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
				LEFT JOIN Distributor ON Distributor.sigmal3addrId = SigmaL3Addr.id
					WHERE NEAX61SigmaELUs.siteId = siteId
                        AND Distributor.available = TRUE
						AND Distributor.id NOT IN (SELECT distributorId
														FROM Subscribers);

END$$

DELIMITER ;
