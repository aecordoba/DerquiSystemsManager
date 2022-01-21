USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberRestrictionsList`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberRestrictionsList`()
BEGIN
	SELECT SubscriberRestrictions.id, terminationRestrictionId, originationRestrictionId,
		TerminationRestrictions.name AS terminationName, TerminationRestrictions.description AS terminationDescription,
        OriginationRestrictions.name AS originationName, OriginationRestrictions.description AS originationDescription
		FROM SubscriberRestrictions
			LEFT JOIN TerminationRestrictions ON TerminationRestrictions.id = terminationRestrictionId
			LEFT JOIN OriginationRestrictions ON OriginationRestrictions.id = originationRestrictionId
				ORDER BY TerminationRestrictions.name, OriginationRestrictions.name;
END$$

DELIMITER ;
