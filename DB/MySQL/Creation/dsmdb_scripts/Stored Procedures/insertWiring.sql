USE `ac1`;
DROP procedure IF EXISTS `insertWiring`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertWiring`(
IN distributorId INT,
IN streetPairId INT,
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

	IF username IS NOT NULL THEN
		IF broadbandPortId = 0 THEN
			SET broadbandPortId = NULL;
		END IF;

		IF modemModelId = 0 THEN
			SET modemModelId = NULL;
		END IF;
        
        INSERT INTO Broadband(username, password, portId, modemId)
			VALUES(username, password, broadbandPortId, modemModelId);
		
        SELECT LAST_INSERT_ID() INTO broadbandId;

        INSERT INTO BroadbandRecord(username, password, portId, modemId, userId)
			VALUES(username, password, broadbandPortId, modemModelId, userId);
        
        SELECT LAST_INSERT_ID() INTO broadbandRecordId;
        
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

    END IF;
    
    INSERT INTO Wiring(distributorId, streetPairId, broadbandId, remarks)
		VALUES(distributorId, streetPairId, broadbandId, remarks);

    INSERT INTO WiringRecord(distributorId, streetPairId, broadbandRecordId, remarks, userId)
		VALUES(distributorId, streetPairId, broadbandRecordId, remarks, userId);

END$$

DELIMITER ;
