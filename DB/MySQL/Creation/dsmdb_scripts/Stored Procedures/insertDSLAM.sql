USE `ac1`;
DROP procedure IF EXISTS `insertDSLAM`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertDSLAM`(
IN siteId INT,
IN name VARCHAR(10),
IN ipAddress VARCHAR(39),
IN routerId INT,
IN modelId INT,
IN remarks VARCHAR(100),
OUT dslamId INT)
BEGIN
	DECLARE dslamModelPorts INT DEFAULT 0;
    DECLARE port INT DEFAULT 1;

	INSERT INTO DSLAMs(siteId, name,  IPAddress, routerId, modelId, remarks)
		VALUES(siteId, name, ipAddress, routerId, modelId, remarks);
	
    SELECT LAST_INSERT_ID() INTO dslamId;
    
    SELECT DSLAMsModels.ports INTO dslamModelPorts
		FROM DSLAMsModels
			WHERE DSLAMsModels.id = modelId;
	
    WHILE port <= dslamModelPorts DO
		INSERT INTO BroadbandPorts(dslamId, port, available)
			VALUES(dslamId, port, true);
		SET port = port + 1;
    END WHILE;
END$$

DELIMITER ;
