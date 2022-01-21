USE `dsmv4`;
DROP procedure IF EXISTS `getFeaturesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getFeaturesList` ()
BEGIN
	SELECT id, name, value
		FROM Features;
END
$$

DELIMITER ;
 
