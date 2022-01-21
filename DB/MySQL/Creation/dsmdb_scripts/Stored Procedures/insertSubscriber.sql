USE `ac1`;
DROP procedure IF EXISTS `insertSubscriber`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertSubscriber`(
IN officeCodeId INT,
IN number CHAR(4),
IN technology VARCHAR(20),
IN equipmentId INT,
IN relationship VARCHAR(20),
IN personId INT,
IN firstName VARCHAR(20),
IN middleName VARCHAR(20),
IN lastName VARCHAR(45),
IN identificationTypeId INT,
IN identificationNumber INT,
IN addressId INT,
IN streetId INT,
IN addressNumber VARCHAR(15),
IN floor VARCHAR(10),
IN apartment VARCHAR(10),
IN street1Id INT,
IN street2Id INT,
IN zipCode VARCHAR(8),
IN locationId INT,
IN remarks VARCHAR(250),
IN type VARCHAR(20),
IN terminationRestriction CHAR(1),
IN originationRestriction CHAR(2),
IN state VARCHAR(20),
IN userId INT)
BEGIN
    DECLARE phoneNumberId INT DEFAULT NULL;
    DECLARE subscriberDataId INT DEFAULT NULL;
    DECLARE subscriberId INT DEFAULT NULL;
    DECLARE distributorId INT DEFAULT NULL;
    DECLARE personAddressId INT DEFAULT NULL;

	INSERT INTO PhoneNumbers(officeCodeId, number)
		VALUES(officeCodeId, number);
	SELECT LAST_INSERT_ID() INTO phoneNumberId;
	
	IF(addressId = 0) THEN
		IF (streetId = 0) THEN
			SET locationId = NULL;
            SET streetId = NULL;
		END IF;
		IF (street1Id = 0) THEN
			SET street1Id = NULL;
        END IF;
		IF (street2Id = 0) THEN
			SET street2Id = NULL;
        END IF;
        
		INSERT INTO Addresses(streetId, number, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId)
			VALUES(streetId, addressNumber, floor, apartment, street1Id, street2Id, zipcode, locationId, phoneNumberId);
		SELECT LAST_INSERT_ID() INTO personAddressId;
        
		INSERT INTO Addresses(streetId, number, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId)
			VALUES(streetId, addressNumber, floor, apartment, street1Id, street2Id, zipcode, locationId, phoneNumberId);
		SELECT LAST_INSERT_ID() INTO addressId;

	ELSE
		UPDATE Addresses
			SET Addresses.phoneNumberId = phoneNumberId
				WHERE id = addressId;
    END IF;

    IF(technology = 'NEAX61E') THEN
		SELECT id INTO distributorId
			FROM Distributor
				WHERE neax61eId = equipmentId;
	ELSE
		IF(technology = 'NEAX61SIGMA') THEN
			SELECT id INTO distributorId
				FROM Distributor
					WHERE neax61sigmaId = equipmentId;
	ELSE
		IF(technology = 'NEAX61SIGMA_ELU') THEN
			SELECT id INTO distributorId
				FROM Distributor
					WHERE sigmal3addrId = equipmentId;
	ELSE
		IF(technology = 'ZHONE') THEN
			SELECT id INTO distributorId
				FROM Distributor
					WHERE zhoneId = equipmentId;
	END IF;
	END IF;
    END IF;
    END IF;

	INSERT INTO SubscribersData(lineClassId, restrictionId, stateId)
		VALUES((SELECT id FROM SubscriberLineClasses WHERE SubscriberLineClasses.type = type),
				(SELECT id FROM SubscriberRestrictions WHERE originationRestrictionId = (SELECT id FROM OriginationRestrictions WHERE name = originationRestriction) AND terminationRestrictionId = (SELECT id FROM TerminationRestrictions WHERE name = terminationRestriction)),
                (SELECT id FROM SubscriberStates WHERE SubscriberStates.name = state));
	SELECT LAST_INSERT_ID() INTO subscriberDataId;
 
	INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, stateId, userId)
		VALUES(subscriberDataId,
				(SELECT id FROM SubscriberLineClasses WHERE SubscriberLineClasses.type = type),
				(SELECT id FROM SubscriberRestrictions WHERE originationRestrictionId = (SELECT id FROM OriginationRestrictions WHERE name = originationRestriction) AND terminationRestrictionId = (SELECT id FROM TerminationRestrictions WHERE name = terminationRestriction)),
                (SELECT id FROM SubscriberStates WHERE SubscriberStates.name = state),
                userId);

    INSERT INTO Subscribers(addressId, distributorId, dataId, remarks)
		VALUES(addressId, distributorId, subscriberDataId, remarks);
	SELECT LAST_INSERT_ID() INTO subscriberId;
    
    IF(personId = 0) THEN
		IF(firstName IS NOT NULL) THEN
			INSERT INTO People(firstName, middleName, lastName, addressId)
				VALUES(firstName, middleName, lastName, personAddressId);
			SELECT LAST_INSERT_ID() INTO personId;
	
			IF (identificationNumber <> 0) THEN
				UPDATE People
					SET identificationTypeId = identificationTypeId,
						identificationNumber = identificationNumber
							WHERE id = personId;
			END IF;
		ELSE
			SET personId = NULL;
        END IF;
    END IF;
    
    IF(personId IS NOT NULL) THEN
		IF(relationship = 'OWNER') THEN
			INSERT INTO Owners(personId, subscriberId)
				VALUES(personId, subscriberId);
			INSERT INTO SubscribersRecord(addressId, distributorId, ownerId, remarks, userId)
				VALUES(addressId, distributorId, personId, remarks, userId);
		ELSE
			INSERT INTO Assignees(personId, subscriberId)
				VALUES(personId, subscriberId);
			INSERT INTO SubscribersRecord(addressId, distributorId, assigneeId, remarks, userId)
				VALUES(addressId, distributorId, personId, remarks, userId);
		END IF;
	ELSE
			INSERT INTO SubscribersRecord(addressId, distributorId, remarks, userId)
				VALUES(addressId, distributorId, remarks, userId);
    END IF;
END$$

DELIMITER ;
