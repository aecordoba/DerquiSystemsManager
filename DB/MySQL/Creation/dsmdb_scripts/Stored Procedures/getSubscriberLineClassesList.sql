USE `ac1`;
DROP procedure IF EXISTS `getSubscriberLineClassesList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getSubscriberLineClassesList` ()
BEGIN
	SELECT id, type, description
			FROM SubscriberLineClasses
				ORDER BY type;
END
$$

DELIMITER ;
 
