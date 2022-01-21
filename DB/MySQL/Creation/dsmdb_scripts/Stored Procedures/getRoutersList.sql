USE `ac1`;
DROP procedure IF EXISTS `getRoutersList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRoutersList`()
BEGIN
	SELECT Routers.id AS routerId, Routers.name AS routerName, Routers.ipAddress AS routerIpAddress, Routers.modelId AS routerModelId,
		RoutersModels.name AS routerModelName, RoutersModels.ports AS routerModelPorts, RoutersModels.manufacturerId AS routerManufacturerId,
        RoutersManufacturers.name AS routerManufacturerName
			FROM Routers
				LEFT JOIN RoutersModels ON RoutersModels.id = Routers.modelId
				LEFT JOIN RoutersManufacturers ON RoutersManufacturers.id = RoutersModels.manufacturerId
					ORDER BY routerName;
END$$

DELIMITER ;
