USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberServicesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberServicesList`()
BEGIN
	SELECT id, name, description
		FROM SubscriberServices;
END$$

DELIMITER ;
 
