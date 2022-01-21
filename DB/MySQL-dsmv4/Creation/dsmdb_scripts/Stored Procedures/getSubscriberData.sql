USE `dsmv4`;
DROP procedure IF EXISTS `getSubscriberData`;

DELIMITER $$
USE `dsmv4`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSubscriberData`(
IN subscriberId INT)
BEGIN
	SELECT OwnerPeople.id AS ownerPersonId, OwnerPeople.firstName AS ownerFirstName, OwnerPeople.middleName AS ownerMiddleName, OwnerPeople.lastName AS ownerLastName,
		AssigneePeople.id AS assigneePersonId, AssigneePeople.firstName AS assigneeFirstName, AssigneePeople.middleName AS assigneeMiddleName, AssigneePeople.lastName AS assigneeLastName,
        SubscribersData.lineClassId, SubscribersData.restrictionId, SubscribersData.stateId,SubscribersDataServices.serviceId, Wiring.broadbandId, SubscribersData.broadbandStateId, SubscribersData.information
			FROM Subscribers 
				LEFT JOIN Owners ON Owners.subscriberId = subscriberId
				LEFT JOIN Assignees ON Assignees.subscriberId = subscriberId
				LEFT JOIN People OwnerPeople ON OwnerPeople.id = Owners.personId
				LEFT JOIN People AssigneePeople ON AssigneePeople.id = Assignees.personId
                LEFT JOIN Wiring ON Wiring.distributorId = Subscribers.distributorId
                LEFT JOIN SubscribersData ON SubscribersData.id = Subscribers.dataId
                LEFT JOIN SubscribersDataServices ON SubscribersDataServices.dataId = SubscribersData.id
					WHERE Subscribers.id = subscriberId
						ORDER BY SubscribersDataServices.serviceId;
END$$

DELIMITER ;
