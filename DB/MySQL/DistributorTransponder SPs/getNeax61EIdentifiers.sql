USE `ac1`;
DROP procedure IF EXISTS `getNeax61EIdentifiers`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getNeax61EIdentifiers`(
IN spce CHAR(2),
IN highway CHAR(1),
IN subhighway CHAR(1),
IN gr CHAR(2),
IN switch CHAR(1),
IN level CHAR(1)
)
BEGIN
	SELECT NEAX61E.id AS neax61eId, Distributor.id AS distributorId
		FROM NEAX61E
			LEFT JOIN Distributor ON Distributor.neax61eId = NEAX61E.id
				WHERE NEAX61E.spce = spce
					AND NEAX61E.highway = highway
					AND NEAX61E.subhighway = subhighway
					AND NEAX61E.group = gr
					AND NEAX61E.switch = switch
					AND NEAX61E.level = level;

END$$

DELIMITER ;

