CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(40) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `last_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);
