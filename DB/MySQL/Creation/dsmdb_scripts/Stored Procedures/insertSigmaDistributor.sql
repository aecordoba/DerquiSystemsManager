USE `ac1`;
DROP procedure IF EXISTS `insertSigmaDistributor`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertSigmaDistributor`(
IN switchBlockName VARCHAR(4),
IN siteId INT,
IN description VARCHAR(100),
IN positions INT,
IN timeSwitch CHAR(2),
IN kHighway CHAR(2),
IN pHighway CHAR(2),
IN firstRow CHAR(1),
IN lastRow CHAR(1),
IN lineModuleName VARCHAR(2),
IN frameName VARCHAR(15)
)
BEGIN
	DECLARE switchBlockId INT DEFAULT NULL;
    DECLARE blockOrder INT DEFAULT 1;
    DECLARE position VARCHAR(4) DEFAULT NULL;
    DECLARE frameId INT DEFAULT NULL;
    DECLARE lineModuleId INT DEFAULT NULL;
    DECLARE currentRow INT DEFAULT firstRow;
    DECLARE currentColumn INT DEFAULT 0;
    DECLARE neax61sId INT DEFAULT NULL;
    
    SELECT id INTO switchBlockId FROM SwitchBlocks WHERE name = switchBlockName AND SwitchBlocks.siteId = siteId;
    
    IF switchBlockId IS NULL THEN
		INSERT INTO SwitchBlocks(name, siteId, description, positions)
			VALUES(switchBlockName, siteId, description, positions);
		
        SELECT LAST_INSERT_ID() INTO switchBlockId;
        
        
		WHILE (blockOrder <= positions) DO
			SELECT LPAD(((blockOrder - 1) DIV 4), 2, '0') INTO position;
			CASE ((blockOrder - 1) % 4)
				WHEN 0 THEN SET position = CONCAT(position, 'AB');
				WHEN 1 THEN SET position = CONCAT(position, 'CD');
				WHEN 2 THEN SET position = CONCAT(position, 'EF');
				WHEN 3 THEN SET position = CONCAT(position, 'GH');
            END CASE;
            
            INSERT INTO BlockPositions(`order`, position, switchBlockId)
				VALUES(blockOrder, position,switchBlockId);
            
			SET blockOrder = blockOrder + 1;
		END WHILE;
    END IF;
    
    
    SELECT id INTO frameId FROM SigmaFrames WHERE name = frameName AND SigmaFrames.siteId = siteId;
    
    IF frameId IS NULL THEN
		INSERT INTO SigmaFrames(name, siteId)
			VALUES(frameName, siteId);
            
		SELECT LAST_INSERT_ID() INTO frameId;
	END IF;
    
    
    SELECT id INTO lineModuleId FROM SigmaLineModules WHERE name = lineModuleName and SigmaLineModules.frameId = frameId;
		
    IF lineModuleId IS NULL THEN
		INSERT INTO SigmaLineModules(name, frameId)
			VALUES(lineModuleName, frameId);
		
        SELECT LAST_INSERT_ID() INTO lineModuleId;
	END IF;
    
    SELECT MIN(`order`) INTO blockOrder FROM BlockPositions
	WHERE BlockPositions.switchBlockId = switchBlockId
		AND id NOT IN (SELECT blockPositionId FROM Distributor);
        
    WHILE (currentRow <= lastRow) DO
        SET currentColumn = 0;
		WHILE (currentColumn <= 31) DO
			
			INSERT INTO NEAX61Sigma(timeSwitch, kHighway, pHighway, `row`, `column`, lineModuleId)
				VALUES(timeSwitch, kHighway, pHighway, currentRow, (SELECT LPAD(currentColumn, 2, '0')), lineModuleId);
			
            SELECT LAST_INSERT_ID() INTO neax61sId;
			
            INSERT INTO Distributor(blockPositionId, neax61sigmaId, available)
				VALUES((SELECT id FROM BlockPositions WHERE BlockPositions.switchBlockId = switchBlockId AND BlockPositions.`order` = blockOrder), neax61sId, true);
			
            SET currentColumn = currentColumn + 1;
            SET blockOrder = blockOrder + 1;
		END WHILE;
        SET currentRow = currentRow + 1;
    END WHILE;
END$$

DELIMITER ;
