USE `ac1`;
DROP procedure IF EXISTS `getLocationsList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getLocationsList`()
BEGIN
	SELECT Locations.id, Locations.name , 
    Cities.id cityId, Cities.name cityName, 
    States.id stateId, States.name stateName, 
    Countries.id countryId, Countries.code countryCode, Countries.name countryName
		FROM Locations
			INNER JOIN Cities ON Cities.id = Locations.cityId
            INNER JOIN States ON States.id = Cities.stateId
            INNER JOIN Countries ON Countries.id = States.countryId
				ORDER BY Locations.name, cityName, stateName;
END$$

DELIMITER ;
