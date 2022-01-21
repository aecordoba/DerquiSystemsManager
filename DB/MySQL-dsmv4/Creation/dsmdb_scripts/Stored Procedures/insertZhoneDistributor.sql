USE `dsmv4`;
DROP procedure IF EXISTS `insertZhoneDistributor`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertZhoneDistributor`(
IN switchBlockName VARCHAR(4),
IN siteId INT,
IN description VARCHAR(100),
IN positions INT,
IN firstCable CHAR(2),
IN lastCable CHAR(2)
)
BEGIN
	DECLARE switchBlockId INT DEFAULT NULL;
    DECLARE blockOrder INT DEFAULT 1;
    DECLARE position VARCHAR(4) DEFAULT NULL;
    DECLARE currentCable INT DEFAULT firstCable;
    DECLARE currentPort INT DEFAULT 1;
    DECLARE zhoneId INT DEFAULT NULL;

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

    SELECT MIN(`order`) INTO blockOrder FROM BlockPositions
	WHERE BlockPositions.switchBlockId = switchBlockId
		AND id NOT IN (SELECT blockPositionId FROM Distributor);

    WHILE (currentCable <= lastCable) DO
        SET currentPort = 1;
		WHILE (currentPort <= 48) DO
			
			INSERT INTO Zhone(cable, port, siteId)
				VALUES((SELECT LPAD(currentCable, 2, '0')), (SELECT LPAD(currentPort, 2, '0')), siteId);
			
            SELECT LAST_INSERT_ID() INTO zhoneId;
			
            INSERT INTO Distributor(blockPositionId, zhoneId, available)
				VALUES((SELECT id FROM BlockPositions WHERE BlockPositions.switchBlockId = switchBlockId AND BlockPositions.`order` = blockOrder), zhoneId, true);
			
            SET currentPort = currentPort + 1;
            SET blockOrder = blockOrder + 1;
		END WHILE;
        SET currentCable = currentCable + 1;
    END WHILE;

END$$

DELIMITER ;
