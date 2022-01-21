USE `ac1`;
DROP procedure IF EXISTS `getRoutersModelsList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRoutersModelsList`()
BEGIN
	SELECT RoutersModels.id As routerModelId, RoutersModels.name AS routerModelName, RoutersModels.ports AS routerModelPorts, RoutersModels.manufacturerId AS routerManufacturerId,
        RoutersManufacturers.name AS routerManufacturerName
			FROM RoutersModels
				LEFT JOIN RoutersManufacturers ON RoutersManufacturers.id = RoutersModels.manufacturerId
					ORDER BY routerModelName;
END$$

DELIMITER ;
