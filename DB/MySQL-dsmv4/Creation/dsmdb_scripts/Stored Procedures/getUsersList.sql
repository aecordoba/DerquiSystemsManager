USE `dsmv4`;
DROP procedure IF EXISTS `getUsersList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUsersList`()
BEGIN
	SELECT Users.id AS userId, Users.name AS username, Users.personId, UsersRoles.roleId,
		People.firstName, People.middleName, People.lastName
			FROM Users
				LEFT JOIN People ON People.id = Users.personId
                LEFT JOIN UsersRoles ON UsersRoles.userId = Users.id;
END$$

DELIMITER ;
