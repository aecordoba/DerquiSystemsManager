USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberBroadbandStatesList`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `getSubscriberBroadbandStatesList` ()
BEGIN
	SELECT id, name, description
			FROM SubscriberBroadbandStates
				ORDER BY name;
END
$$

DELIMITER ; 
