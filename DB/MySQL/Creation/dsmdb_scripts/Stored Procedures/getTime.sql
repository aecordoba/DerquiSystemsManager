USE `ac1`;
DROP procedure IF EXISTS `getTime`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getTime` ()
BEGIN
	SELECT time
		FROM Users
			WHERE id = 1;
END$$

DELIMITER ;
 
