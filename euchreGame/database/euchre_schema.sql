CREATE DATABASE  IF NOT EXISTS `euchre` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `euchre`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: euchre
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `GameStates`
--

DROP TABLE IF EXISTS `GameStates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GameStates` (
  `GameStateUID` varchar(10) NOT NULL,
  `Player1` int DEFAULT NULL,
  `Player2` int DEFAULT NULL,
  `Player3` int DEFAULT NULL,
  `Player4` int DEFAULT NULL,
  `GameStartTime` datetime DEFAULT NULL,
  `GameSaveTime` datetime DEFAULT NULL,
  `Team1Score` int DEFAULT '0',
  `Team2Score` int DEFAULT '0',
  `Team1Tricks` int DEFAULT '0',
  `Team2Tricks` int DEFAULT '0',
  `Player1Hand` varchar(19) DEFAULT NULL,
  `Player2Hand` varchar(19) DEFAULT NULL,
  `Player3Hand` varchar(19) DEFAULT NULL,
  `Player4Hand` varchar(19) DEFAULT NULL,
  `Dealer` int DEFAULT '-1',
  `LeadingPlayer` int DEFAULT '-1',
  `Trump` varchar(1) DEFAULT NULL,
  `CallingTeam` int DEFAULT '-1',
  `PlayerAlone` int DEFAULT '-1',
  PRIMARY KEY (`GameStateUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Games`
--

DROP TABLE IF EXISTS `Games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Games` (
  `GameUID` int NOT NULL AUTO_INCREMENT,
  `Player1` int DEFAULT NULL,
  `Player2` int DEFAULT NULL,
  `Player3` int DEFAULT NULL,
  `Player4` int DEFAULT NULL,
  `GameStartTime` datetime DEFAULT NULL,
  `GameEndTime` datetime DEFAULT NULL,
  `Team1Score` int DEFAULT NULL,
  `Team2Score` int DEFAULT NULL,
  PRIMARY KEY (`GameUID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `UserUID` int NOT NULL AUTO_INCREMENT,
  `UserName` varchar(45) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `AccountCreation` datetime DEFAULT NULL,
  `LastLogin` datetime DEFAULT NULL,
  `Settings` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`UserUID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into Users (`UserUID`, `UserName`, `EmailAddress`, `AccountCreation`, `LastLogin`, `Settings`) values (-1, 'EuchreBot', 'euchrebot@euchre.bot', CURRENT_TIME(), CURRENT_TIME(),'');
insert into Users (`UserUID`, `UserName`, `EmailAddress`, `AccountCreation`, `LastLogin`, `Settings`) values (0, 'guestUser', 'guest@euchre.guest', CURRENT_TIME(), CURRENT_TIME(),'');
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
