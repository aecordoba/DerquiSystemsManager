USE `dsmv4`;
DROP procedure IF EXISTS `getDSLAMsBoardsModelsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getDSLAMsBoardsModelsList`()
BEGIN
	SELECT DSLAMsBoardsModels.id AS dslamBoardModelId, DSLAMsBoardsModels.name AS dslamBoardModelName, DSLAMsBoardsModels.manufacturerId AS dslamBoardModelManufacturerId, DSLAMsBoardsModels.ports AS dslamBoardModelPorts, DSLAMsBoardsModels.description AS dslamBoardModelDescription,
        BoardsManufacturers.name AS dslamBoardModelManufacturerName
			FROM DSLAMsBoardsModels
                LEFT JOIN DSLAMsManufacturers BoardsManufacturers ON BoardsManufacturers.id = DSLAMsBoardsModels.manufacturerId
					ORDER BY dslamBoardModelName;
END$$

DELIMITER ;
