USE `dsmv4`;
DROP procedure IF EXISTS `insertServiceOrder`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertServiceOrder`(
IN subscriberId INT,
IN repairingTypeId INT,
IN remarks VARCHAR(250),
IN contact VARCHAR(45),
IN repairmanId INT,
IN userId INT,
OUT serviceOrderId INT
)
BEGIN
	INSERT INTO ServiceOrders(subscriberId, repairingTypeId, remarks, contact, userId)
		VALUES(subscriberId, repairingTypeId, remarks, contact, userId);
	SELECT LAST_INSERT_ID() INTO serviceOrderId;
    
	IF(repairmanId > 0) THEN
		INSERT INTO Repairings(serviceOrderId, repairmanId)
			VALUES(serviceOrderId, repairmanId);
    END IF;
END$$

DELIMITER ;
