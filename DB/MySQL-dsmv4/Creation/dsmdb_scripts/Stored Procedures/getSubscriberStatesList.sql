USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberStatesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberStatesList`()
BEGIN
	SELECT id, name, description
			FROM SubscriberStates
				ORDER BY name;
END$$

DELIMITER ;
