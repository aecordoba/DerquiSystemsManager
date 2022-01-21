USE `ac1`;
DROP procedure IF EXISTS `updateDSLAMBoard`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateDSLAMBoard`(
IN dslamBoardId INT,
IN dslamId INT,
IN modelId INT,
IN slot INT,
IN remarks VARCHAR(100))
BEGIN
	DECLARE dslamBoardModelPorts INT DEFAULT 0;
    DECLARE port INT DEFAULT 0;

	IF(dslamBoardId = 0) THEN
		CALL insertDSLAMBoard(dslamId, modelId, slot, remarks);
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
	
		SELECT DSLAMsBoardsModels.ports INTO dslamBoardModelPorts
			FROM DSLAMsBoardsModels
				WHERE DSLAMsBoardsModels.id =modelId;
	
		WHILE port <= dslamBoardModelPorts DO
			INSERT INTO BroadbandPorts(boardId, port, available)
				VALUES(dslamBoardId, port, true);
			SET port = port + 1;
		END WHILE;
	END IF;
END$$

DELIMITER ;
