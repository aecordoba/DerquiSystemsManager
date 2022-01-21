USE `ac1`;
DROP procedure IF EXISTS `getVacantZhoneEquipmentList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getVacantZhoneEquipmentList`(
IN siteId INT)
BEGIN
    SELECT Zhone.id AS zhoneId, cable AS zhoneCable, port AS zhonePort, siteId AS zhoneSiteId
        FROM Zhone
            LEFT JOIN Distributor ON Distributor.zhoneId = Zhone.id
            WHERE Zhone.siteId = siteId
                AND Distributor.available = TRUE
                AND Distributor.id NOT IN (SELECT distributorId
                                                FROM Subscribers);
END$$

DELIMITER ;
