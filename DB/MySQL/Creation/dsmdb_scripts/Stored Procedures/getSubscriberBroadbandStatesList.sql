USE `ac1`;
DROP procedure IF EXISTS `getSubscriberBroadbandStatesList`;

DELIMITER $$
USE `ac1`$$
CREATE PROCEDURE `getSubscriberBroadbandStatesList` ()
BEGIN
	SELECT id, name, description
			FROM SubscriberBroadbandStates
				ORDER BY name;
END
$$

DELIMITER ; 
