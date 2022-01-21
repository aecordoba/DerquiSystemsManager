USE `dsmv4`;
DROP procedure IF EXISTS `updateDSLAM`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateDSLAM`(
IN dslamId INT,
IN siteId INT,
IN name VARCHAR(10),
IN ipAddress VARCHAR(39),
IN routerId INT,
IN modelId INT,
IN remarks VARCHAR(100))
BEGIN
	UPDATE DSLAMs
		SET siteId = siteId,
        name = name,
        IPAddress = ipAddress,
        routerId = routerId,
        modelId = modelId,
        remarks = remarks
			WHERE id = dslamId;
END$$

DELIMITER ;
