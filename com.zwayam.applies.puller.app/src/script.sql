--18 Jul

CREATE TABLE IF NOT EXISTS `pull_applies_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `apiBaseUrl` varchar(255) DEFAULT NULL,
  `authParams` varchar(255) DEFAULT NULL,
  `bodyDataType` varchar(255) DEFAULT NULL,
  `bodyParams` text,
  `frequency` varchar(255) DEFAULT NULL,
  `headerParams` text,
  `httpMethodType` varchar(255) DEFAULT NULL,
  `jobAppMapTemplate` text,
  `lastTime` datetime DEFAULT NULL,
  `nextTime` datetime DEFAULT NULL,
  `queries` text,
  `urlParams` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;






CREATE TABLE IF NOT EXISTS `auth_api_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `apiBaseUrl` varchar(255) DEFAULT NULL,
  `bodyDataType` varchar(255) DEFAULT NULL,
  `bodyParams` text,
  `headerParams` text,
  `httpMethodType` varchar(255) DEFAULT NULL,
  `urlParams` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `auth_api_config`
--



INSERT INTO `pull_applies_config` (`id`, `name`, `apiBaseUrl`, `authParams`, `bodyDataType`, `bodyParams`, `frequency`, `headerParams`, `httpMethodType`, `jobAppMapTemplate`, `lastTime`, `nextTime`, `queries`, `urlParams`) VALUES
(1, 'Hiree', 'https://api.quikr.com/hiree/api/getExternalJobApplications.rest?', ' {"Appid": "890",\n    "Secret": "3404e61c98978a1c24d254eb513b3a16",\n    "email": "roshan@zwayam.com"}', NULL, NULL, '120', '{"cache-control": "no-cache",\n"X-Quikr-App-Id":"Appid",\n"X-Quikr-Signature-v2": "token",\n"X-Quikr-Token-Id": "tokenId"}', 'GET', '{\n  "phoneNo": "phone",\n  "emailId": "email",\n  "firstName": "name",\n  "middleName": "",\n  "lastName": "",\n  "jobId": "",\n  "companyId": "",\n  "fileName": "",\n  "resumeURL": "resumeURL",\n  "experience": "experience",\n  "expectedSalary": "",\n  "location": "currentLocation",\n  "dateOfBirth": ""\n}', '2017-07-24 19:34:23', '2017-07-19 22:00:00', 'SELECT id as ''jobId'',company_id as ''companyId'' FROM `c3_job` WHERE id in (13989,20471,19184,19301,20480,22625,22678,23098,23877,23879,24011,24012,24020,23049,23276,23277,23279,23280,23477,14424,22259,23960,23965,24061,20726,20727,20729,21482,21483,1288)', '{"jobId":"","companyId":""}');

INSERT INTO `auth_api_config` (`id`, `name`, `apiBaseUrl`, `bodyDataType`, `bodyParams`, `headerParams`, `httpMethodType`, `urlParams`) VALUES
(1, 'Hiree', 'https://api.quikr.com/app/auth/access_token', NULL, '{"signature":"",\n"appId":""}', '{"Content-Type":"application/JSON"}', 'POST', NULL);


