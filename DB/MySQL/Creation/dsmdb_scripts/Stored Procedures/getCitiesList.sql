USE `ac1`;
DROP procedure IF EXISTS `getCitiesList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getCitiesList` ()
BEGIN
	SELECT id, name
		FROM Cities
			ORDER BY name;
END
$$

DELIMITER ; 
