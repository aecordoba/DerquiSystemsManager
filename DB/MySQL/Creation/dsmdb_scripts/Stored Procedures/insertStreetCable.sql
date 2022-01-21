USE `ac1`;
DROP procedure IF EXISTS `insertStreetCable`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertStreetCable`(
IN name CHAR,
IN frameId INT,
IN pairs INT,
IN description VARCHAR(100))
BEGIN
	DECLARE cableId INT DEFAULT NULL;
    DECLARE pair INT DEFAULT 1;
    
	INSERT INTO StreetCables(name, frameId, pairs, description)
		VALUES(name, frameId, pairs, description);
        
	SELECT LAST_INSERT_ID() INTO cableId;
    
    WHILE pair <= pairs DO
		INSERT INTO StreetPairs(cableId, pair)
			VALUES(cableId, pair);
		SET pair = pair + 1;
	END WHILE;
END$$

DELIMITER ;
