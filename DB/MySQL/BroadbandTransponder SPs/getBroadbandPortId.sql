USE `ac1`;
DROP procedure IF EXISTS `getBroadbandPortId`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getBroadbandPortId`(
IN dslamName VARCHAR(10),
IN slot INT,
IN port INT)
BEGIN
	IF(slot = 0) THEN
		SELECT BroadbandPorts.id
			FROM BroadbandPorts
				LEFT JOIN DSLAMs ON DSLAMs.id = BroadbandPorts.dslamId
					WHERE DSLAMs.name = dslamName AND BroadbandPorts.port = port;
	ELSE
		SELECT BroadbandPorts.id
			FROM BroadbandPorts
				LEFT JOIN DSLAMsBoards ON DSLAMsBoards.id = BroadbandPorts.boardId
				LEFT JOIN DSLAMs DSLAMBoardDslam ON DSLAMBoardDslam.id = DSLAMsBoards.dslamId
					WHERE DSLAMBoardDslam.name = dslamName AND DSLAMsBoards.slot = slot AND BroadbandPorts.port = port;
	END IF;

END$$

DELIMITER ;
