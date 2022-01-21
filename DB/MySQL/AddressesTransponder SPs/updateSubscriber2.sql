USE `ac1`;
DROP procedure IF EXISTS `updateSubscriber2`;

DELIMITER $$
USE `ac1`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateSubscriber2`(
IN subscriberId INT,
IN distributorId INT,
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
IN userId INT)
BEGIN
    DECLARE phoneId INT DEFAULT NULL;
    DECLARE personAddressId INT DEFAULT NULL;

	SELECT phoneNumberId INTO phoneId
		FROM Addresses
			LEFT JOIN Subscribers ON Subscribers.addressId = Addresses.id
				WHERE Subscribers.id = subscriberId;
	
    DELETE FROM Owners
		WHERE Owners.subscriberId = subscriberId;

    DELETE FROM Assignees
		WHERE Assignees.subscriberId = subscriberId;

	IF(addressId = 0) THEN
		IF (streetId = 0) THEN
			SET streetId = NULL;
			SET locationId = NULL;
		END IF;
		IF (street1Id = 0) THEN
			SET street1Id = NULL;
		END IF;
		IF (street2Id = 0) THEN
			SET street2Id = NULL;
		END IF;

		INSERT INTO Addresses(streetId, number, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId)
			VALUES(streetId, addressNumber, floor, apartment, street1Id, street2Id, zipcode, locationId, phoneId);
		SELECT LAST_INSERT_ID() INTO personAddressId;

		INSERT INTO Addresses(streetId, number, floor, apartment, street1Id, street2Id, zipCode, locationId, phoneNumberId)
			VALUES(streetId, addressNumber, floor, apartment, street1Id, street2Id, zipcode, locationId, phoneId);
		SELECT LAST_INSERT_ID() INTO addressId;
        
	ELSE
		UPDATE Addresses
			SET Addresses.phoneNumberId = phoneNumberId
				WHERE id = addressId;
    END IF;

	UPDATE Subscribers
		SET addressId = addressId, distributorId = distributorId, remarks = remarks
			WHERE id = subscriberId;
    
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
 
