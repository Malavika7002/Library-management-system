-- MySQL dump 10.13  Distrib 26.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: library_db
-- ------------------------------------------------------
-- Server version	26.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2bb64c7c-c56f-11f1-81f1-6cf6da133679:1-31';

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('ADMIN01','Library Admin','admin1@library.com','$2a$12$UeEcnlKwsgV98fJGOpWzz.fN0W5Y08NJJhzbvchvOhwYgJ1cYJ/kG'),('ADMIN02','Head Librarian','admin2@library.com','$2a$12$HpdpgK68GeQ/5BGZdUQcUOKhjt1X3fKEx5GRc5xfP/1vBqLYJ9hHq'),('ADMIN03','Assistant Admin','admin3@library.com','$2a$12$qAykqhq082qsR0UfT.RP4uk8hCNcQvUPoeBbdvxEFVxWTiYkzyah2');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `author` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `item_type` enum('Book','Magazine','Newspaper') NOT NULL DEFAULT 'Book',
  `total_copies` int NOT NULL DEFAULT '1',
  `available_copies` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Introduction to Algorithms','Thomas H. Cormen','Computer Science','Book',5,5),(2,'Database System Concepts','Abraham Silberschatz','Computer Science','Book',4,4),(3,'Java: The Complete Reference','Herbert Schildt','Programming','Book',6,6),(4,'Operating System Concepts','Abraham Silberschatz','Computer Science','Book',4,4),(5,'Computer Networks','Andrew S. Tanenbaum','Networking','Book',3,3),(6,'Engineering Mathematics','B. S. Grewal','Mathematics','Book',8,8),(7,'Wings of Fire','A. P. J. Abdul Kalam','Biography','Book',5,5),(8,'Digital Image Processing','Rafael C. Gonzalez','Electronics','Book',2,2),(9,'Software Engineering','Ian Sommerville','Computer Science','Book',4,4),(10,'The Hindu','The Hindu Group','General','Newspaper',2,2),(11,'Frontline','The Hindu Group','Current Affairs','Magazine',3,3),(12,'Competition Success Review','CSR Publications','Competitive Exam','Magazine',3,3);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libraryalerts`
--

DROP TABLE IF EXISTS `libraryalerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libraryalerts` (
  `admission_no` varchar(30) NOT NULL,
  `book_id` int NOT NULL,
  `return_status` enum('Overdue','Recently Returned','Today') NOT NULL,
  KEY `admission_no` (`admission_no`),
  CONSTRAINT `libraryalerts_ibfk_1` FOREIGN KEY (`admission_no`) REFERENCES `students` (`admission_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libraryalerts`
--

LOCK TABLES `libraryalerts` WRITE;
/*!40000 ALTER TABLE `libraryalerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `libraryalerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passwordresets`
--

DROP TABLE IF EXISTS `passwordresets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passwordresets` (
  `email` varchar(100) NOT NULL,
  `otp` varchar(10) NOT NULL,
  `expires_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passwordresets`
--

LOCK TABLES `passwordresets` WRITE;
/*!40000 ALTER TABLE `passwordresets` DISABLE KEYS */;
/*!40000 ALTER TABLE `passwordresets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recentissues`
--

DROP TABLE IF EXISTS `recentissues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recentissues` (
  `issue_id` int NOT NULL AUTO_INCREMENT,
  `admission_no` varchar(30) NOT NULL,
  `book_id` int NOT NULL,
  `issue_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  PRIMARY KEY (`issue_id`),
  KEY `admission_no` (`admission_no`),
  CONSTRAINT `recentissues_ibfk_1` FOREIGN KEY (`admission_no`) REFERENCES `students` (`admission_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recentissues`
--

LOCK TABLES `recentissues` WRITE;
/*!40000 ALTER TABLE `recentissues` DISABLE KEYS */;
/*!40000 ALTER TABLE `recentissues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `admission_no` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`admission_no`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('ADM001','Arun Kumar','9876543210','arun.kumar@example.com','$2a$12$wdnOLH5i0/7TIdMrsO0W9.KGVVddNmdD3T27xzimJTx5msCQ1tMNq'),('ADM002','Priya Sharma','9876543211','priya.sharma@example.com','$2a$12$Cp4Ls64rUYEW4kV9dwupkONM/cTOkZQKarncqTfhWktUqnmxzrxZW'),('ADM003','Karthik Raj','9876543212','karthik.raj@example.com','$2a$12$Lh4PFneR/mZ4qiTEqZn/Puh099rNi1wQJvH4bVg2o6BOZujTbcDtC'),('ADM004','Divya Lakshmi','9876543213','divya.lakshmi@example.com','$2a$12$UMvo3xuiQ5imMUPsTb4JbOej02udNz4KsR3GRdQO.D7yeZZGQhIme'),('ADM005','Mohammed Faiz','9876543214','faiz.m@example.com','$2a$12$qS5A6Ums9dJjjF/BMYQ0p.nYl2vNd4HDU1umKSWxX/BqaVLitPNz2'),('ADM006','Sneha Reddy','9876543215','sneha.reddy@example.com','$2a$12$lmmcCkqrlBo6YFMnydo/ROYxniwoVQ111dkhKhvZqQhVv0PALyMTi'),('ADM007','Vignesh S','9876543216','vignesh.s@example.com','$2a$12$zikYNdVAeqjdKgR0hlogl.WUpwOyOvy6lH.z3gB7AgarDZYHdrakC'),('ADM008','Ananya Nair','9876543217','ananya.nair@example.com','$2a$12$mrc0zQ0e0G1rZMwgZHWjVes34K9fFSUUafpScmHiLyplk1LRZxL3m'),('ADM009','Rahul Verma','9876543218','rahul.verma@example.com','$2a$12$Da6STmI8VyLH5uP3mbhqXeeCUBBq9dBJrzqqjqKOn9RdBGi2OE6ku'),('ADM010','Meena Subramani','9876543219','meena.s@example.com','$2a$12$7SJjFHpA9lWbdhHLQx4zH.1jufEKPSe6inO0omiN3D79EYUFr6B3m');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `admission_no` varchar(30) NOT NULL,
  `book_id` int NOT NULL,
  `issue_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `renewal_count` int NOT NULL DEFAULT '0',
  `fine_amount` decimal(8,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`),
  KEY `admission_no` (`admission_no`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`admission_no`) REFERENCES `students` (`admission_no`),
  CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-12 21:47:13
