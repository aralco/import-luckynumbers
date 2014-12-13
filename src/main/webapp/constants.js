'use strict';

/* Constants */

var URL = {};
URL.host = "http://localhost";
URL.port = "7001";
URL.dirFiles ="http://localhost:9000/ftp/in/";

luckynumbersApp.constant('USER_ROLES', {
	'all': '*',
	'admin': 'ROLE_ADMIN',
	'user': 'ROLE_USER'
});
