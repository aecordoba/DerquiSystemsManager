USE `dsmv4`;
DROP procedure IF EXISTS `getTime`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getTime` ()
BEGIN
	SELECT time
		FROM Users
			WHERE id = 1;
END$$

DELIMITER ;
 
