USE `dsmv4`;
DROP procedure IF EXISTS `clearOwnNumerationTables`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `clearOwnNumerationTables`()
BEGIN
	TRUNCATE OwnNumeration;
    ALTER TABLE OwnNumeration AUTO_INCREMENT = 1;
	TRUNCATE OwnPhoneNumbers;
    ALTER TABLE OwnPhoneNumbers AUTO_INCREMENT = 1;
END$$

DELIMITER ;
 
