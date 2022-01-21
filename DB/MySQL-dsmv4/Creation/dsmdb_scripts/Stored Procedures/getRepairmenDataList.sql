USE `dsmv4`;
DROP procedure IF EXISTS `getRepairmenDataList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRepairmenDataList`()
BEGIN
	SELECT Repairmen.id AS repairmanId, Repairmen.personId, Repairmen.repairsCompanyId, Repairmen.services,
		People.firstName, People.middleName, People.lastName,
        RepairsCompanies.name AS repairsCompanyName
			FROM Repairmen
				LEFT JOIN People ON People.id = Repairmen.personId
                LEFT JOIN RepairsCompanies ON RepairsCompanies.id = Repairmen.repairsCompanyId;
END$$

DELIMITER ;
