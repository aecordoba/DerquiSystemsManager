USE `ac1`;
DROP procedure IF EXISTS `updateDSLAM`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateDSLAM`(
IN dslamId INT,
IN siteId INT,
IN name VARCHAR(10),
IN ipAddress VARCHAR(39),
IN routerId INT,
IN modelId INT,
IN remarks VARCHAR(100))
BEGIN
	DECLARE dslamModelPorts INT DEFAULT 0;
    DECLARE port INT DEFAULT 0;

    SELECT (DSLAMsModels.ports + 1) INTO port
		FROM DSLAMsModels
			WHERE DSLAMsModels.id = (SELECT DSLAMs.modelId
										FROM DSLAMs
											WHERE id = dslamId);
	
	UPDATE DSLAMs
		SET siteId = siteId,
        name = name,
        IPAddress = ipAddress,
        routerId = routerId,
        modelId = modelId,
        remarks = remarks
			WHERE id = dslamId;
    
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
