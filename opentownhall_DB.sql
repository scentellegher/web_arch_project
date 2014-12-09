-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 09, 2014 at 10:05 AM
-- Server version: 5.6.12
-- PHP Version: 5.5.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `opentownhall_DB`
--
CREATE DATABASE IF NOT EXISTS `opentownhall_DB` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `opentownhall_DB`;

-- --------------------------------------------------------

--
-- Table structure for table `Comment`
--

CREATE TABLE IF NOT EXISTS `Comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `timestamp` datetime NOT NULL,
  `report_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `report_id` (`report_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `Comment`
--

INSERT INTO `Comment` (`id`, `text`, `timestamp`, `report_id`, `user_id`) VALUES
(1, 'Confermo la presenza di spazzatura! CI passo ogni mattina ed e un bel po che e li', '2014-11-26 10:00:00', 4, 4),
(2, 'Quanto bisogna aspettare ancora?', '2014-12-28 18:00:00', 4, 6),
(3, 'Questa mattina abbiamo provveduto alla rimozione! Grazie per la segnalazione!', '2014-12-30 15:19:00', 4, 3),
(6, 'Confermo il problema! Ieri sera era impossibile vedere a un metro!', '2014-12-04 14:41:21', 6, 4),
(8, 'ciccio', '2014-12-05 17:44:58', 10, 4),
(9, 'wwwww', '2014-12-05 17:45:43', 10, 2),
(10, 'afaslfjsaklfjaklsdjlkflsajfklsdajflkdsajfklsdjflskajflkdasjflksdjalkfjadsklfjsdlakfjlkadsjfklsdajflksajflksajfkdsaljflsdakjfklsd', '2014-12-05 17:51:19', 10, 2),
(11, 'solved', '2014-12-07 11:19:10', 10, 3);

-- --------------------------------------------------------

--
-- Table structure for table `Picture`
--

CREATE TABLE IF NOT EXISTS `Picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(200) NOT NULL,
  `report_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `report_id` (`report_id`),
  KEY `user_id` (`user_id`),
  KEY `report_id_2` (`report_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `Picture`
--

INSERT INTO `Picture` (`id`, `path`, `report_id`, `user_id`) VALUES
(2, 'resources/pics/tombino.jpg', 3, 5),
(3, 'resources/pics/mirror.jpg', 2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `Report`
--

CREATE TABLE IF NOT EXISTS `Report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `timestamp` datetime NOT NULL,
  `description` text NOT NULL,
  `address` varchar(100) NOT NULL,
  `coordinates` varchar(50) NOT NULL,
  `status` int(11) DEFAULT '0',
  `id_user` int(11) NOT NULL,
  `id_worker` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_user` (`id_user`),
  KEY `id_worker` (`id_worker`),
  KEY `id_worker_2` (`id_worker`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `Report`
--

INSERT INTO `Report` (`id`, `title`, `timestamp`, `description`, `address`, `coordinates`, `status`, `id_user`, `id_worker`) VALUES
(1, 'Dissesto manto stradale', '2014-11-16 13:00:00', 'Buongiorno,\r\nvolevo segnalare lo stato di dissesto del manto stradale in viale Verona 25, nei pressi del distributore Esso. Tale tratto di strada presenta delle vistose e pericolose buche. Cosapevole della minore disponibilità finanziaria del comune, in attesa dell''asfaltatura totale, chiedo almeno di rimediare provvisoriamente sistemando le buche più pericolose.\r\n', 'Trento, Viale Verona 25', '46.057592, 11.128175', 1, 4, 9),
(2, 'Posizione specchio sbagliata', '2014-11-03 19:35:00', 'Volevo segnalare che a cause delle forti raffiche di vento dei giorni scorsi, lo specchio in via Maccani vicino al distributore Agip non permette di vedere i veicoli in quanto non più in posizione corretta', 'Trento, Via Ezio Maccani 103 ', '46.081636, 11.115896', 0, 4, 9),
(3, 'Tombino scolo acqua piovana', '2014-11-16 17:00:00', 'Buongiorno,\r\nVolevo segnalare che la bocchetta di scolo che dovrebbe convogliare l''acqua piovana nel tombino posto in corrispondenza della fermata dell''autobus davanti al civico 66 di via Caneppele risulta ostruita e questo porta al formarsi ogni volta che piove di una pozza d''acqua che al passaggio delle automobili o dello stesso autobus viene lanciata sulle persone in attesa alla fermata e sui semplici fruitori del marciapiede. ', 'Trento, Via Caneppele 66', '46.096469, 11.104004', -1, 5, NULL),
(4, 'Immondizia lungo la strada', '2014-10-05 09:00:00', 'Segnalo che in via Bartolomeo Malfatti sul marciapiede è presente dell''immondizia da almeno 2 settimane. ', 'Trento, Via Bartolomeo Malfatti 6', '46.061639, 11.125955', 1, 6, 3),
(6, 'Illuminazione Via Valoni', '2014-12-01 13:54:59', 'Volevo segnalare che l''illuminazione che porta dalla facolta di scienze fino in stazione dei treni non funziona da almeno 3 giorni.', 'Povo, Via Valoni 15', '46.0664843, 11.1484482', -1, 8, NULL),
(10, 'ahfadksjfhdaskfshdkj', '2014-12-05 17:44:40', 'asdjfhsakjdhfkja', 'sss, sss sss', '46.0664843, 11.1484482', 1, 4, 3);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `surname` varchar(30) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(50) NOT NULL,
  `birthdate` date NOT NULL,
  `address` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `fiscal_code` varchar(20) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'user',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `name`, `surname`, `email`, `password`, `birthdate`, `address`, `phone`, `fiscal_code`, `role`) VALUES
(2, 'Marco', 'Rossi', 'marco.rossi@tn.it', 'f5888d0bb58d611107e11f7cbc41c97a', '1974-11-04', 'Trento, via Nazionale 12', '3456789990', 'RSSMRC74S04L378Q ', 'admin'),
(3, 'Carlo', 'Verdi', 'carlo.verdi@tn.it', '7d6543d7862a07edf7902086f39b4b9a', '1967-06-17', 'Trento, viale Verona 7', '3298876553', 'VRDCRL67H17L378A', 'worker'),
(4, 'Simone', 'Cent', 'simone.cent@tn.it', '47eb752bac1c08c75e30d9624b3e58b7', '1989-12-29', 'Roncegno, via G.Prati 1', '3451230998', 'CNTSMN89T29L378I ', 'user'),
(5, 'Davide', 'Ghio', 'davide.ghio@tn.it', '446fca5553df49ad9c6348cf1ff71d51', '1989-08-17', 'Trento, Via 3 Novembre 2', '3385657559', 'GHIDVD89M17L378J ', 'user'),
(6, 'Sergio', 'Decri', 'sergio.decri@tn.it', '3bffa4ebdf4874e506c2b12405796aa5', '1989-04-27', 'Trento, Via Lungadige Marco Apuelio', '3412345889', 'SRGDRS89E27E885R', 'user'),
(8, 'Luca', 'Greg', 'luca.greg@tn.it', 'ff377aff39a9345a9cca803fb5c5c081', '1990-06-12', 'Trento, Via S.Margherita', '3453453455', 'LCUGRG90S06L378E', 'user'),
(9, 'Mario', 'Bianchi', 'mario.bianchi@tn.it', 'de2f15d014d40b93578d255e6221fd60', '1976-09-18', 'Trento, Via Verdi', '3213213211', 'BNCMRA76P18L378Y ', 'worker');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Comment`
--
ALTER TABLE `Comment`
  ADD CONSTRAINT `Comment_ibfk_1` FOREIGN KEY (`report_id`) REFERENCES `Report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `Picture`
--
ALTER TABLE `Picture`
  ADD CONSTRAINT `Picture_ibfk_1` FOREIGN KEY (`report_id`) REFERENCES `Report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Picture_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `Report`
--
ALTER TABLE `Report`
  ADD CONSTRAINT `Report_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `Report_ibfk_2` FOREIGN KEY (`id_worker`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
