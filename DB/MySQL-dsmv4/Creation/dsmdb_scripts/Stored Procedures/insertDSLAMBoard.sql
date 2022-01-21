USE `dsmv4`;
DROP procedure IF EXISTS `insertDSLAMBoard`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertDSLAMBoard`(
IN dslamId INT,
IN modelId INT,
IN slot INT,
IN ports INT,
IN remarks VARCHAR(100))
BEGIN
	DECLARE dslamBoardId INT;
    DECLARE port INT DEFAULT 1;
    
    IF (modelId = 0) THEN
		SET modelId = NULL;
        SET slot = NULL;
	END IF;

	INSERT INTO DSLAMsBoards(dslamId, modelId,  slot, remarks)
		VALUES(dslamId, modelId, slot, remarks);
	
    SELECT LAST_INSERT_ID() INTO dslamBoardId;
	
    WHILE port <= ports DO
		INSERT INTO BroadbandPorts(boardId, port, available)
			VALUES(dslamBoardId, port, true);
		SET port = port + 1;
    END WHILE;
END$$

DELIMITER ;
