USE `ac1`;
DROP procedure IF EXISTS `getNeax61SigmaELUIdentifiers`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getNeax61SigmaELUIdentifiers`(
IN name VARCHAR(8),
IN l3addr INT
)
BEGIN
	SELECT SigmaL3Addr.id AS sigmal3addrId, Distributor.id AS distributorId
		FROM SigmaL3Addr
            LEFT JOIN NEAX61SigmaELUs ON NEAX61SigmaELUs.id = SigmaL3Addr.eluId
            LEFT JOIN Distributor ON Distributor.sigmal3addrId = SigmaL3Addr.id
                WHERE SigmaL3Addr.l3addr = l3addr
                    AND NEAX61SigmaELUs.name = name;

END$$

DELIMITER ;

