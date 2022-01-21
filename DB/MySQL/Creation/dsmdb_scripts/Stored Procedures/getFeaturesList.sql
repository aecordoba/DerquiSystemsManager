USE `ac1`;
DROP procedure IF EXISTS `getFeaturesList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getFeaturesList` ()
BEGIN
	SELECT id, name, value
		FROM Features;
END
$$

DELIMITER ;
 
