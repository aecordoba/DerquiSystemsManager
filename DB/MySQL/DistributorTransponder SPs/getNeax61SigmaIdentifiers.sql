USE `ac1`;
DROP procedure IF EXISTS `getNeax61SigmaIdentifiers`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getNeax61SigmaIdentifiers`(
IN timeSwitch CHAR(2),
IN kHighway CHAR(2),
IN pHighway CHAR(2),
IN row CHAR(1),
IN `column` CHAR(2)
)
BEGIN
	SELECT NEAX61Sigma.id AS neax61sigmaId, Distributor.id AS distributorId
		FROM NEAX61Sigma
			LEFT JOIN Distributor ON Distributor.neax61sigmaId = NEAX61Sigma.id
				WHERE NEAX61Sigma.timeSwitch = timeSwitch
					AND NEAX61Sigma.kHighway = kHighway
					AND NEAX61Sigma.pHighway = pHighway
					AND NEAX61Sigma.row = row
					AND NEAX61Sigma.column = `column`;

END$$

DELIMITER ;

