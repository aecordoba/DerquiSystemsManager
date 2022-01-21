USE `ac1`;
DROP procedure IF EXISTS `deleteWiring`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteWiring`(
IN wiringId INT,
IN remarks VARCHAR(100),
IN userId INT)
BEGIN
	DECLARE distributorId INT DEFAULT NULL;
	DECLARE broadbandId INT DEFAULT NULL;
    DECLARE subscriberDataId INT DEFAULT NULL;

	SELECT Wiring.distributorId INTO distributorId
		FROM Wiring
			WHERE Wiring.id = wiringId;
	
	SELECT Wiring.broadbandId INTO broadbandId
		FROM Wiring
			WHERE Wiring.id = wiringId;
    
    INSERT INTO WiringRecord(distributorId, remarks, userId)
		VALUES(distributorId, remarks, userId);
	
    DELETE FROM Wiring
		WHERE Wiring.id = wiringId;

    IF broadbandId IS NOT NULL THEN
		DELETE FROM Broadband
			WHERE Broadband.id = broadbandId;

			SELECT dataId INTO subscriberDataId
				FROM Subscribers
					WHERE Subscribers.distributorId = distributorId;
                
			UPDATE SubscribersData
				SET broadbandStateId = NULL
					WHERE SubscribersData.id = subscriberDataId;
		
			INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, services, stateId, broadbandStateId, information, userId)
				VALUES(subscriberDataId, 
					(SELECT lineClassId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT restrictionId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT GROUP_CONCAT(SubscriberServices.name SEPARATOR '/') FROM SubscriberServices
							WHERE id IN (SELECT serviceId FROM SubscribersDataServices WHERE dataId = subscriberDataId)),
					(SELECT stateId FROM SubscribersData WHERE id = subscriberDataId),
					NULL,
					(SELECT information FROM SubscribersData WHERE id = subscriberDataId),
					userId);

	END IF;
    
END$$

DELIMITER ;
