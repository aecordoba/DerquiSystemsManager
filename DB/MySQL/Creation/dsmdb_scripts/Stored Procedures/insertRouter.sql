USE `ac1`;
DROP procedure IF EXISTS `insertRouter`;

DELIMITER $$
USE `ac1`$$
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
