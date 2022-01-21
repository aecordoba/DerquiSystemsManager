USE `dsmv4`;
DROP procedure IF EXISTS `modifyServiceOrder`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `modifyServiceOrder`(
IN serviceOrderId INT,
IN repairingTypeId INT,
IN remarks VARCHAR(250),
IN contact VARCHAR(45),
IN repairmanId INT,
IN repaired BOOLEAN,
IN repairmanRemarks VARCHAR(100),
IN RepairingCheckRemarks VARCHAR(100),
IN userId INT,
IN approved BOOLEAN
)
BEGIN
	DECLARE assignedRepairmanId INT DEFAULT NULL;
	DECLARE currentRepairingId INT DEFAULT NULL;
    
	UPDATE ServiceOrders
		SET repairingTypeId = repairingTypeId,
			remarks = remarks,
            contact = contact
				WHERE id = serviceOrderId;

	SELECT Repairings.repairmanId INTO assignedRepairmanId
		FROM Repairings
			WHERE Repairings.serviceOrderId = serviceOrderId 
				AND Repairings.assignmentTime = (SELECT MAX(assignmentTime)
													FROM Repairings
														WHERE Repairings.serviceOrderId = serviceOrderId);
    
	IF assignedRepairmanId IS NULL OR (repairmanId != assignedRepairmanId) THEN
		INSERT INTO Repairings(serviceOrderId, repairmanId)
			VALUES(serviceOrderId, repairmanId);
    END IF;
    
    SELECT id INTO currentRepairingId
		FROM Repairings
			WHERE Repairings.serviceOrderId = serviceOrderId 
				AND Repairings.assignmentTime = (SELECT MAX(assignmentTime)
													FROM Repairings
														WHERE Repairings.serviceOrderId = serviceOrderId);

	IF(repaired = TRUE) THEN
		UPDATE Repairings
			SET repairedDate = CURDATE()
					WHERE id = currentRepairingId;
    END IF;

	IF(repairmanRemarks IS NOT NULL) THEN
		UPDATE Repairings
			SET Repairings.repairmanRemarks = repairmanRemarks
					WHERE id = currentRepairingId;
    END IF;
    
    IF(userId != 0) THEN
		INSERT INTO RepairingChecks (repairingId, userId, remarks, approved)
			VALUES(currentRepairingId, userId, repairingCheckRemarks, approved);
            
		IF(approved = FALSE) THEN
			INSERT INTO Repairings(serviceOrderId, repairmanId)
				VALUES(serviceOrderId, repairmanId);

			IF(repairmanRemarks IS NOT NULL) THEN
				UPDATE Repairings
					SET Repairings.repairmanRemarks = repairmanRemarks
						WHERE id = (SELECT LAST_INSERT_ID());
			END IF;
        END IF;
    END IF;
END$$

DELIMITER ;
