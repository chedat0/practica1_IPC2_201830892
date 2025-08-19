/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: eventosdb
-- ------------------------------------------------------
-- Server version	11.7.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `actividad`
--

DROP TABLE IF EXISTS `actividad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividad` (
  `codigo_actividad` varchar(15) NOT NULL,
  `codigo_evento` varchar(15) NOT NULL,
  `tipo_actividad` enum('CHARLA','TALLER','DEBATE','OTRA') NOT NULL,
  `titulo_actividad` varchar(200) NOT NULL,
  `correo_encargado` varchar(100) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `cupo_maximo` int(11) NOT NULL CHECK (`cupo_maximo` > 0),
  PRIMARY KEY (`codigo_actividad`),
  KEY `fk_cod_evento` (`codigo_evento`),
  KEY `fk_correo_actividad` (`correo_encargado`),
  CONSTRAINT `fk_cod_evento` FOREIGN KEY (`codigo_evento`) REFERENCES `evento` (`codigo_evento`),
  CONSTRAINT `fk_correo_actividad` FOREIGN KEY (`correo_encargado`) REFERENCES `participante` (`correo_electronico`),
  CONSTRAINT `CONSTRAINT_1` CHECK (`hora_fin` > `hora_inicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad`
--

LOCK TABLES `actividad` WRITE;
/*!40000 ALTER TABLE `actividad` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id_asistencia` int(11) NOT NULL AUTO_INCREMENT,
  `correo_participante` varchar(100) NOT NULL,
  `codigo_actividad` varchar(15) NOT NULL,
  `asistio` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id_asistencia`),
  UNIQUE KEY `unique_asistencia` (`correo_participante`,`codigo_actividad`),
  KEY `fk_cod_actividad` (`codigo_actividad`),
  CONSTRAINT `fk_cod_actividad` FOREIGN KEY (`codigo_actividad`) REFERENCES `actividad` (`codigo_actividad`),
  CONSTRAINT `fk_correo_asistencia` FOREIGN KEY (`correo_participante`) REFERENCES `participante` (`correo_electronico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificado`
--

DROP TABLE IF EXISTS `certificado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificado` (
  `id_certificado` int(11) NOT NULL AUTO_INCREMENT,
  `correo_participante` varchar(100) NOT NULL,
  `codigo_evento` varchar(15) NOT NULL,
  `fecha_emision` timestamp NULL DEFAULT current_timestamp(),
  `ruta_archivo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_certificado`),
  UNIQUE KEY `unique_certificado` (`correo_participante`,`codigo_evento`),
  KEY `fk_codi_evento` (`codigo_evento`),
  CONSTRAINT `fk_codi_evento` FOREIGN KEY (`codigo_evento`) REFERENCES `evento` (`codigo_evento`),
  CONSTRAINT `fk_correo_certificado` FOREIGN KEY (`correo_participante`) REFERENCES `participante` (`correo_electronico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificado`
--

LOCK TABLES `certificado` WRITE;
/*!40000 ALTER TABLE `certificado` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento` (
  `codigo_evento` varchar(15) NOT NULL,
  `fecha` date NOT NULL,
  `tipo_evento` enum('CHARLA','CONGRESO','TALLER','DEBATE') NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `ubicacion` varchar(200) NOT NULL,
  `cupo_maximo` int(11) NOT NULL CHECK (`cupo_maximo` > 0),
  `costo_inscripcion` decimal(10,2) NOT NULL,
  PRIMARY KEY (`codigo_evento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evento`
--

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscripcion`
--

DROP TABLE IF EXISTS `inscripcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `inscripcion` (
  `id_inscripcion` int(11) NOT NULL AUTO_INCREMENT,
  `correo_participante` varchar(100) NOT NULL,
  `codigo_evento` varchar(15) NOT NULL,
  `tipo_inscripcion` enum('ASISTENTE','CONFERENCISTA','TALLERISTA','OTRO') NOT NULL,
  `validada` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id_inscripcion`),
  KEY `fk_correo_validacion` (`correo_participante`),
  KEY `fk_codigo_evento` (`codigo_evento`),
  CONSTRAINT `fk_codigo_evento` FOREIGN KEY (`codigo_evento`) REFERENCES `evento` (`codigo_evento`),
  CONSTRAINT `fk_correo_validacion` FOREIGN KEY (`correo_participante`) REFERENCES `participante` (`correo_electronico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscripcion`
--

LOCK TABLES `inscripcion` WRITE;
/*!40000 ALTER TABLE `inscripcion` DISABLE KEYS */;
/*!40000 ALTER TABLE `inscripcion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logprocesamiento`
--

DROP TABLE IF EXISTS `logprocesamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `logprocesamiento` (
  `id_log` int(11) NOT NULL AUTO_INCREMENT,
  `instruccion` text NOT NULL,
  `resultado` enum('EXITOSO','ERROR') NOT NULL,
  `mensaje` varchar(500) DEFAULT NULL,
  `fecha_procesamiento` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_log`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logprocesamiento`
--

LOCK TABLES `logprocesamiento` WRITE;
/*!40000 ALTER TABLE `logprocesamiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `logprocesamiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `id_inscripcion` int(11) NOT NULL,
  `metodo_pago` enum('EFECTIVO','TRANFERENCIA','TARJETA') NOT NULL,
  `monto` decimal(10,2) NOT NULL CHECK (`monto` > 0),
  `estado_pago` enum('PENDIENTE','COMPLETADO','RECHAZADO') DEFAULT 'COMPLETADO',
  PRIMARY KEY (`id_pago`),
  KEY `fk_inscripcion` (`id_inscripcion`),
  CONSTRAINT `fk_inscripcion` FOREIGN KEY (`id_inscripcion`) REFERENCES `inscripcion` (`id_inscripcion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participante`
--

DROP TABLE IF EXISTS `participante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `participante` (
  `correo_electronico` varchar(100) NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  `tipo_participante` enum('ESTUDIANTE','PROFESIONAL','INVITADO') NOT NULL,
  `institucion_de_procedencia` varchar(150) NOT NULL,
  PRIMARY KEY (`correo_electronico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participante`
--

LOCK TABLES `participante` WRITE;
/*!40000 ALTER TABLE `participante` DISABLE KEYS */;
/*!40000 ALTER TABLE `participante` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-08-19  9:26:07
