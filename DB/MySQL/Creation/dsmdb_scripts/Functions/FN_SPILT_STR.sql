 
USE `ac1`;
DROP function IF EXISTS `SPLIT_STR`;

DELIMITER $$
USE `ac1`$$
CREATE FUNCTION `SPLIT_STR` (
x VARCHAR(255),
delim VARCHAR(12),
pos INT)
RETURNS VARCHAR(255)
BEGIN
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');
END
$$

DELIMITER ;
