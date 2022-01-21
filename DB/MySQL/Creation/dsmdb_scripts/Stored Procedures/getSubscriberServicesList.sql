USE `ac1`;
DROP procedure IF EXISTS `getSubscriberServicesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberServicesList`()
BEGIN
	SELECT id, name, description
		FROM SubscriberServices;
END$$

DELIMITER ;
 
