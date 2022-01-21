USE `ac1`;
DROP procedure IF EXISTS `insertUser`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertUser`(
IN username VARCHAR(45),
IN password VARCHAR(128),
IN personId INT,
IN roleId INT)
BEGIN
	DECLARE userId INT DEFAULT NULL;
    
    SELECT id INTO userId
		FROM Users
			WHERE Users.name = username AND Users.personId = personId;

	IF userId IS NULL THEN
		INSERT INTO Users(name, password, personId)
			VALUES(username, password, personId);
		SELECT LAST_INSERT_ID() INTO userId;
		INSERT INTO UsersRoles(userId, roleId)
			VALUES(userId, roleId);
	ELSE
		UPDATE Users
			SET password = password
				WHERE id = userId;
		INSERT INTO UsersRoles(userId, roleId)
			SELECT * FROM (SELECT userId, roleId) AS tmp
				WHERE NOT EXISTS (SELECT userId FROM UsersRoles WHERE UsersRoles.userId = userId AND UsersRoles.roleId = roleId) LIMIT 1;
		
    END IF;
END$$

DELIMITER ;
