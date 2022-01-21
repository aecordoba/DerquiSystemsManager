USE `dsmv4`;
DROP procedure IF EXISTS `insertArea`;

DELIMITER $$
USE `dsmv4`$$
CREATE PROCEDURE `insertArea` (
IN countryCode INT,
IN countryName VARCHAR(45),
IN areaCode INT,
IN areaName VARCHAR(45))
BEGIN
	DECLARE countryId INT DEFAULT NULL;
    
    SELECT id INTO countryId
		FROM Countries
			WHERE code = countryCode;
	
    IF countryId IS NULL THEN
		INSERT INTO Countries (code, name)
			VALUES(countryCode, countryName);
		SELECT LAST_INSERT_ID() INTO countryId;
    END IF;
    
    INSERT INTO Areas(code, name, countryId)
		VALUES(areaCode, areaName, countryId);

END$$

DELIMITER ; 
