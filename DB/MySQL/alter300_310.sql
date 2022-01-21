ALTER TABLE ac1.Wiring
	ADD COLUMN secondStreetPairId INT NULL AFTER streetPairId;
ALTER TABLE ac1.Wiring
	ADD CONSTRAINT FK_Wiring_StreetPairs2
		FOREIGN KEY (secondStreetPairId)
			REFERENCES ac1.StreetPairs (id)
				ON DELETE NO ACTION
				ON UPDATE NO ACTION ;
    
ALTER TABLE ac1.WiringRecord
	ADD COLUMN secondStreetPairId INT NULL AFTER streetPairId;
ALTER TABLE ac1.WiringRecord
	ADD CONSTRAINT FK_WiringRecord_StreetPairs2
		FOREIGN KEY (secondStreetPairId)
			REFERENCES ac1.StreetPairs (id)
				ON DELETE NO ACTION
				ON UPDATE NO ACTION ;

ALTER TABLE ac1.Users
	ADD COLUMN `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
    
UPDATE `ac1`.`Features` SET `value`='3.1.0' WHERE `id`='1';
