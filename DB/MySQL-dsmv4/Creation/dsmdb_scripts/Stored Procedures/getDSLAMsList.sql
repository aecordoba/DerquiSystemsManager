USE `dsmv4`;
DROP procedure IF EXISTS `getDSLAMsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDSLAMsList`()
BEGIN
	SELECT DSLAMs.id AS dslamId, DSLAMs.siteId AS dslamSiteId, DSLAMs.name AS dslamName, DSLAMs.ipAddress AS dslamIpAddress, DSLAMs.routerId AS routerId, DSLAMs.modelId AS dslamModelId, DSLAMs.remarks AS dslamRemarks,
		DSLAMsModels.name AS dslamModelName, DSLAMsModels.slots AS dslamModelSlots, DSLAMsModels.ports AS dslamModelPorts,
		DSLAMsManufacturers.id AS dslamManufacturerId, DSLAMsManufacturers.name AS dslamManufacturerName,
		DSLAMsBoards.id AS boardId, DSLAMsBoards.modelId AS boardModelId, DSLAMsBoards.slot AS boardSlot, DSLAMsBoards.remarks AS boardRemarks,
		DSLAMsBoardsModels.name AS boardModelName, DSLAMsBoardsModels.manufacturerId AS boardManufacturerId, DSLAMsBoardsModels.ports AS boardModelPorts, DSLAMsBoardsModels.description AS boardModelDescription,
        BoardsManufacturers.name AS boardManufacturerName,
        Sites.name AS siteName, Sites.code AS siteCode,
        Routers.name AS routerName, Routers.ipAddress AS routerIpAddress, Routers.modelId AS routerModelId,
		RoutersModels.name AS routerModelName, RoutersModels.ports AS routerModelPorts, RoutersModels.manufacturerId AS routerManufacturerId,
        RoutersManufacturers.name AS routerManufacturerName
			FROM DSLAMs
                LEFT JOIN DSLAMsBoards ON DSLAMsBoards.dslamId =  DSLAMs.id
				LEFT JOIN DSLAMsBoardsModels ON DSLAMsBoardsModels.id = DSLAMsBoards.modelId
                LEFT JOIN DSLAMsManufacturers BoardsManufacturers ON BoardsManufacturers.id = DSLAMsBoardsModels.manufacturerId
                LEFT JOIN Sites ON Sites.id = DSLAMs.siteId
                LEFT JOIN Routers ON Routers.id = DSLAMs.routerId
				LEFT JOIN RoutersModels ON RoutersModels.id = Routers.modelId
				LEFT JOIN RoutersManufacturers ON RoutersManufacturers.id = RoutersModels.manufacturerId
                LEFT JOIN DSLAMsModels ON DSLAMsModels.id = DSLAMs.modelId
                LEFT JOIN DSLAMsManufacturers ON DSLAMsManufacturers.id = DSLAMsModels.manufacturerId
					ORDER BY DSLAMs.name, DSLAMsBoards.slot;
END$$

DELIMITER ;
