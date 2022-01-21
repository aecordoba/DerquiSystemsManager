USE `dsmv4`;
DROP procedure IF EXISTS `insertRouter`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `insertRouter` (
IN name VARCHAR(45),
IN IPAddress VARCHAR(39),
IN modelId INT)
BEGIN
	INSERT INTO Routers(name, IPAddress, modelId)
		VALUES(name, IPAddress, modelId);
END
$$

DELIMITER ;
