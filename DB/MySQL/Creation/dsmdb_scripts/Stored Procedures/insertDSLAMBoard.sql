USE `ac1`;
DROP procedure IF EXISTS `insertDSLAMBoard`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertDSLAMBoard`(
IN dslamId INT,
IN modelId INT,
IN slot INT,
IN remarks VARCHAR(100))
BEGIN
	DECLARE dslamBoardId INT;
	DECLARE dslamBoardModelPorts INT DEFAULT 0;
    DECLARE port INT DEFAULT 1;

	INSERT INTO DSLAMsBoards(dslamId, modelId,  slot, remarks)
		VALUES(dslamId, modelId, slot, remarks);
	
    SELECT LAST_INSERT_ID() INTO dslamBoardId;
    
    SELECT DSLAMsBoardsModels.ports INTO dslamBoardModelPorts
		FROM DSLAMsBoardsModels
			WHERE DSLAMsBoardsModels.id = modelId;
	
    WHILE port <= dslamBoardModelPorts DO
		INSERT INTO BroadbandPorts(boardId, port, available)
			VALUES(dslamBoardId, port, true);
		SET port = port + 1;
    END WHILE;
END$$

DELIMITER ;
