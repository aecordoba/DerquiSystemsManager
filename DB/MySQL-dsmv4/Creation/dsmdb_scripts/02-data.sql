-- GRANT USAGE ON *.* TO 'systemsmanager'@'%';
-- drop User 'systemsmanager'@'%';
-- GRANT USAGE ON *.* TO 'smapp'@'%';
-- drop User 'smapp'@'%';
-- flush privileges;


-- CREATE User 'systemsmanager'@'%' IDENTIFIED BY 'systemsmanager123';
GRANT ALL PRIVILEGES ON `dsmv4`.* TO 'systemsmanager'@'%';
-- CREATE User 'smapp'@'%' IDENTIFIED BY 'smapp123';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON `dsmv4`.* TO 'smapp'@'%';
GRANT SELECT ON mysql.proc TO 'smapp'@'%';

flush privileges;

use dsmv4;

### Features ###

INSERT INTO Features(name, value)
	VALUES('dbVersion', '4.1.0'),
	('countryCode.areaCode.officeCode1', '54.230.448'),
    ('countryCode.areaCode.officeCode2', '54.230.449.5'),
    ('countryCode.areaCode.officeCode3', '54.230.447.5'),
    ('countryCode.areaCode.officeCode4', '54.230.454'),
    ('internetProviderTechnology1', 'ZhoneEquipment');

### Descriptors ###

# Roles

INSERT INTO Roles(name, description)
	VALUES('administrator', 'System administrator'),
	('assignment-operator', 'Assignment operator'),
	('assignment-observer', 'Assignment observer'),
    ('cables-pairs-operator', 'Cables and pairs operator'),
    ('cables-pairs-observer', 'Cables and pairs observer'),
    ('dslams-operator', 'DSLAMs operator'),
    ('dslams-observer', 'DSLAMs observer'),
	('equipment-operator', 'Equipment operator'),
	('equipment-observer', 'Equipment observer'),
	('people-operator', 'People operator'),
	('people-observer', 'People observer'),
	('repairs-operator', 'Repairs operator'),
	('repairs-checker', 'Repairs checker'),
	('repairs-observer', 'Repairs observer'),
	('subscriber-data-operator', 'Subscriber data operator'),
	('subscriber-data-observer', 'Subscriber data observer'),
	('wiring-operator', 'Wiring operator'),
	('wiring-observer', 'Wiring observer'),
	('broadband-observer', 'Broadband observer');

# Countries

INSERT INTO Countries(name, code)
	VALUES('Argentina', 54);

# Areas

INSERT INTO Areas(name, code, countryId)
	VALUES('Buenos Aires', 11, (SELECT id FROM Countries WHERE code = 54)),
	('Pilar', 230, (SELECT id FROM Countries WHERE code = 54));

# OfficeCodes

INSERT INTO OfficeCodes(code, areaId)
	VALUES(448, (SELECT id FROM Areas WHERE code = 230 AND countryId = (SELECT id FROM Countries WHERE code = 54))),
	(447, (SELECT id FROM Areas WHERE code = 230 AND countryId = (SELECT id FROM Countries WHERE code = 54))),
    (449, (SELECT id FROM Areas WHERE code = 230 AND countryId = (SELECT id FROM Countries WHERE code = 54))),
    (454, (SELECT id FROM Areas WHERE code = 230 AND countryId = (SELECT id FROM Countries WHERE code = 54))),
    (4641, (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54))),
    (5738, (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54)));

# States

