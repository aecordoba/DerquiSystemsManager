INSERT INTO Roles(name, description)
	VALUES('broadband-observer', 'Broadband observer'); 

INSERT INTO UsersRoles (userId, roleId)
	VALUES((SELECT id FROM Users WHERE name = 'SMadmin'),
		(SELECT id FROM Roles WHERE name = 'broadband-observer'));

UPDATE `ac1`.`Features` SET `value`='3.2.0' WHERE `name`='dbVersion';

UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~3)' WHERE `id`='6';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~4)' WHERE `id`='7';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~5)' WHERE `id`='8';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~6)' WHERE `id`='9';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~7)' WHERE `id`='10';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)' WHERE `id`='11';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)' WHERE `id`='12';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)' WHERE `id`='13';
UPDATE `ac1`.`OriginationRestrictions` SET `description`='Emergencias+0800+Local+Pilar+BUE+BUE(celulares)+DDN(1~11)' WHERE `id`='23';
INSERT INTO `ac1`.`OriginationRestrictions` (`name`, `description`) VALUES ('37', 'Emergencias+0800+Local+Pilar+BUE+DDN(1~11)+DDI');
INSERT INTO `ac1`.`SubscriberRestrictions` (`originationRestrictionId`, `terminationRestrictionId`) VALUES ('24', '1');
INSERT INTO `ac1`.`SubscriberRestrictions` (`originationRestrictionId`, `terminationRestrictionId`) VALUES ('24', '2');
UPDATE `ac1`.`SubscriberBroadbandStates` SET `description`='Broadband debtor' WHERE `id`='2';
INSERT INTO `ac1`.`SubscriberBroadbandStates` (`name`, `description`) VALUES ('DISABLED', 'Broadband disabled');
