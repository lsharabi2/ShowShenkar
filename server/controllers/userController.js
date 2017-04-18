var mongoose = require ('../database');
var user = require ('../schemes/user');
var crypto = require ('../crypto');


exports.getAllUsers = function (req, res) {

    user.find ({}).
    where('user').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.auth = function (req, res) {
	
	if (req.body.userName == undefined || req.body.password == undefined ) {
		res.send ("missing parameters");
	}
	else {
	var userName = req.body.userName;
	var query = user.findOne().where ('userName', userName);
	
	
	 query.exec (function (err,doc) {
	 		try {	
	 	if (doc.password == req.body.password) {
	 		if (doc.role == "admin"){
	 			
	 		res.cookie ("shenkarShowSession", crypto.hashMake (doc.email),  { expires: new Date(Date.now() + 900000), domain: 'http://www.talco.co', sameSite: false ,path: 'http://www.talco.co'});
	 		res.cookie ("shenkarShowUserId", doc._id,  { expires: new Date(Date.now() + 900000), path: 'http://www.talco.co'});
	 		res.writeHead(302, {Location: 'http://www.talco.co'});
	 		res.end ();
	 		
	 		}
	 		
	 		else if 
	 		(doc.role == "institute manager"){
	 			
	 		res.cookie ("shenkarShowSession", crypto.hashMake (doc.email),  { expires: new Date(Date.now() + 900000), path: '/institute'});
	 		res.cookie ("shenkarShowUserId", doc._id,  { expires: new Date(Date.now() + 900000), path: '/institute'});
	 		res.writeHead(302, {Location: '/institute'});
	 		res.end ();
	 		
	 		}
	 		
	 		else if 
	 		(doc.role == "department manager"){
	 			
	 		res.cookie ("shenkarShowSession", crypto.hashMake (doc.email),  { expires: new Date(Date.now() + 900000), path: '/department'});
	 		res.cookie ("shenkarShowUserId", doc._id,  { expires: new Date(Date.now() + 900000), path: '/department'});
	 		res.writeHead(302, {Location: '/department'});
	 		res.end ();
	 		
	 		}
	 		
	 		
	 		else if (doc.role == "student"){
	 		res.cookie ("shenkarShowSession", crypto.hashMake (doc.email),  { expires: new Date(Date.now() + 900000), path: '/student'});
	 		res.cookie ("shenkarShowUserId", doc._id,  { expires: new Date(Date.now() + 900000), path: '/student'});
	 		res.writeHead(302, {Location: '/student'});
	 		res.end ();
	 		}
	 		
	 		else {
	 			res.cookie ("shenkar-show", "guest",  { expires: new Date(Date.now() + 900000), httpOnly: true });
	 		}
	 		
	 	}
	 	else {
	 		res.send ("fail");
	 	}
	 	}
	 	catch (exception) {
 	console.log (exception); 
 	res.send (false);
 }
	 	
	 });
	}
 
}


exports.getUserById  = function (req, res) {
    var id = req.params.userId;
    console.log ('User ID = ' + id);

    user.find ({_id : id}).
    where('user').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.getUserByName  = function (req, res) {
    var name = req.params.userName;
    console.log ('User name = ' + name);

    user.find ({name : name}).
    where('user').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.createUser = function (request, response) {

    user.find({userName : request.body.userName },function(err, doc){
       if (doc.length){
            console.log("user already exists");
            response.send ("user already exists");
          }else{
          	
             var newUser = new user({
             	role : request.body.role,
             	userName :request.body.userName,
                name :request.body.name,
                password :request.body.password,
                email :request.body.email,
                department :request.body.department,
                institute :request.body.institute
              });
              
           try {
              newUser.save(function(error, result) {
                if (error) {
                  console.error(error);
                } else {
                  console.log("Saved document : " + doc);
                  response.send (true);
                };
              });
              }
               catch (exception) {
 	console.log (exception); 
 	response.send (false);
 }
            }
          });    




}

exports.updateUser = function (request, response) {

	 var query = user.findOne().where ('userName', request.body.userName);
	 
	 query.exec (function (err,doc) {
	
	try {	
	 	var query = doc.update ({
	 		$set: {
	 			role : request.body.role,
             	userName :request.body.userName,
                name :request.body.name,
                password :request.body.password,
                email :request.body.email,
                department :request.body.department,
                institute :request.body.institute
	 		}	 		
	 	});
 	
 	 	query.exec (function (err, results) {
 		console.log ("\n Resulets Object : " + JSON.stringify (results));
 	});
 	      	
			console.log("Updated Doc : " + doc);
            response.send (true);
            
 	}
 catch (exception) {
 	console.log (exception); 
 	response.send (false);
 }

 });
}


exports.deleteUser = function (request, response) {

	var query = user.findOne().where ('userName', request.body.userName);
	
	 query.exec (function (err,doc) {
	 		try {	
	 	var query = doc.remove (function (err, deletedDoc) {
	 		user.findOne ({userName: request.body.userName}, function (err, doc) {
	 			console.log("Removed doc : " + doc);
                  response.send (true);
	 		})
	 	});
	 	
	 	}
	 	catch (exception) {
 	console.log (exception); 
 	response.send (false);
 }
	 	
	 });

}

