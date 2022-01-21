USE `dsmv4`;
DROP procedure IF EXISTS `updateSubscriberData`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateSubscriberData`(
IN dataId INT,
IN lineClassId INT,
IN restrictionId INT,
IN stateId INT,
IN broadbandStateId INT,
IN information VARCHAR(250),
IN clip BOOLEAN,
IN clir BOOLEAN,
IN cf BOOLEAN,
IN cw BOOLEAN,
IN userId INT)
BEGIN

	IF(broadbandStateId = 0) THEN
		SET broadbandStateId = NULL;
    END IF;
    
	UPDATE SubscribersData
		SET lineClassId = lineClassId,
			restrictionId = restrictionId,
            stateId = stateId,
            broadbandStateId = broadbandStateId,
            information = information
				WHERE id = dataId;
                
	DELETE FROM SubscribersDataServices
		WHERE SubscribersDataServices.dataId = dataId;
        
	IF(clip = true) THEN
		INSERT INTO SubscribersDataServices(dataId, serviceId)
			VALUES(dataId, (SELECT id FROM SubscriberServices WHERE name = 'clip'));
	END IF;
	IF(clir = true) THEN
		INSERT INTO SubscribersDataServices(dataId, serviceId)
			VALUES(dataId, (SELECT id FROM SubscriberServices WHERE name = 'clir'));
	END IF;
	IF(cf = true) THEN
		INSERT INTO SubscribersDataServices(dataId, serviceId)
			VALUES(dataId, (SELECT id FROM SubscriberServices WHERE name = 'cf'));
	END IF;
	IF(cw = true) THEN
		INSERT INTO SubscribersDataServices(dataId, serviceId)
			VALUES(dataId, (SELECT id FROM SubscriberServices WHERE name = 'cw'));
	END IF;
    
	INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, services, stateId, broadbandStateId, information, userId)
		VALUES(dataId, lineClassId, restrictionId,
			(SELECT GROUP_CONCAT(SubscriberServices.name SEPARATOR '/') FROM SubscriberServices
					WHERE id IN (SELECT serviceId FROM SubscribersDataServices WHERE SubscribersDataServices.dataId = dataId)),
			stateId, broadbandStateId, information, userId);
    
END$$

DELIMITER ;
