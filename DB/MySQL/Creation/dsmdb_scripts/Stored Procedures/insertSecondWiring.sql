USE `ac1`;
DROP procedure IF EXISTS `insertSecondWiring`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertSecondWiring`(
IN distributorId INT,
IN secondStreetPairId INT,
IN userId INT)
BEGIN
	DECLARE broadbandRecordId INT DEFAULT NULL;

	UPDATE Wiring
		SET secondStreetPairId = secondStreetPairId
			WHERE Wiring.distributorId = distributorId;

	SELECT broadbandRecordId INTO broadbandRecordId
		FROM WiringRecord
			WHERE WiringRecord.distributorId = distributorId
				AND time = (SELECT MAX(time)
								FROM WiringRecord
									WHERE WiringRecord.distributorId = distributorId);

    INSERT INTO WiringRecord(distributorId, streetPairId, secondStreetPairId, broadbandRecordId, remarks, userId)
		VALUES(distributorId, 
			(SELECT streetPairId FROM Wiring WHERE Wiring.distributorId = distributorId),
            secondStreetPairId,
            broadbandRecordId,
            (SELECT remarks FROM Wiring WHERE Wiring.distributorId = distributorId), 
            userId);

END$$

DELIMITER ;
