USE `ac1`;
DROP procedure IF EXISTS `getSubscriberStatesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberStatesList`()
BEGIN
	SELECT id, name, description
			FROM SubscriberStates
				ORDER BY name;
END$$

DELIMITER ;