INSERT INTO States(name, countryId)
	VALUES('C.A.B.A.',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Buenos Aires',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Santa Fé',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Córdoba',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Entre Ríos',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('La Pampa',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Corrientes',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Mendoza',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Chaco',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Santiago del Estero',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Misiones',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Formosa',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Salta',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Jujuy',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Tucumán',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Catamarca',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('La Rioja',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('San Juan',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('San Luis',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Neuquén',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Río Negro',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Chubut',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Santa Cruz',(SELECT id FROM Countries WHERE name = 'Argentina')),
	('Tierra del Fuego',(SELECT id FROM Countries WHERE name = 'Argentina'));

# Cities

INSERT INTO Cities(name, stateId)
	VALUES('C.A.B.A.',(SELECT id FROM States WHERE name = 'C.A.B.A.')),
	('Pilar',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('Derqui',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('Villa Rosa',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('Ramos Mejía',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('Haedo',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('Ituzaingo',(SELECT id FROM States WHERE name = 'Buenos Aires')),
    ('José C. Paz',(SELECT id FROM States WHERE name = 'Buenos Aires'));

# Locations

INSERT INTO Locations(name, cityId)
	VALUES('Liniers',(SELECT id FROM Cities WHERE name = 'C.A.B.A.')),
	('Derqui',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('Toro',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('Santa Ana',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('La Cautiva',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('San Souci',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('Ribera Villate',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('Villa Luján',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('Monterrey',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('La Alborada',(SELECT id FROM Cities WHERE name = 'Derqui')),
	('La Escondida',(SELECT id FROM Cities WHERE name = 'Derqui')),
    ('Villa Rosa',(SELECT id FROM Cities WHERE name = 'Villa Rosa')),
    ('Haedo',(SELECT id FROM Cities WHERE name = 'Haedo')),
    ('Ituzaingo',(SELECT id FROM Cities WHERE name = 'Ituzaingo')),
    ('José C. Paz',(SELECT id FROM Cities WHERE name = 'José C. Paz'));

# PhoneNumbers

INSERT INTO PhoneNumbers(officeCodeId, number)
	VALUES((SELECT id FROM OfficeCodes WHERE code = 4641 AND areaId = (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54))), '4581'),
	((SELECT id FROM OfficeCodes WHERE code = 5738 AND areaId = (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54))), '5359');

# IdentificationTypes

INSERT INTO IdentificationsTypes(name)
	VALUES('DNI'),
	('CI'),
	('LC/LE'),
	('Pasaporte');

# Sites

INSERT INTO Sites(name, code)
	VALUES('Derqui', 'DRQ'),
	('Villa Rosa', 'VRS'),
	('Muzilli', 'MZL'),
	('Springdale', 'SPD');

# StreetFrames

INSERT INTO StreetFrames(siteId, name, description)
	VALUES((SELECT id FROM Sites WHERE code = 'DRQ'), 'Z', NULL),
		((SELECT id FROM Sites WHERE code = 'DRQ'), 'A', NULL),
        ((SELECT id FROM Sites WHERE code = 'DRQ'), 'C', NULL),
        ((SELECT id FROM Sites WHERE code = 'VRS'), 'V', NULL),
        ((SELECT id FROM Sites WHERE code = 'MZL'), 'U', NULL),
        ((SELECT id FROM Sites WHERE code = 'SPD'), 'S', NULL);

# ModemTypes

INSERT INTO ModemsTypes(name)
	VALUES('Ethernet'),
	('USB'),
	('Wireless');

# ModemsManufacturers

INSERT INTO ModemsManufacturers(name)
	VALUES('ZyXEL'),
	('TP-LINK'),
	('NISUTA'),
	('TENDA');

# ModemsModels

INSERT INTO ModemsModels(name, manufacturerId, typeId)
	VALUES('AMG1001-T10A', (SELECT id FROM ModemsManufacturers WHERE name = 'ZyXEL'), (SELECT id FROM ModemsTypes WHERE name = 'Ethernet')),
	('P-660R-T1 v2', (SELECT id FROM ModemsManufacturers WHERE name = 'ZyXEL'), (SELECT id FROM ModemsTypes WHERE name = 'Ethernet')),
	('P-660R-T1 v3s', (SELECT id FROM ModemsManufacturers WHERE name = 'ZyXEL'), (SELECT id FROM ModemsTypes WHERE name = 'Ethernet')),
	('TD-8816', (SELECT id FROM ModemsManufacturers WHERE name = 'TP-LINK'), (SELECT id FROM ModemsTypes WHERE name = 'Ethernet')),
	('TD-8817', (SELECT id FROM ModemsManufacturers WHERE name = 'TP-LINK'), (SELECT id FROM ModemsTypes WHERE name = 'Ethernet')),
	('TD-W8951ND', (SELECT id FROM ModemsManufacturers WHERE name = 'TP-LINK'), (SELECT id FROM ModemsTypes WHERE name = 'Wireless')),
    ('NS-WMR150N2', (SELECT id FROM ModemsManufacturers WHERE name = 'NISUTA'), (SELECT id FROM ModemsTypes WHERE name = 'Wireless'));

# DSLAMsManufacturers

INSERT INTO DSLAMsManufacturers(name)
	VALUES('ZTE'),
	('ZyXEL');

# DSLAMsModels

INSERT INTO DSLAMsModels(name, slots, manufacturerId, ports)
	VALUES('ZXDSL 8426', '0',(SELECT id FROM DSLAMsManufacturers WHERE name = 'ZTE'), 24),
	('IES-1248-51A', '0',(SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 48),
	('IP EXPRESS IES-1248-71', '0', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 48),
	('IP EXPESS IES-5000', '3-4-5-6-7-8-9-10',(SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 0),
	('IES-6000M', '1-2-3-4-5-6-7-10-11-12-13-14-15-16-17',(SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 0),
	('IES-2000', '2-3-5-6', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 0),
	('IP EXPRESS IES-1000', '1-2', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 0),
	('IES-5005', '2-3-4-5',(SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 0);
	
# BoardsModels

INSERT INTO DSLAMsBoardsModels(name, manufacturerId, ports, description)
	VALUES('ALC1248G-51', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 48, 'Splitter 2 x ASC1024'),
	('ALC1272G-51', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 72, 'Splitter integrated'),
	('ALC1372G-51', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 72, 'Splitter integrated'),
	('ALC1024-61', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 24, 'Splitter ASC1024'),
	('ALC1024-71', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 24, 'Splitter integrated'),
	('ANNEX A', (SELECT id FROM DSLAMsManufacturers WHERE name = 'ZyXEL'), 12, NULL);

# RoutersManufacturers

INSERT INTO RoutersManufacturers(name)
	VALUES('MikroTik');
	
# RoutersModels

INSERT INTO RoutersModels(name, ports, manufacturerId)
	VALUES('RouterBoard 1100', 24, (SELECT id FROM RoutersManufacturers WHERE name = 'MikroTik')),
	('Server', 10,(SELECT id FROM RoutersManufacturers WHERE name = 'MikroTik'));

### Data ###
# Streets

INSERT INTO Streets(id, name, code)
	VALUES(10001, 'TUYUTÍ', 'TYT'),
    (10002, 'MONTIEL', 'MNT'),
    (10003, 'GÜEMES TORINO', 'GTN');

# Addresses

INSERT INTO Addresses(streetId, number, street1Id, street2Id, zipCode, locationId, phoneNumberId)
	VALUES((SELECT id FROM Streets WHERE name = 'TUYUTÍ'),'7126', (SELECT id FROM Streets WHERE name = 'MONTIEL'), (SELECT id FROM Streets WHERE name = 'GÜEMES TORINO'), 'C1408CFN', (SELECT id FROM Locations WHERE name = 'Liniers' AND cityId = (SELECT id FROM Cities WHERE name = 'C.A.B.A.' AND stateId = (SELECT id FROM States WHERE name = 'C.A.B.A.' AND countryId = (SELECT id FROM Countries WHERE name = 'Argentina')))), (SELECT id FROM PhoneNumbers WHERE number = '4581' AND officeCodeId = (SELECT id FROM OfficeCodes WHERE code = 4641 AND areaId = (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54)))));

# People

INSERT INTO People(firstName, middleName, lastName, identificationTypeId, identificationNumber, addressId, phoneNumberId)
	VALUES('Adrián', 'Esteban', 'Córdoba', (SELECT id FROM IdentificationsTypes WHERE name = 'DNI'), 14009938, (SELECT id FROM Addresses WHERE streetId = (SELECT id FROM Streets WHERE name = 'TUYUTÍ') AND number = '7126'), (SELECT id FROM PhoneNumbers WHERE number = '5359' AND officeCodeId = (SELECT id FROM OfficeCodes WHERE code = 5738 AND areaId = (SELECT id FROM Areas WHERE code = 11 AND countryId = (SELECT id FROM Countries WHERE code = 54)))));

# Users

INSERT Users(name, password, personId)
	VALUES('SMadmin', 'bbe11a5f928ee4934a2a8342878fb7d2681d5d6267c8fc0e5e6600c49f83151a72b0eec3e577590c9cb25613e14ed8b5028915ef06ded09fc6bbb5304ddb63cb', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)), # password: sm123
    ('sysopertest', '376f2ca9e3c54382de9f760fce6f9b015dbe26e3cacba016434896f8ba0fc7c6e73f7505e0590e92a3af726b58ee8392302d7d00b4d7653cc82b7af79c9b5fba', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)), # password: oper123
    ('bustest', '0e9b77ecfc7547ee3f39c498cbfddc3229a6bb9f8d51d9c8b6d0203c7650b1fb1cc396c8fd4e20bc2921b55198c3bf06c238910d812cce71012af428cebde0c1', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)), # password: bus123
    ('usertest', '70e9b857aca8d91bc6407f76262723939ea25cdaf74644820afffd28cfdba12d84121fd225a1c7bdac0c7d9116e04a08bde682716e43d24ac31436b8eb8f575a', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)), # password: user123
    ('noseytest', '9050460532895977a76a586169878597255e67bf30bd1cdc1a4387deac44f7faab246b201c6888589407113cbfaf55b3c1f835c1a2675fd27d3aec42321f7798', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)), # password: nosey123
    ('repairtest', '2552ea01ebc4ce7261272ceeec7d6d69692d997200d991e116c7665435bd6af701b02ef0d78a1e1b9ed028e1fb5a27999fd1d9fbc31c4956e57b8d5df192003f', (SELECT id FROM People WHERE IdentificationTypeId = (SELECT id FROM IdentificationsTypes WHERE name = 'DNI') AND IdentificationNumber = 14009938)); # password: repair123
 
# UsersRoles

INSERT INTO UsersRoles(userId, roleId)
	VALUES((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'administrator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'assignment-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'cables-pairs-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'dslams-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'equipment-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'people-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'repairs-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'repairs-checker')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'subscriber-data-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'wiring-operator')),
	((SELECT id FROM Users WHERE name = 'SMadmin'), (SELECT id FROM Roles WHERE name = 'broadband-observer')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'assignment-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'cables-pairs-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'dslams-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'equipment-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'people-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'repairs-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'repairs-checker')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'subscriber-data-operator')),
	((SELECT id FROM Users WHERE name = 'sysopertest'), (SELECT id FROM Roles WHERE name = 'wiring-operator')),
	((SELECT id FROM Users WHERE name = 'bustest'), (SELECT id FROM Roles WHERE name = 'subscriber-data-operator')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'assignment-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'cables-pairs-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'dslams-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'equipment-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'people-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'repairs-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'subscriber-data-observer')),
	((SELECT id FROM Users WHERE name = 'usertest'), (SELECT id FROM Roles WHERE name = 'wiring-observer')),
	((SELECT id FROM Users WHERE name = 'noseytest'), (SELECT id FROM Roles WHERE name = 'subscriber-data-observer')),
	((SELECT id FROM Users WHERE name = 'repairtest'), (SELECT id FROM Roles WHERE name = 'repairs-operator'));


# TerminationRestrictions

INSERT INTO TerminationRestrictions(name, description)
	VALUES('0', 'No restriction'),
    ('1', 'Total restriction');

# OriginationRestrictions

INSERT INTO OriginationRestrictions(name, description)
	VALUES('00', 'No restriction'),
    ('01', 'Total restriction'),
    ('02', 'Emergencias+0800+Local'),
    ('03', 'Emergencias+0800+Local+Pilar+Pilar(celulares)'),
    ('04', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)'),
    ('05', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~3)'),
    ('06', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~4)'),
    ('07', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~5)'),
    ('08', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~6)'),
    ('09', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~7)'),
    ('10', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)'),
    ('11', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)'),
    ('12', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE+BUE(celulares)+DDN(1~11)'),
	('17', 'MOROSO (Emergencias)'),
    ('21', 'MOROSO (Emergencias)'),
    ('22', 'MOROSO (Emergencias+Local)'),
    ('23', 'MOROSO (Emergencias+Local+Pilar)'),
    ('24', 'MOROSO (Emergencias+Local+Pilar+BUE)'),
    ('29', 'MOROSO (Emergencias+0800)'),
    ('33', 'Emergencias+0800+Local+Pilar'),
    ('34', 'Emergencias+0800+Local+Pilar+Pilar(celulares)+BUE'),
    ('35', 'Emergencias+0800+Local+Pilar+BUE'),
    ('36', 'Emergencias+0800+Local+Pilar+BUE+BUE(celulares)+DDN(1~11)'),
    ('37', 'Emergencias+0800+Local+Pilar+BUE+DDN(1~11)+DDI');

# SubscriberRestrictions

INSERT INTO SubscriberRestrictions(originationRestrictionId, terminationRestrictionId)
	VALUES((SELECT id FROM OriginationRestrictions WHERE name = '00'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '01'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '02'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '03'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '04'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '05'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '06'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '07'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '08'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '09'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '10'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '11'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '12'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '17'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '21'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '22'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '23'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '24'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '29'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '33'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '34'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '35'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '36'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '37'), (SELECT id FROM TerminationRestrictions WHERE name = '0')),    ((SELECT id FROM OriginationRestrictions WHERE name = '00'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '01'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '02'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '03'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '04'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '05'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '06'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '07'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '08'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '09'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '10'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '11'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '12'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '17'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '21'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '22'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '23'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '24'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '29'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '33'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '34'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '35'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '36'), (SELECT id FROM TerminationRestrictions WHERE name = '1')),
    ((SELECT id FROM OriginationRestrictions WHERE name = '37'), (SELECT id FROM TerminationRestrictions WHERE name = '1'));

# SubscriberServices

INSERT INTO SubscriberServices(name, description)
	VALUES('clip', 'Caller ID'),
    ('clir', 'ID Restriction'),
    ('cf', 'Call Forwarding'),
    ('cw', 'Call Waiting'),
    ('cfda', 'Call Forwarding Dont Answer'),
    ('cfbl', 'Call Forwarding Busy Line');

# SubscriberLineClasses

INSERT INTO SubscriberLineClasses(type, description)
	VALUES('INDIVIDUAL', 'Common subscriber'),
    ('PUBLIC', 'Public subscriber'),
    ('SPECIAL', 'Special subscriber');

# SubscriberStates

INSERT INTO SubscriberStates(name, description)
	VALUES('ENABLED', 'All services enabled'),
    ('DISABLED', 'All services disabled'),
    ('DEBTOR', 'Debtor subscriber');

# SubscriberBroadbandStates

INSERT INTO SubscriberBroadbandStates(name, description)
	VALUES('ENABLED', 'Broadband enabled'),
    ('DEBTOR', 'Broadband debtor'),
    ('DISABLED', 'Broadband disabled');
    
# RepairingTypes

INSERT INTO RepairingTypes(id, name, description)
	VALUES(1, 'NO_TONE', 'No tone'),
    (2, 'NOISE', 'Noisy line'),
    (3, 'LINKED', 'Linked line'),
    (4, 'NO_RINGING', 'No ringing'),
    (5, 'BROKEN', 'Bronken phone'),
    (6, 'DISTURB', 'Disturbing calls'),
    (7, 'NOT_RECEIVE', 'Does not receive calls'),
    (8, 'NOT_LISTEN_TO_HIM', 'They do not listen to him'),
    (9, 'CALL_CUT', 'Call is cut'),
    (10, 'NOT_OUTGOING_CALLS', 'Cannot make outgoing calls'),
    (11, 'INTERNET', 'Internet problem'),
    (12, 'OTHER', 'Other problems'),
    (13, 'BROADBAND', 'Broadband problem'),
    (21, 'NEW_LINE', 'New line'),
    (22, 'TRANSFER', 'Move line'),
    (23, 'PARALLEL', 'Parallel line'),
    (24, 'ADDITIONAL', 'Additional rosette'),
    (25, 'RELOCATE_WIRING', 'Relocate wiring'),
    (26, 'CLIENT_INTERNAL', 'Client internal installation');

# ServiceOrders AUTO_INCREMENT start

ALTER TABLE ServiceOrders AUTO_INCREMENT = 2018;
