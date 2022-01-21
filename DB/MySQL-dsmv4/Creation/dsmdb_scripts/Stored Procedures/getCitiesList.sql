USE `dsmv4`;
DROP procedure IF EXISTS `getCitiesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getCitiesList` ()
BEGIN
	SELECT id, name
		FROM Cities
			ORDER BY name;
END
$$

DELIMITER ; 
