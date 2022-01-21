USE `ac1`;
DROP procedure IF EXISTS `getSubscriberDataRecordList`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberDataRecordList`(
IN countryCode INT,
IN areaCode INT,
IN officeCode INT,
IN number CHAR(4)
)
BEGIN
		SELECT SubscriberLineClasses.type AS lineClassType, 
			TerminationRestrictions.name AS terminationRestrictionName, TerminationRestrictions.description AS terminationRestrictionDescription,
            OriginationRestrictions.name AS originationRestrictionName, OriginationRestrictions.description AS originationRestrictionDescription,
			SubscriberStates.name AS stateName, SubscriberBroadbandStates.name AS broadbandStateName,
            SubscribersDataRecord.services, SubscribersDataRecord.information,
			Users.name AS username, People.firstName, People.lastName,
			SubscribersDataRecord.time
				FROM SubscribersDataRecord
					LEFT JOIN Subscribers ON Subscribers.dataId = SubscribersDataRecord.dataId
					LEFT JOIN Addresses ON Addresses.id = Subscribers.addressId
                    LEFT JOIN PhoneNumbers ON PhoneNumbers.id = Addresses.phoneNumberId
                    LEFT JOIN OfficeCodes ON OfficeCodes.id = PhoneNumbers.officeCodeId
                    LEFT JOIN Areas ON Areas.id = OfficeCodes.areaId
                    LEFT JOIN Countries ON Countries.id = Areas.countryId
					LEFT JOIN SubscriberLineClasses ON SubscriberLineClasses.id = SubscribersDataRecord.lineClassId
					LEFT JOIN SubscriberRestrictions ON SubscriberRestrictions.id = SubscribersDataRecord.restrictionId
                    LEFT JOIN TerminationRestrictions ON TerminationRestrictions.id = SubscriberRestrictions.terminationRestrictionId
                    LEFT JOIN OriginationRestrictions ON OriginationRestrictions.id = SubscriberRestrictions.originationRestrictionId
					LEFT JOIN SubscriberStates ON SubscriberStates.id = SubscribersDataRecord.stateId
                    LEFT JOIN SubscriberBroadbandStates ON SubscriberBroadbandStates.id = SubscribersDataRecord.broadbandStateId
					LEFT JOIN Users ON Users.id = SubscribersDataRecord.userId
					LEFT JOIN People ON People.id = Users.personId
						WHERE Countries.code = countryCode AND Areas.code = areaCode AND OfficeCodes.code = officeCode AND PhoneNumbers.number = number
							ORDER BY SubscribersDataRecord.time;
END$$

DELIMITER ;
