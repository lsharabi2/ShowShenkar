var mongoose = require ('../database');
var project = require ('../schemes/project');

//return all projects in database
exports.getAllProjects = function (req, res) {

    project.find ({}).
    where('project').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}


//search project by id
exports.getProjectById  = function (req, res) {
    var id = req.params.projectId;
    console.log ('project ID = ' + id);

    project.find ({_id : id}).
    where('project').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

//search project by id
exports.getProjectById2  = function (projectId, callback) {
    var id = projectId;
    console.log ('Dep ID = ' + id);

    project.findOne ({_id : id}).populate('students', 'name').
    where('project').ne ('PRIVATE').
    exec (function (err, doc) {
    	console.log ('dasdasd');
        callback (doc);
    });
}

//search project by name
exports.getProjectByName  = function (req, res) {
    var name = req.params.projectName;
    console.log ('project name = ' + name);

    project.find ({name : name}).
    where('project').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}


exports.createProject = function (request, response) {

    project.find({name : request.body.name },function(err, doc){
       if (doc.length){
            console.log("project already exists");
            response.send ("project already exists");
          }else{
          	
          
          	
             var newProject = new project({
                name :request.body.name,
                department :request.body.department,
                students :request.body.students,
                description :request.body.description,
                image :request.body.image,
                video :request.body.video,
                audio :request.body.audio,
                location :request.body.location,
				likes :request.body.likes,
                comments :request.body.comments,
                QRcode :request.body.QRcode
              });
              
           try {
              newProject.save(function(error, result) {
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

exports.updateProject = function (request, response) {

	 var query = project.findOne().where ('name', request.body.name);
	 
	 query.exec (function (err,doc) {
	
	try {	
	 	var query = doc.update ({
	 		$set: {
	 				name :request.body.name,
	                department :request.body.department,
	                students :request.body.students,
	                description :request.body.description,
	                image :request.body.image,
	                video :request.body.video,
	                audio :request.body.audio,
	                location :request.body.location,
					likes :request.body.likes,
	                comments :request.body.comments,
	                QRcode :request.body.QRcode
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


exports.deleteProject = function (request, response) {

	var query = project.findOne().where ('name', request.body.name);
	
	 query.exec (function (err,doc) {
	 		try {	
	 	var query = doc.remove (function (err, deletedDoc) {
	 		project.findOne ({name: request.body.name}, function (err, doc) {
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

