var mongoose = require ('../database');
var user = require ('../schemes/user');
var crypto = require ('../crypto');

exports.authCookies =    function (session, userId, callback ) {
	if (userId != undefined) {
		var query = user.findOne().where ('_id', userId);
		
		 query.exec (function (err,doc) {
	 		try {	
	 	if (crypto.hashCheck (doc.email , session)) {
	 		
	 		console.log ("Role : " + doc.role);
	 		callback (doc.role);
	 	}
	 	else {
	 		callback ("fail");
	 	}
	 	}
	 	catch (exception) {
 	console.log (exception); 
 	return (false);
 }
	 	
	 });
	}
}
