USE `dsmv4`;
DROP procedure IF EXISTS `getUser`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUser`(IN username VARCHAR(45),
			    IN password VARCHAR(128))
BEGIN
	UPDATE Users
		SET time = NOW()
			WHERE BINARY Users.name = username
						AND Users.password = password;
                        
	SELECT Users.id AS userId, UsersRoles.roleId,
		People.id AS personId, People.firstName, People.middleName, People.lastName
			FROM Users
				LEFT JOIN People ON People.id = Users.personId
                LEFT JOIN UsersRoles ON UsersRoles.userId = Users.id
					WHERE BINARY Users.name = username
						AND Users.password = password;
END$$

DELIMITER ;
