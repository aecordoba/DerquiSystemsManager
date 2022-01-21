USE `ac1`;
DROP procedure IF EXISTS `getSitesList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSitesList`()
BEGIN
	CREATE TEMPORARY TABLE TempSites (
    siteId INT PRIMARY KEY NOT NULL,
    siteName VARCHAR(20),
    siteCode VARCHAR(3),
    NEAX61E BOOLEAN DEFAULT 0,
    NEAX61SIGMA BOOLEAN DEFAULT 0,
    NEAX61SIGMAELU BOOLEAN DEFAULT 0,
    ZHONE BOOLEAN DEFAULT 0) ENGINE = MEMORY;
    
    INSERT INTO TempSites(siteId, siteName, siteCode)
		SELECT id, name, code
			FROM Sites
				ORDER BY code;
                
	UPDATE TempSites
		SET NEAX61E = true
			WHERE siteId IN (SELECT DISTINCT(siteId)
								FROM EFrames);

	UPDATE TempSites
		SET NEAX61SIGMA = true
			WHERE siteId IN (SELECT DISTINCT(siteId)
								FROM SigmaFrames);
                                
	UPDATE TempSites
		SET NEAX61SIGMAELU = true
			WHERE siteId IN (SELECT DISTINCT(siteId)
								FROM NEAX61SigmaELUs);

	UPDATE TempSites
		SET ZHONE = true
			WHERE siteId IN (SELECT DISTINCT(siteId)
								FROM Zhone);
    
	SELECT *
		FROM TempSites;
        
	DROP TEMPORARY TABLE TempSites;
END$$

DELIMITER ;
