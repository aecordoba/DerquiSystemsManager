USE `dsmv4`;
DROP procedure IF EXISTS `updateDSLAMBoard`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateDSLAMBoard`(
IN dslamBoardId INT,
IN dslamId INT,
IN modelId INT,
IN slot INT,
IN ports INT,
IN remarks VARCHAR(100))
BEGIN
    DECLARE port INT DEFAULT 1;

    IF (modelId = 0) THEN
		SET modelId = NULL;
        SET slot = NULL;
	END IF;

	IF(dslamBoardId = 0) THEN
		CALL insertDSLAMBoard(dslamId, modelId, slot, ports, remarks);
	ELSE
		SELECT (DSLAMsBoardsModels.ports + 1) INTO port
			FROM DSLAMsBoardsModels
				WHERE DSLAMsBoardsModels.id = (SELECT DSLAMsBoards.modelId
													FROM DSLAMsBoards
														WHERE id = dslamBoardId);
            
		UPDATE DSLAMsBoards
			SET dslamId = dslamId,
				modelId = modelId,
                slot = slot,
                remarks = remarks
					WHERE id = dslamBoardId;

		WHILE port <= ports DO
			INSERT INTO BroadbandPorts(boardId, port, available)
				VALUES(dslamBoardId, port, true);
			SET port = port + 1;
		END WHILE;

	END IF;
	
END$$

DELIMITER ;
