USE `ac1`;
DROP procedure IF EXISTS `getSwitchBlocksList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSwitchBlocksList`()
BEGIN
	SELECT SwitchBlocks.id, SwitchBlocks.name, SwitchBlocks.siteId, SwitchBlocks.description, SwitchBlocks.positions
		FROM SwitchBlocks
			LEFT JOIN Sites ON Sites.id = SwitchBlocks.siteId
			ORDER BY Sites.code, SwitchBlocks.name;
END$$

DELIMITER ; 
