-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema ac1
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ac1` ;

-- -----------------------------------------------------
-- Schema ac1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ac1` DEFAULT CHARACTER SET utf8 ;
USE `ac1` ;

-- -----------------------------------------------------
-- Table `ac1`.`Countries`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Countries` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Countries` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `code` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`States`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`States` ;

CREATE TABLE IF NOT EXISTS `ac1`.`States` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `countryId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `state_UNIQUE` (`name` ASC, `countryId` ASC),
  INDEX `FK_States_1_idx` (`countryId` ASC),
  CONSTRAINT `FK_States_Countries`
    FOREIGN KEY (`countryId`)
    REFERENCES `ac1`.`Countries` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Cities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Cities` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Cities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `stateId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `city_UNIQUE` (`name` ASC, `stateId` ASC),
  INDEX `FK_Cities_1_idx` (`stateId` ASC),
  CONSTRAINT `FK_Cities_States`
    FOREIGN KEY (`stateId`)
    REFERENCES `ac1`.`States` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Locations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Locations` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Locations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `cityId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `location_UNIQUE` (`name` ASC, `cityId` ASC),
  INDEX `FK_Locations_1_idx` (`cityId` ASC),
  CONSTRAINT `FK_Locations_Cities`
    FOREIGN KEY (`cityId`)
    REFERENCES `ac1`.`Cities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Areas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Areas` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Areas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `code` INT NOT NULL,
  `countryId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `areaCode_UNIQUE` (`code` ASC, `countryId` ASC),
  INDEX `fk_Areas_Countries_idx` (`countryId` ASC),
  CONSTRAINT `fk_Areas_Countries`
    FOREIGN KEY (`countryId`)
    REFERENCES `ac1`.`Countries` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`OfficeCodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`OfficeCodes` ;

CREATE TABLE IF NOT EXISTS `ac1`.`OfficeCodes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` INT NOT NULL,
  `areaId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `officeCode_UNIQUE` (`code` ASC, `areaId` ASC),
  INDEX `fk_OfficeCodes_Areas_idx` (`areaId` ASC),
  CONSTRAINT `fk_OfficeCodes_Areas`
    FOREIGN KEY (`areaId`)
    REFERENCES `ac1`.`Areas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`PhoneNumbers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`PhoneNumbers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`PhoneNumbers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `officeCodeId` INT NOT NULL,
  `number` CHAR(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `number_UNIQUE` (`number` ASC, `officeCodeId` ASC),
  INDEX `fk_PhoneNumbers_OfficeCodes_idx` (`officeCodeId` ASC),
  CONSTRAINT `fk_PhoneNumbers_OfficeCodes`
    FOREIGN KEY (`officeCodeId`)
    REFERENCES `ac1`.`OfficeCodes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Streets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Streets` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Streets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `code` VARCHAR(5) NULL,
  `spare` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Addresses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Addresses` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Addresses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `streetId` INT NULL,
  `number` VARCHAR(15) NULL,
  `floor` VARCHAR(10) NULL,
  `apartment` VARCHAR(10) NULL,
  `street1Id` INT NULL,
  `street2Id` INT NULL,
  `zipCode` VARCHAR(8) NULL,
  `locationId` INT NULL,
  `phoneNumberId` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_Addresses_1_idx1` (`locationId` ASC),
  INDEX `FK_Addresses_1_idx` (`phoneNumberId` ASC),
  INDEX `FK_Addresses_Streets_idx` (`streetId` ASC),
  INDEX `FK_Addresses_Streets1_idx` (`street1Id` ASC),
  INDEX `FK_Addresses_Streets2_idx` (`street2Id` ASC),
  CONSTRAINT `FK_Addresses_Locations`
    FOREIGN KEY (`locationId`)
    REFERENCES `ac1`.`Locations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Addresses_PhoneNumbers`
    FOREIGN KEY (`phoneNumberId`)
    REFERENCES `ac1`.`PhoneNumbers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Addresses_Streets`
    FOREIGN KEY (`streetId`)
    REFERENCES `ac1`.`Streets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Addresses_Streets1`
    FOREIGN KEY (`street1Id`)
    REFERENCES `ac1`.`Streets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Addresses_Streets2`
    FOREIGN KEY (`street2Id`)
    REFERENCES `ac1`.`Streets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`IdentificationsTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`IdentificationsTypes` ;

CREATE TABLE IF NOT EXISTS `ac1`.`IdentificationsTypes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`People`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`People` ;

CREATE TABLE IF NOT EXISTS `ac1`.`People` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(20) NOT NULL,
  `middleName` VARCHAR(20) NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `identificationTypeId` INT NULL,
  `identificationNumber` INT NULL,
  `addressId` INT NULL,
  `phoneNumberId` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_People_2_idx` (`addressId` ASC),
  INDEX `FK_People_1_idx1` (`identificationTypeId` ASC),
  INDEX `FK_People_1_idx` (`phoneNumberId` ASC),
  CONSTRAINT `FK_People_Addresses`
    FOREIGN KEY (`addressId`)
    REFERENCES `ac1`.`Addresses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_People_IdentificationsTypes`
    FOREIGN KEY (`identificationTypeId`)
    REFERENCES `ac1`.`IdentificationsTypes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_People_PhoneNumbers`
    FOREIGN KEY (`phoneNumberId`)
    REFERENCES `ac1`.`PhoneNumbers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Users` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `personId` INT NOT NULL,
  `time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`name` ASC),
  INDEX `FK_Users_1_idx` (`personId` ASC),
  CONSTRAINT `FK_Users_People`
    FOREIGN KEY (`personId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Roles` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Sites`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Sites` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Sites` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `code` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SwitchBlocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SwitchBlocks` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SwitchBlocks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(4) NOT NULL,
  `siteId` INT NOT NULL,
  `description` VARCHAR(100) NULL,
  `positions` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `block_UNIQUE` (`name` ASC, `siteId` ASC),
  INDEX `fk_SwitchBlocks_Sites_idx` (`siteId` ASC),
  CONSTRAINT `fk_SwitchBlocks_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`BlockPositions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`BlockPositions` ;

CREATE TABLE IF NOT EXISTS `ac1`.`BlockPositions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order` INT NOT NULL,
  `position` VARCHAR(4) NOT NULL,
  `switchBlockId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `position_UNIQUE` (`position` ASC, `switchBlockId` ASC),
  INDEX `fk_BlockPositions_SwitchBlocks_idx` (`switchBlockId` ASC),
  UNIQUE INDEX `order_UNIQUE` (`order` ASC, `switchBlockId` ASC),
  CONSTRAINT `fk_BlockPositions_SwitchBlocks`
    FOREIGN KEY (`switchBlockId`)
    REFERENCES `ac1`.`SwitchBlocks` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SigmaFrames`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SigmaFrames` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SigmaFrames` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  `siteId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `SFrames_UNIQUE` (`name` ASC, `siteId` ASC),
  INDEX `FK_SFrames_Sites_idx` (`siteId` ASC),
  CONSTRAINT `FK_SFrames_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SigmaLineModules`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SigmaLineModules` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SigmaLineModules` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(2) NOT NULL,
  `frameId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_SLineModules_1_idx` (`frameId` ASC),
  UNIQUE INDEX `lineModule_UNIQUE` (`name` ASC, `frameId` ASC),
  CONSTRAINT `FK_SLineModules_SFrames`
    FOREIGN KEY (`frameId`)
    REFERENCES `ac1`.`SigmaFrames` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`NEAX61Sigma`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`NEAX61Sigma` ;

CREATE TABLE IF NOT EXISTS `ac1`.`NEAX61Sigma` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `timeSwitch` CHAR(2) NOT NULL,
  `kHighway` CHAR(2) NOT NULL,
  `pHighway` CHAR(2) NOT NULL,
  `row` CHAR(1) NOT NULL,
  `column` CHAR(2) NOT NULL,
  `lineModuleId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `SigmaEL_UNIQUE` (`column` ASC, `timeSwitch` ASC, `kHighway` ASC, `pHighway` ASC, `row` ASC),
  INDEX `FK_NEAX61S_SLineModules_idx` (`lineModuleId` ASC),
  CONSTRAINT `FK_NEAX61S_igmaSLineModules`
    FOREIGN KEY (`lineModuleId`)
    REFERENCES `ac1`.`SigmaLineModules` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`EFrames`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`EFrames` ;

CREATE TABLE IF NOT EXISTS `ac1`.`EFrames` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  `siteId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `EFrames_UNIQUE` (`name` ASC, `siteId` ASC),
  INDEX `FK_EFrames_1_idx` (`siteId` ASC),
  CONSTRAINT `FK_EFrames_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`ELineModules`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`ELineModules` ;

CREATE TABLE IF NOT EXISTS `ac1`.`ELineModules` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(2) NOT NULL,
  `frameId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `lineModule_UNIQUE` (`name` ASC, `frameId` ASC),
  INDEX `FK_ELineModules_1_idx` (`frameId` ASC),
  CONSTRAINT `FK_ELineModules_EFrames`
    FOREIGN KEY (`frameId`)
    REFERENCES `ac1`.`EFrames` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`NEAX61E`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`NEAX61E` ;

CREATE TABLE IF NOT EXISTS `ac1`.`NEAX61E` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `spce` CHAR(2) NOT NULL,
  `highway` CHAR(1) NOT NULL,
  `subhighway` CHAR(1) NOT NULL,
  `group` CHAR(2) NOT NULL,
  `switch` CHAR(1) NOT NULL,
  `level` CHAR(1) NOT NULL,
  `lineModuleId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `EEL_UNIQUE` (`spce` ASC, `highway` ASC, `group` ASC, `switch` ASC, `level` ASC, `subhighway` ASC),
  INDEX `FK_NEAX61E_ELineModules_idx` (`lineModuleId` ASC),
  CONSTRAINT `FK_NEAX61E_ELineModules`
    FOREIGN KEY (`lineModuleId`)
    REFERENCES `ac1`.`ELineModules` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Zhone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Zhone` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Zhone` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cable` CHAR(2) NOT NULL,
  `port` CHAR(2) NOT NULL,
  `siteId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `Equipment_UNIQUE` (`cable` ASC, `port` ASC),
  INDEX `FK_Zhone_Sites_idx` (`siteId` ASC),
  CONSTRAINT `FK_Zhone_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`NEAX61SigmaELUs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`NEAX61SigmaELUs` ;

CREATE TABLE IF NOT EXISTS `ac1`.`NEAX61SigmaELUs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(8) NOT NULL,
  `timeSwitch` CHAR(2) NOT NULL,
  `kHighway` CHAR(2) NOT NULL,
  `pHighway` CHAR(2) NOT NULL,
  `siteId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  INDEX `FK_SigmaELUs_1_idx` (`siteId` ASC),
  CONSTRAINT `FK_SigmaELUs_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SigmaL3Addr`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SigmaL3Addr` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SigmaL3Addr` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `eluId` INT NOT NULL,
  `l3addr` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `eluId_l3addr_UNIQUE` (`eluId` ASC, `l3addr` ASC),
  CONSTRAINT `FK_L3Addr_SigmaELUs`
    FOREIGN KEY (`eluId`)
    REFERENCES `ac1`.`NEAX61SigmaELUs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Distributor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Distributor` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Distributor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `blockPositionId` INT NOT NULL,
  `neax61sigmaId` INT NULL,
  `sigmal3addrId` INT NULL,
  `neax61eId` INT NULL,
  `zhoneId` INT NULL,
  `available` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `FK_Distributor_2_idx` (`blockPositionId` ASC),
  UNIQUE INDEX `blockPosition_UNIQUE` (`blockPositionId` ASC),
  INDEX `FK_Distributor_1_idx2` (`neax61sigmaId` ASC),
  INDEX `FK_Distributor_1_idx3` (`neax61eId` ASC),
  INDEX `FK_Distributor_1_idx4` (`zhoneId` ASC),
  UNIQUE INDEX `neax61sId_UNIQUE` (`neax61sigmaId` ASC),
  UNIQUE INDEX `neax61eId_UNIQUE` (`neax61eId` ASC),
  UNIQUE INDEX `fracasId_UNIQUE` (`zhoneId` ASC),
  UNIQUE INDEX `sigmal3addrId_UNIQUE` (`sigmal3addrId` ASC),
  CONSTRAINT `FK_Distributor_BlockPositions`
    FOREIGN KEY (`blockPositionId`)
    REFERENCES `ac1`.`BlockPositions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Distributor_NEAX61Sigma`
    FOREIGN KEY (`neax61sigmaId`)
    REFERENCES `ac1`.`NEAX61Sigma` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Distributor_NEAX61E`
    FOREIGN KEY (`neax61eId`)
    REFERENCES `ac1`.`NEAX61E` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Distributor_Zhone`
    FOREIGN KEY (`zhoneId`)
    REFERENCES `ac1`.`Zhone` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Distributor_SigmaL3Addr`
    FOREIGN KEY (`sigmal3addrId`)
    REFERENCES `ac1`.`SigmaL3Addr` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscriberLineClasses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscriberLineClasses` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscriberLineClasses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscriberStates`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscriberStates` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscriberStates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `state_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`OriginationRestrictions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`OriginationRestrictions` ;

CREATE TABLE IF NOT EXISTS `ac1`.`OriginationRestrictions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`TerminationRestrictions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`TerminationRestrictions` ;

CREATE TABLE IF NOT EXISTS `ac1`.`TerminationRestrictions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscriberRestrictions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscriberRestrictions` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscriberRestrictions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `originationRestrictionId` INT NOT NULL,
  `terminationRestrictionId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_SubscribersRestrictions_1_idx` (`originationRestrictionId` ASC),
  INDEX `fk_SubscribersRestrictions_Termination_idx` (`terminationRestrictionId` ASC),
  UNIQUE INDEX `originationId_terminationId_UNIQUE` (`originationRestrictionId` ASC, `terminationRestrictionId` ASC),
  CONSTRAINT `fk_SubscribersRestrictions_Origination`
    FOREIGN KEY (`originationRestrictionId`)
    REFERENCES `ac1`.`OriginationRestrictions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersRestrictions_Termination`
    FOREIGN KEY (`terminationRestrictionId`)
    REFERENCES `ac1`.`TerminationRestrictions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscriberBroadbandStates`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscriberBroadbandStates` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscriberBroadbandStates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscribersData`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscribersData` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscribersData` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lineClassId` INT NOT NULL,
  `restrictionId` INT NOT NULL,
  `stateId` INT NOT NULL,
  `broadbandStateId` INT NULL,
  `information` VARCHAR(250) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_SubscribersData_1_idx` (`lineClassId` ASC),
  INDEX `fk_SubscribersData_1_idx1` (`stateId` ASC),
  INDEX `fk_SubscribersData_Restrictions_idx` (`restrictionId` ASC),
  INDEX `fk_SubscribersData_BroadbandStates_idx` (`broadbandStateId` ASC),
  CONSTRAINT `fk_SubscribersData_LineClasses`
    FOREIGN KEY (`lineClassId`)
    REFERENCES `ac1`.`SubscriberLineClasses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersData_States`
    FOREIGN KEY (`stateId`)
    REFERENCES `ac1`.`SubscriberStates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersData_Restrictions`
    FOREIGN KEY (`restrictionId`)
    REFERENCES `ac1`.`SubscriberRestrictions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersData_BroadbandStates`
    FOREIGN KEY (`broadbandStateId`)
    REFERENCES `ac1`.`SubscriberBroadbandStates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Subscribers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Subscribers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Subscribers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `addressId` INT NOT NULL,
  `distributorId` INT NOT NULL,
  `dataId` INT NOT NULL,
  `remarks` VARCHAR(250) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `FK_Subscribers_1_idx1` (`addressId` ASC),
  INDEX `fk_Subscribers_Distributor_idx` (`distributorId` ASC),
  UNIQUE INDEX `distributorId_UNIQUE` (`distributorId` ASC),
  INDEX `fk_Subscribers_Data_idx` (`dataId` ASC),
  CONSTRAINT `FK_Subscribers_Addresses`
    FOREIGN KEY (`addressId`)
    REFERENCES `ac1`.`Addresses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subscribers_Distributor`
    FOREIGN KEY (`distributorId`)
    REFERENCES `ac1`.`Distributor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subscribers_Data`
    FOREIGN KEY (`dataId`)
    REFERENCES `ac1`.`SubscribersData` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Owners`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Owners` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Owners` (
  `subscriberId` INT NOT NULL,
  `personId` INT NOT NULL,
  INDEX `fk_Owner_1_idx` (`subscriberId` ASC),
  INDEX `FK_People_Owners_idx` (`personId` ASC),
  UNIQUE INDEX `subscriberId_UNIQUE` (`subscriberId` ASC),
  CONSTRAINT `FK_Owners_Subscribers`
    FOREIGN KEY (`subscriberId`)
    REFERENCES `ac1`.`Subscribers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Owners_People`
    FOREIGN KEY (`personId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Assignees`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Assignees` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Assignees` (
  `subscriberId` INT NOT NULL,
  `personId` INT NOT NULL,
  INDEX `FK_Subscribers_Concerning_idx` (`subscriberId` ASC),
  INDEX `FK_Concerning_1_idx` (`personId` ASC),
  UNIQUE INDEX `subscriberId_UNIQUE` (`subscriberId` ASC),
  CONSTRAINT `FK_Assignees_Subscribers`
    FOREIGN KEY (`subscriberId`)
    REFERENCES `ac1`.`Subscribers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Assignees_People`
    FOREIGN KEY (`personId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`ModemsManufacturers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`ModemsManufacturers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`ModemsManufacturers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`ModemsTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`ModemsTypes` ;

CREATE TABLE IF NOT EXISTS `ac1`.`ModemsTypes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`ModemsModels`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`ModemsModels` ;

CREATE TABLE IF NOT EXISTS `ac1`.`ModemsModels` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `typeId` INT NOT NULL,
  `manufacturerId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_Modems_1_idx` (`manufacturerId` ASC),
  INDEX `FK_Modems_1_idx1` (`typeId` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `FK_ModemsModels_Manufacturers`
    FOREIGN KEY (`manufacturerId`)
    REFERENCES `ac1`.`ModemsManufacturers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ModemsModels_Types`
    FOREIGN KEY (`typeId`)
    REFERENCES `ac1`.`ModemsTypes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`DSLAMsManufacturers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`DSLAMsManufacturers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`DSLAMsManufacturers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`DSLAMsModels`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`DSLAMsModels` ;

CREATE TABLE IF NOT EXISTS `ac1`.`DSLAMsModels` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `slots` VARCHAR(100) NOT NULL,
  `manufacturerId` INT NOT NULL,
  `ports` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_DSLAMs_1_idx` (`manufacturerId` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `FK_DSLAMsModels_Manufacturers`
    FOREIGN KEY (`manufacturerId`)
    REFERENCES `ac1`.`DSLAMsManufacturers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`RoutersManufacturers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`RoutersManufacturers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`RoutersManufacturers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`RoutersModels`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`RoutersModels` ;

CREATE TABLE IF NOT EXISTS `ac1`.`RoutersModels` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `ports` INT NOT NULL,
  `manufacturerId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_RoutersModels_1_idx` (`manufacturerId` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `FK_RoutersModels_manufacturers`
    FOREIGN KEY (`manufacturerId`)
    REFERENCES `ac1`.`RoutersManufacturers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Routers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Routers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Routers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `IPAddress` VARCHAR(39) NOT NULL,
  `modelId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_Routers_1_idx1` (`modelId` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `IPAddress_UNIQUE` (`IPAddress` ASC),
  CONSTRAINT `FK_Routers_Models`
    FOREIGN KEY (`modelId`)
    REFERENCES `ac1`.`RoutersModels` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`DSLAMs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`DSLAMs` ;

CREATE TABLE IF NOT EXISTS `ac1`.`DSLAMs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `siteId` INT NOT NULL,
  `name` VARCHAR(10) NOT NULL,
  `IPAddress` VARCHAR(39) NOT NULL,
  `routerId` INT NOT NULL,
  `modelId` INT NOT NULL,
  `remarks` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `broadband_UNIQUE` (`siteId` ASC, `name` ASC),
  INDEX `FK_BroadbandDSLAMs_1_idx` (`modelId` ASC),
  INDEX `FK_DSLAMs_1_idx` (`routerId` ASC),
  CONSTRAINT `FK_DSLAMs_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DSLAMs_Models`
    FOREIGN KEY (`modelId`)
    REFERENCES `ac1`.`DSLAMsModels` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DSLAMs_Routers`
    FOREIGN KEY (`routerId`)
    REFERENCES `ac1`.`Routers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`DSLAMsBoardsModels`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`DSLAMsBoardsModels` ;

CREATE TABLE IF NOT EXISTS `ac1`.`DSLAMsBoardsModels` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `manufacturerId` INT NOT NULL,
  `ports` INT NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_BoardsModels_1_idx` (`manufacturerId` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  CONSTRAINT `FK_BoardsModels_Manufacturers`
    FOREIGN KEY (`manufacturerId`)
    REFERENCES `ac1`.`DSLAMsManufacturers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`DSLAMsBoards`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`DSLAMsBoards` ;

CREATE TABLE IF NOT EXISTS `ac1`.`DSLAMsBoards` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dslamId` INT NOT NULL,
  `modelId` INT NOT NULL,
  `slot` INT NOT NULL,
  `remarks` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_BroadbandBoards_1_idx` (`dslamId` ASC),
  INDEX `FK_BroadbandBoards_Models_idx` (`modelId` ASC),
  UNIQUE INDEX `dslamI_slot_UNIQUE` (`dslamId` ASC, `slot` ASC),
  CONSTRAINT `FK_Boards_DSLAMs`
    FOREIGN KEY (`dslamId`)
    REFERENCES `ac1`.`DSLAMs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_DSLAMsBoards_Models`
    FOREIGN KEY (`modelId`)
    REFERENCES `ac1`.`DSLAMsBoardsModels` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`BroadbandPorts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`BroadbandPorts` ;

CREATE TABLE IF NOT EXISTS `ac1`.`BroadbandPorts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dslamId` INT NULL,
  `boardId` INT NULL,
  `port` INT NOT NULL,
  `available` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `FK_BroadbandPorts_1_idx` (`boardId` ASC),
  UNIQUE INDEX `BroadbandPort_UNIQUE` (`boardId` ASC, `port` ASC),
  UNIQUE INDEX `board_port_UNIQUE` (`boardId` ASC, `port` ASC),
  INDEX `fk_BroadbandPorts_DSLAMs_idx` (`dslamId` ASC),
  CONSTRAINT `FK_BroadbandPorts_DSLAMsBoards`
    FOREIGN KEY (`boardId`)
    REFERENCES `ac1`.`DSLAMsBoards` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BroadbandPorts_DSLAMs`
    FOREIGN KEY (`dslamId`)
    REFERENCES `ac1`.`DSLAMs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Broadband`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Broadband` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Broadband` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `portId` INT NULL,
  `modemId` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_BroadbandUsers_1_idx1` (`modemId` ASC),
  INDEX `FK_BroadbandUsers_1_idx` (`portId` ASC),
  CONSTRAINT `FK_Broadband_ModemsModels`
    FOREIGN KEY (`modemId`)
    REFERENCES `ac1`.`ModemsModels` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Broadband_Ports`
    FOREIGN KEY (`portId`)
    REFERENCES `ac1`.`BroadbandPorts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`StreetFrames`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`StreetFrames` ;

CREATE TABLE IF NOT EXISTS `ac1`.`StreetFrames` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `siteId` INT NOT NULL,
  `name` CHAR NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `frame_UNIQUE` (`name` ASC, `siteId` ASC),
  INDEX `FK_StreetFrames_1_idx` (`siteId` ASC),
  CONSTRAINT `FK_StreetFrames_Sites`
    FOREIGN KEY (`siteId`)
    REFERENCES `ac1`.`Sites` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`StreetCables`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`StreetCables` ;

CREATE TABLE IF NOT EXISTS `ac1`.`StreetCables` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` CHAR NOT NULL,
  `frameId` INT NOT NULL,
  `pairs` INT NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC, `frameId` ASC),
  INDEX `FK_StreetCables_1_idx` (`frameId` ASC),
  CONSTRAINT `FK_StreetCables_Frames`
    FOREIGN KEY (`frameId`)
    REFERENCES `ac1`.`StreetFrames` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`StreetPairs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`StreetPairs` ;

CREATE TABLE IF NOT EXISTS `ac1`.`StreetPairs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cableId` INT NOT NULL,
  `pair` INT NOT NULL,
  `available` TINYINT(1) NOT NULL DEFAULT 1,
  `remarks` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_Pairs_1_idx` (`cableId` ASC),
  UNIQUE INDEX `pair_UNIQUE` (`pair` ASC, `cableId` ASC),
  CONSTRAINT `FK_Pairs_Cables`
    FOREIGN KEY (`cableId`)
    REFERENCES `ac1`.`StreetCables` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Wiring`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Wiring` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Wiring` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `distributorId` INT NOT NULL,
  `streetPairId` INT NOT NULL,
  `secondStreetPairId` INT NULL,
  `broadbandId` INT NULL,
  `remarks` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_Wiring_1_idx1` (`distributorId` ASC),
  INDEX `FK_Wiring_1_idx2` (`broadbandId` ASC),
  INDEX `FK_Wiring_1_idx3` (`streetPairId` ASC),
  INDEX `FK_Wiring_Pairs2_idx` (`secondStreetPairId` ASC),
  CONSTRAINT `FK_Wiring_Distributor`
    FOREIGN KEY (`distributorId`)
    REFERENCES `ac1`.`Distributor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Wiring_Broadband`
    FOREIGN KEY (`broadbandId`)
    REFERENCES `ac1`.`Broadband` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Wiring_StreetPairs`
    FOREIGN KEY (`streetPairId`)
    REFERENCES `ac1`.`StreetPairs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Wiring_StreetPairs2`
    FOREIGN KEY (`secondStreetPairId`)
    REFERENCES `ac1`.`StreetPairs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Features`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Features` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Features` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`OwnNumeration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`OwnNumeration` ;

CREATE TABLE IF NOT EXISTS `ac1`.`OwnNumeration` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `officeCodeId` INT NOT NULL,
  `number` VARCHAR(4) NULL,
  PRIMARY KEY (`id`))
ENGINE = MEMORY;


-- -----------------------------------------------------
-- Table `ac1`.`OwnPhoneNumbers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`OwnPhoneNumbers` ;

CREATE TABLE IF NOT EXISTS `ac1`.`OwnPhoneNumbers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `officeCodeId` INT NOT NULL,
  `number` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = MEMORY;


-- -----------------------------------------------------
-- Table `ac1`.`SubscribersRecord`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscribersRecord` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscribersRecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `addressId` INT NOT NULL,
  `distributorId` INT NOT NULL,
  `ownerId` INT NULL,
  `assigneeId` INT NULL,
  `remarks` VARCHAR(250) NULL,
  `userId` INT NOT NULL,
  `time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_SubscribersRecord_Addresses_idx` (`addressId` ASC),
  INDEX `fk_SubscribersRecord_Distributor_idx` (`distributorId` ASC),
  INDEX `fk_SubscribersRecord_People_Owners_idx` (`ownerId` ASC),
  INDEX `fk_SubscribersRecord_People_Assignees_idx` (`assigneeId` ASC),
  INDEX `fk_SubscribersRecord_Users_idx` (`userId` ASC),
  CONSTRAINT `fk_SubscribersRecord_Addresses`
    FOREIGN KEY (`addressId`)
    REFERENCES `ac1`.`Addresses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersRecord_Distributor`
    FOREIGN KEY (`distributorId`)
    REFERENCES `ac1`.`Distributor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersRecord_People_Owners`
    FOREIGN KEY (`ownerId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersRecord_People_Assignees`
    FOREIGN KEY (`assigneeId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersRecord_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`BroadbandRecord`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`BroadbandRecord` ;

CREATE TABLE IF NOT EXISTS `ac1`.`BroadbandRecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `portId` INT NULL,
  `modemId` INT NULL,
  `userId` INT NOT NULL,
  `time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_BroadbandRecord_1_idx1` (`portId` ASC),
  INDEX `fk_BroadbandRecord_1_idx2` (`modemId` ASC),
  INDEX `fk_BroadbandRecord_1_idx3` (`userId` ASC),
  CONSTRAINT `fk_BroadbandRecord_BroadbandPorts`
    FOREIGN KEY (`portId`)
    REFERENCES `ac1`.`BroadbandPorts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BroadbandRecord_ModemsModels`
    FOREIGN KEY (`modemId`)
    REFERENCES `ac1`.`ModemsModels` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BroadbandRecord_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`WiringRecord`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`WiringRecord` ;

CREATE TABLE IF NOT EXISTS `ac1`.`WiringRecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `distributorId` INT NOT NULL,
  `streetPairId` INT NULL,
  `secondStreetPairId` INT NULL,
  `broadbandRecordId` INT NULL,
  `remarks` VARCHAR(100) NULL,
  `userId` INT NOT NULL,
  `time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_WiringRecord_1_idx` (`streetPairId` ASC),
  INDEX `fk_WiringRecord_Distributor_idx` (`distributorId` ASC),
  INDEX `fk_WiringRecord_1_idx2` (`userId` ASC),
  INDEX `fk_WiringRecord_BroadbandRecord_idx` (`broadbandRecordId` ASC),
  INDEX `fk_WiringRecord_StreetPairs2_idx` (`secondStreetPairId` ASC),
  CONSTRAINT `fk_WiringRecord_Distributor`
    FOREIGN KEY (`distributorId`)
    REFERENCES `ac1`.`Distributor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WiringRecord_StreetPairs`
    FOREIGN KEY (`streetPairId`)
    REFERENCES `ac1`.`StreetPairs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WiringRecord_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WiringRecord_BroadbandRecord`
    FOREIGN KEY (`broadbandRecordId`)
    REFERENCES `ac1`.`BroadbandRecord` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_WiringRecord_StreetPairs2`
    FOREIGN KEY (`secondStreetPairId`)
    REFERENCES `ac1`.`StreetPairs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SigmaDTIs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SigmaDTIs` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SigmaDTIs` (
  `eluId` INT NOT NULL,
  `dti` INT NOT NULL,
  UNIQUE INDEX `elu_dti_UNIQUE` (`eluId` ASC, `dti` ASC),
  CONSTRAINT `FK_DTIs_SigmaELUs`
    FOREIGN KEY (`eluId`)
    REFERENCES `ac1`.`NEAX61SigmaELUs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscriberServices`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscriberServices` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscriberServices` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscribersDataServices`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscribersDataServices` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscribersDataServices` (
  `dataId` INT NOT NULL,
  `serviceId` INT NOT NULL,
  INDEX `fk_SubscribersDataServices_1_idx` (`dataId` ASC),
  INDEX `fk_SubscribersDataServices_Services_idx` (`serviceId` ASC),
  CONSTRAINT `fk_SubscribersDataServices_Data`
    FOREIGN KEY (`dataId`)
    REFERENCES `ac1`.`SubscribersData` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataServices_Services`
    FOREIGN KEY (`serviceId`)
    REFERENCES `ac1`.`SubscriberServices` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`SubscribersDataRecord`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`SubscribersDataRecord` ;

CREATE TABLE IF NOT EXISTS `ac1`.`SubscribersDataRecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dataId` INT NOT NULL,
  `lineClassId` INT NOT NULL,
  `restrictionId` INT NOT NULL,
  `services` VARCHAR(250) NULL,
  `stateId` INT NOT NULL,
  `broadbandStateId` INT NULL,
  `information` VARCHAR(250) NULL,
  `userId` INT NOT NULL,
  `time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_SubscribersDataRecord_1_idx` (`dataId` ASC),
  INDEX `fk_SubscribersDataRecord_1_idx1` (`lineClassId` ASC),
  INDEX `fk_SubscribersDataRecord_1_idx2` (`stateId` ASC),
  INDEX `fk_SubscribersDataRecord_Restrictions_idx` (`restrictionId` ASC),
  INDEX `fk_SubscribersDataRecord_Users_idx` (`userId` ASC),
  INDEX `fk_SubscribersDataRecord_BroadbandStates_idx` (`broadbandStateId` ASC),
  CONSTRAINT `fk_SubscribersDataRecord_Data`
    FOREIGN KEY (`dataId`)
    REFERENCES `ac1`.`SubscribersData` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataRecord_LineClasses`
    FOREIGN KEY (`lineClassId`)
    REFERENCES `ac1`.`SubscriberLineClasses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataRecord_States`
    FOREIGN KEY (`stateId`)
    REFERENCES `ac1`.`SubscriberStates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataRecord_Restrictions`
    FOREIGN KEY (`restrictionId`)
    REFERENCES `ac1`.`SubscriberRestrictions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataRecord_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SubscribersDataRecord_BroadbandStates`
    FOREIGN KEY (`broadbandStateId`)
    REFERENCES `ac1`.`SubscriberBroadbandStates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`RepairsCompanies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`RepairsCompanies` ;

CREATE TABLE IF NOT EXISTS `ac1`.`RepairsCompanies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Repairmen`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Repairmen` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Repairmen` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `personId` INT NOT NULL,
  `repairsCompanyId` INT NOT NULL,
  `services` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Repairman_People_idx` (`personId` ASC),
  INDEX `fk_Repairman_Companies_idx` (`repairsCompanyId` ASC),
  UNIQUE INDEX `personId_UNIQUE` (`personId` ASC),
  CONSTRAINT `fk_Repairman_People`
    FOREIGN KEY (`personId`)
    REFERENCES `ac1`.`People` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Repairman_RepairsCompanies`
    FOREIGN KEY (`repairsCompanyId`)
    REFERENCES `ac1`.`RepairsCompanies` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`RepairingTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`RepairingTypes` ;

CREATE TABLE IF NOT EXISTS `ac1`.`RepairingTypes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`ServiceOrders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`ServiceOrders` ;

CREATE TABLE IF NOT EXISTS `ac1`.`ServiceOrders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `subscriberId` INT NOT NULL,
  `repairingTypeId` INT NOT NULL,
  `remarks` VARCHAR(250) NULL,
  `contact` VARCHAR(45) NULL,
  `creationTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `userId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Repairings_RepairingTypes_idx` (`repairingTypeId` ASC),
  INDEX `fk_ServiceOrders_Subscribers_idx` (`subscriberId` ASC),
  INDEX `fk_ServiceOrders_Users_idx` (`userId` ASC),
  CONSTRAINT `fk_ServiceOrders_Subscribers`
    FOREIGN KEY (`subscriberId`)
    REFERENCES `ac1`.`Subscribers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ServiceOrders_RepairingTypes`
    FOREIGN KEY (`repairingTypeId`)
    REFERENCES `ac1`.`RepairingTypes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ServiceOrders_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`Repairings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`Repairings` ;

CREATE TABLE IF NOT EXISTS `ac1`.`Repairings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `serviceOrderId` INT NOT NULL,
  `repairmanId` INT NOT NULL,
  `repairmanRemarks` VARCHAR(100) NULL,
  `assignmentTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `repairedDate` DATE NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_RepairingsRepairmen_Repairings_idx` (`serviceOrderId` ASC),
  INDEX `fk_RepairingsRepairmen_Repaimen_idx` (`repairmanId` ASC),
  CONSTRAINT `fk_Repairings_ServiceOrders`
    FOREIGN KEY (`serviceOrderId`)
    REFERENCES `ac1`.`ServiceOrders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Repairings_Repaimen`
    FOREIGN KEY (`repairmanId`)
    REFERENCES `ac1`.`Repairmen` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`RepairingChecks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`RepairingChecks` ;

CREATE TABLE IF NOT EXISTS `ac1`.`RepairingChecks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `repairingId` INT NOT NULL,
  `userId` INT NOT NULL,
  `remarks` VARCHAR(100) NULL,
  `approved` TINYINT(1) NULL,
  `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_RepairingChecks_Users_idx` (`userId` ASC),
  INDEX `fk_RepairingChecks_RepairingsRepairmen_idx` (`repairingId` ASC),
  CONSTRAINT `fk_RepairingChecks_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RepairingChecks_Repairings`
    FOREIGN KEY (`repairingId`)
    REFERENCES `ac1`.`Repairings` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ac1`.`UsersRoles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ac1`.`UsersRoles` ;

CREATE TABLE IF NOT EXISTS `ac1`.`UsersRoles` (
  `userId` INT NOT NULL,
  `roleId` INT NOT NULL,
  UNIQUE INDEX `user_role_UNIQUE` (`userId` ASC, `roleId` ASC),
  INDEX `fk_UsersRoles_Roles_idx` (`roleId` ASC),
  CONSTRAINT `fk_UsersRoles_Users`
    FOREIGN KEY (`userId`)
    REFERENCES `ac1`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UsersRoles_Roles`
    FOREIGN KEY (`roleId`)
    REFERENCES `ac1`.`Roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
