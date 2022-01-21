USE `dsmv4`;
DROP procedure IF EXISTS `insertEDistributor`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertEDistributor`(
IN switchBlockName VARCHAR(4),
IN siteId INT,
IN description VARCHAR(100),
IN positions INT,
IN spce CHAR(2),
IN highway CHAR(1),
IN subhighway CHAR(1),
IN firstGroup CHAR(2),
IN lastGroup CHAR(2),
IN lineModuleName VARCHAR(2),
IN frameName VARCHAR(15)
)
BEGIN
	DECLARE switchBlockId INT DEFAULT NULL;
    DECLARE blockOrder INT DEFAULT 1;
    DECLARE position VARCHAR(4) DEFAULT NULL;
    DECLARE frameId INT DEFAULT NULL;
    DECLARE lineModuleId INT DEFAULT NULL;
    DECLARE currentGroup INT DEFAULT firstGroup;
    DECLARE currentLevel INT DEFAULT 0;
    DECLARE currentSwitch INT DEFAULT 0;
    DECLARE neax61eId INT DEFAULT NULL;
    
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
    
    
    SELECT id INTO frameId FROM EFrames WHERE name = frameName AND EFrames.siteId = siteId;
    
    IF frameId IS NULL THEN
		INSERT INTO EFrames(name, siteId)
			VALUES(frameName, siteId);
            
		SELECT LAST_INSERT_ID() INTO frameId;
	END IF;
    
    
    SELECT id INTO lineModuleId FROM ELineModules WHERE name = lineModuleName and ELineModules.frameId = frameId;
		
    IF lineModuleId IS NULL THEN
		INSERT INTO ELineModules(name, frameId)
			VALUES(lineModuleName, frameId);
		
        SELECT LAST_INSERT_ID() INTO lineModuleId;
	END IF;
    
    SELECT MIN(`order`) INTO blockOrder
		FROM BlockPositions
			WHERE BlockPositions.switchBlockId = switchBlockId
				AND id NOT IN (SELECT blockPositionId FROM Distributor);
        
    WHILE (currentGroup <= lastGroup) DO
        SET currentLevel = 0;
		WHILE (currentLevel <= 3) DO
			SET currentSwitch = 0;
			WHILE (currentSwitch <= 7) DO
            
				INSERT INTO NEAX61E(spce, highway, subhighway, `group`, switch, level, lineModuleId)
					VALUES(spce, highway, subhighway, (SELECT LPAD(currentGroup, 2, '0')), currentSwitch, currentLevel, lineModuleId);
			
				SELECT LAST_INSERT_ID() INTO neax61eId;
			
				INSERT INTO Distributor(blockPositionId, neax61eId, available)
					VALUES((SELECT id FROM BlockPositions WHERE BlockPositions.switchBlockId = switchBlockId AND BlockPositions.`order` = blockOrder), neax61eId, true);
			
				SET currentSwitch = currentSwitch + 1;
				SET blockOrder = blockOrder + 1;
                
            END WHILE;
            SET currentLevel = currentLevel + 1;
		END WHILE;
        SET currentGroup = currentGroup + 1;
    END WHILE;
END$$

DELIMITER ;
