USE `ac1`;
DROP procedure IF EXISTS `insertStreet`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `insertStreet` (
IN streetId INT,
IN name VARCHAR(25))
BEGIN
	INSERT INTO Streets(id, name)
		VALUES(streetId, name);
END$$

DELIMITER ;
 
