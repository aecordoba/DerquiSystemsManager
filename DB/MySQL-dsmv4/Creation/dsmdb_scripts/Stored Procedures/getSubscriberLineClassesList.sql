USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberLineClassesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getSubscriberLineClassesList` ()
BEGIN
	SELECT id, type, description
			FROM SubscriberLineClasses
				ORDER BY type;
END
$$

DELIMITER ;
 
