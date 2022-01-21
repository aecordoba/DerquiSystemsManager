USE `dsmv4`;
DROP procedure IF EXISTS `deleteSecondWiring`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteSecondWiring`(
IN wiringId INT,
IN userId INT)
BEGIN
	DECLARE broadbandRecordId INT DEFAULT NULL;

	UPDATE Wiring
		SET secondStreetPairId = null
			WHERE Wiring.id = wiringId;
            
	SELECT broadbandRecordId INTO broadbandRecordId
		FROM WiringRecord
			WHERE WiringRecord.distributorId = distributorId
				AND time = (SELECT MAX(time)
								FROM WiringRecord
									WHERE WiringRecord.distributorId = distributorId);

    INSERT INTO WiringRecord(distributorId, streetPairId, broadbandRecordId, remarks, userId)
		VALUES((SELECT distributorId FROM Wiring WHERE Wiring.id = wiringId),
			(SELECT streetPairId FROM Wiring WHERE Wiring.id = wiringId),
            broadbandRecordId,
            (SELECT remarks FROM Wiring WHERE Wiring.id = wiringId),
            userId);
    
END$$

DELIMITER ;
