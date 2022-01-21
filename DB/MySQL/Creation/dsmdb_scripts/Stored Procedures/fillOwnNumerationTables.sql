USE `ac1`;
DROP procedure IF EXISTS `fillOwnNumerationTables`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `fillOwnNumerationTables`()
BEGIN
	DECLARE done INT DEFAULT FALSE;
	DECLARE feature VARCHAR(45);
    DECLARE ownOfficeCodeId INT;
	DECLARE ownNumberChars VARCHAR(4);
	DECLARE downLimit INT;
    DECLARE upLimit INT;
    DECLARE ownNumberDigits INT;
    
    DECLARE featuresCursor CURSOR FOR SELECT value 
									FROM Features
										WHERE name LIKE 'countryCode.areaCode.officeCode%';
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	IF NOT EXISTS (SELECT id FROM OwnNumeration) THEN
    
		OPEN featuresCursor;
    
		read_loop: LOOP
			FETCH featuresCursor INTO feature;
			IF done THEN
				LEAVE read_loop;
			END IF;
    
			SELECT id INTO ownOfficeCodeId
				FROM OfficeCodes
					WHERE code = (SELECT SPLIT_STR(feature, '.', 3))
						AND areaId = (SELECT id 
										FROM Areas 
											WHERE code = (SELECT SPLIT_STR(feature, '.', 2))
												AND countryId = (SELECT id 
																	FROM Countries
																		WHERE code = (SELECT SPLIT_STR(feature, '.', 1)) ));
                                                                                
			SELECT SPLIT_STR(feature, '.', 4) INTO ownNumberChars;
        
			INSERT INTO OwnNumeration(officeCodeId, number)
				VALUES(ownOfficeCodeId, ownNumberChars);
    
			CASE (SELECT CHAR_LENGTH(ownNumberChars))
				WHEN 0 THEN
					SET downLimit = 0;
					SET upLimit = 9999;
				WHEN 1 THEN
					SET ownNumberDigits = (SELECT CAST(ownNumberChars AS UNSIGNED));
					SET downLimit = ownNumberDigits * 1000;
					SET upLimit = downLimit + 999;
				WHEN 2 THEN
					SET ownNumberDigits = (SELECT CAST(ownNumberChars AS UNSIGNED));
					SET downLimit = ownNumberDigits * 100;
					SET upLimit = downLimit + 99;
				WHEN 3 THEN
					SET ownNumberDigits = (SELECT CAST(ownNumberChars AS UNSIGNED));
					SET downLimit = ownNumberDigits * 10;
					SET upLimit = downLimit + 9;
			END CASE;

			WHILE (downLimit <= upLimit) DO
				INSERT INTO OwnPhoneNumbers(officeCodeId, number)
					VALUES(ownOfficeCodeId, LPAD(downLimit,4,'0'));
				SET downLimit = downLimit + 1;
			END WHILE;

		END LOOP;
    
		CLOSE featuresCursor;
    END IF;
END$$

DELIMITER ;
