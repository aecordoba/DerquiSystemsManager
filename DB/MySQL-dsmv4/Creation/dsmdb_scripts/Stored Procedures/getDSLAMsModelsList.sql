USE `dsmv4`;
DROP procedure IF EXISTS `getDSLAMsModelsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDSLAMsModelsList`()
BEGIN
	SELECT DSLAMsModels.id AS dslamModelId, DSLAMsModels.name AS dslamModelName, DSLAMsModels.slots AS dslamModelSlots, DSLAMsModels.ports AS dslamModelPorts,
		DSLAMsManufacturers.id AS dslamManufacturerId, DSLAMsManufacturers.name AS dslamManufacturerName
		FROM DSLAMsModels
         LEFT JOIN DSLAMsManufacturers ON DSLAMsManufacturers.id = DSLAMsModels.manufacturerId
			ORDER BY dslamModelName;
END$$

DELIMITER ;
