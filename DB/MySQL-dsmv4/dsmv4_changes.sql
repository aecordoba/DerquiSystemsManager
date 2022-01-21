USE dsmv4;

ALTER TABLE DSLAMsBoards
	MODIFY COLUMN   `modelId` INT NULL;
ALTER TABLE DSLAMsBoards
	MODIFY COLUMN   `slot` INT NULL;


INSERT INTO `dsmv4`.`DSLAMsBoards` (`id`, `dslamId`) VALUES ('46', '1');
INSERT INTO `dsmv4`.`DSLAMsBoards` (`id`, `dslamId`) VALUES ('47', '2');
INSERT INTO `dsmv4`.`DSLAMsBoards` (`id`, `dslamId`) VALUES ('48', '3');
INSERT INTO `dsmv4`.`DSLAMsBoards` (`id`, `dslamId`) VALUES ('49', '8');

UPDATE BroadbandPorts
	SET boardId = 46
		WHERE dslamId = 1;
UPDATE BroadbandPorts
	SET boardId = 47
		WHERE dslamId = 2;
UPDATE BroadbandPorts
	SET boardId = 48
		WHERE dslamId = 3;
UPDATE BroadbandPorts
	SET boardId = 49
		WHERE dslamId = 8;

ALTER TABLE BroadbandPorts
DROP FOREIGN KEY fk_BroadbandPorts_DSLAMs;
ALTER TABLE BroadbandPorts
DROP COLUMN dslamId;

UPDATE `dsmv4`.`Features` SET `value`='4.0.0' WHERE `id`='1';
