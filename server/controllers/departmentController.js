var mongoose = require ('../database');
var department = require ('../schemes/department');


exports.getAllDepartments = function (req, res) {

    department.find ({}).
    where('department').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}


exports.getDepartmentById  = function (req, res) {
    var id = req.params.departmentId;
    console.log ('Dep ID = ' + id);

    department.find ({_id : id}).
    where('department').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.getDepartmentById2  = function (depId, callback) {
    var id = depId;
    console.log ('Dep ID = ' + id);

    department.findOne ({_id : id}).
    where('department').ne ('PRIVATE').
    exec (function (err, doc) {
    	console.log ('dasdasd');
        callback (doc);
    });
}

exports.getDepartmentByName  = function (req, res) {
    var name = req.params.name;
    console.log ('department name = ' + department);

    department.find ({name : name}).
    where('department').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}


exports.createDepartment = function (request, response, files) {
	
	var fileKeys = [];
	var logoKey = '';
	if (files['images'] != undefined){
	for (i=0; i< files['images'].length; i++){
	console.log ("FILEKEY : " + files['images'][i].key);
	fileKeys.push (files['images'][i].key);
}
}
if (files['logo'] != undefined ) {
	logoKey = files['logo'][0].key;	
}


    department.find({name : request.body.name },function(err, doc){
       if (doc.length){
            console.log("department already exists");
            response.send ("department already exists");
          }else{
          	
             var newDepartment = new department({
                name :request.body.name,
               	institute : request.body.institute,
                manager :request.body.manager,
                description :request.body.description,
                logo : logoKey,
                images : fileKeys

              });
              
           try {
              newDepartment.save(function(error, result) {
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

exports.updateDepartment = function (request, response, files) {

	 var query = department.findOne().where ('name', request.body.name);
	 
	 query.exec (function (err,doc) {
	
	try {	
	 	var query = doc.update ({
	 		$set: {
	 				name :request.body.name,
                	manager :request.body.manager,
                	description :request.body.description,
                	images :request.body.images
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


exports.deleteDepartment = function (request, response) {

	var query = department.findOne().where ('name', request.body.name);
	
	 query.exec (function (err,doc) {
	 		try {	
	 	var query = doc.remove (function (err, deletedDoc) {
	 		department.findOne ({name: request.body.name}, function (err, doc) {
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

