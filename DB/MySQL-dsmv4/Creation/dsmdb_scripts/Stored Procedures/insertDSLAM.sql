USE `dsmv4`;
DROP procedure IF EXISTS `insertDSLAM`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertDSLAM`(
IN siteId INT,
IN name VARCHAR(10),
IN ipAddress VARCHAR(39),
IN routerId INT,
IN modelId INT,
IN remarks VARCHAR(100),
OUT dslamId INT)
BEGIN
	INSERT INTO DSLAMs(siteId, name,  IPAddress, routerId, modelId, remarks)
		VALUES(siteId, name, ipAddress, routerId, modelId, remarks);
	
    SELECT LAST_INSERT_ID() INTO dslamId;
END$$

DELIMITER ;
