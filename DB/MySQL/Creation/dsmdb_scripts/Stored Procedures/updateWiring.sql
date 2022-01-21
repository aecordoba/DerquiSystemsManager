USE `ac1`;
DROP procedure IF EXISTS `updateWiring`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateWiring`(
IN wiringId INT,
IN distributorId INT,
IN streetPairId INT,
IN secondStreetPairId INT,
IN broadbandPortId INT,
IN username VARCHAR(20),
IN password VARCHAR(20),
IN modemModelId INT,
IN remarks VARCHAR(100),
IN userId INT)
BEGIN
	DECLARE broadbandId INT DEFAULT NULL;
	DECLARE broadbandRecordId INT DEFAULT NULL;
    DECLARE subscriberDataId INT DEFAULT NULL;

	SELECT Wiring.broadbandId INTO broadbandId
		FROM Wiring
			WHERE Wiring.id = wiringId;

	IF username IS NOT NULL THEN
 		
		IF broadbandPortId = 0 THEN
			SET broadbandPortId = NULL;
		END IF;

		IF modemModelId = 0 THEN
			SET modemModelId = NULL;
		END IF;
        
        IF broadbandId IS NULL THEN
        
			INSERT INTO Broadband(username, password, portId, modemId)
				VALUES(username, password, broadbandPortId, modemModelId);
		
			SELECT LAST_INSERT_ID() INTO broadbandId;

			SELECT dataId INTO subscriberDataId
				FROM Subscribers
					WHERE Subscribers.distributorId = distributorId;
                
			UPDATE SubscribersData
				SET broadbandStateId = (SELECT id FROM SubscriberBroadbandStates WHERE name = 'ENABLED')
					WHERE SubscribersData.id = subscriberDataId;
		
			INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, services, stateId, broadbandStateId, information, userId)
				VALUES(subscriberDataId, 
					(SELECT lineClassId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT restrictionId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT GROUP_CONCAT(SubscriberServices.name SEPARATOR '/') FROM SubscriberServices
							WHERE id IN (SELECT serviceId FROM SubscribersDataServices WHERE dataId = subscriberDataId)),
					(SELECT stateId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT broadbandStateId FROM SubscribersData WHERE id = subscriberDataId),
					(SELECT information FROM SubscribersData WHERE id = subscriberDataId),
					userId);
		ELSE
        
			UPDATE Broadband
				SET Broadband.username = username, 
                Broadband.password = password, 
                Broadband.portId = broadbandPortId, 
                Broadband.modemId = modemModelId
					WHERE Broadband.id = broadbandId;

        END IF;

		INSERT INTO BroadbandRecord(username, password, portId, modemId, userId)
			VALUES(username, password, broadbandPortId, modemModelId, userId);
        
		SELECT LAST_INSERT_ID() INTO broadbandRecordId;
	
    ELSE
    
		IF broadbandId IS NOT NULL THEN
			
            UPDATE Wiring
				SET broadbandId = NULL
					WHERE Wiring.id = wiringId;
                    
            DELETE FROM Broadband
				WHERE Broadband.id = broadbandId;
			
            SET broadbandId = NULL;
            
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
        
    END IF;
    
    UPDATE Wiring
		SET distributorId = distributorId,
			streetPairId = streetPairId,
            broadbandId = broadbandId,
            remarks = remarks
            WHERE id = wiringId;

	IF secondStreetPairId = 0 THEN
			SET secondStreetPairId = NULL;
	END IF;
	
    INSERT INTO WiringRecord(distributorId, streetPairId, secondStreetPairId, broadbandRecordId, remarks, userId)
		VALUES(distributorId, streetPairId, secondStreetPairId, broadbandRecordId, remarks, userId);

END$$

DELIMITER ;
