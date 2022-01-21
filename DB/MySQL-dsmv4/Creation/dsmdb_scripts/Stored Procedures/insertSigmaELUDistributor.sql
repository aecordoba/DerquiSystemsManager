USE `dsmv4`;
DROP procedure IF EXISTS `insertSigmaELUDistributor`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertSigmaELUDistributor`(
IN switchBlockName VARCHAR(4),
IN siteId INT,
IN description VARCHAR(100),
IN positions INT,
IN timeSwitch CHAR(2),
IN kHighway CHAR(2),
IN pHighway CHAR(2),
IN eluName VARCHAR(8),
IN initialDTI INT,
IN finalDTI INT
)
BEGIN
	DECLARE switchBlockId INT DEFAULT NULL;
    DECLARE blockOrder INT DEFAULT 1;
    DECLARE position VARCHAR(4) DEFAULT NULL;
    DECLARE neax61sigmaELUId INT DEFAULT NULL;
    DECLARE l3AddrId INT DEFAULT NULL;
    DECLARE l3Addr INT DEFAULT 0;
    
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

	INSERT INTO NEAX61SigmaELUs(name, timeSwitch, kHighway, pHighway, siteId)
		VALUES(eluName, timeSwitch, kHighway, pHighway, siteId);
			
	SELECT LAST_INSERT_ID() INTO neax61sigmaELUId;
    
    WHILE (initialDTI <= finalDTI) DO
		INSERT INTO SigmaDTIs(eluId, dti)
			VALUES(neax61sigmaELUId, initialDTI);
		SET initialDTI = initialDTI + 1;
	END WHILE;

    WHILE (l3Addr <= 119) DO
			
		INSERT INTO SigmaL3Addr(eluId, l3addr)
			VALUES(neax61sigmaELUId, l3Addr);
		SELECT LAST_INSERT_ID() INTO l3AddrId;
			
		INSERT INTO Distributor(blockPositionId, sigmal3addrId, available)
				VALUES((SELECT id FROM BlockPositions WHERE BlockPositions.switchBlockId = switchBlockId AND BlockPositions.`order` = blockOrder), l3AddrId, true);
			
		SET blockOrder = blockOrder + 1;
        SET l3Addr = l3Addr + 1;
    END WHILE;
END$$

DELIMITER ;
