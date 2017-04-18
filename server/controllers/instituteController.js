var mongoose = require ('mongoose');
var institute = require ('../schemes/institute');
var credentials = require ('../credentials.js');

var AWS = require('aws-sdk');
var multer  = require('multer');
var multerS3 = require('multer-s3');
var mime = require('mime-types');



AWS.config.update({
    accessKeyId: credentials.accessKeyId,
    secretAccessKey: credentials.secretAccessKey
});

var s3 = new AWS.S3();







exports.getAllInstitutes = function (req, res) {

    institute.find ({}).populate('manager', 'name').populate('departments', ['name','logo']).
    where('institute').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}



exports.getInstituteById  = function (req, res) {
    var id = req.params.instituteId;
    console.log ('User ID = ' + id);

    institute.find ({_id : id}).populate('manager', 'name').
    populate('departments', ['name', 'logo']).
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.getInstituteByName  = function (req, res) {
    var name = req.params.instituteName;
    console.log ('Institute name = ' + name);

    institute.find ({name : name}).populate('manager', 'name').
    where('institute').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
    });
}

exports.createInstitute = function (request, response, fileKey) {

    institute.find({name : request.body.name },function(err, doc){
       if (doc.length){
            console.log("institute already exists");
            response.send ("institute already exists");
          }else{
          	
             var newInstitute = new institute({
                name :request.body.name,
                manager :request.body.manager,
                logo : fileKey,
                maps :request.body.maps,
                routes :request.body.routes,
                
                headerBackgroundColor: request.body.headerBackgroundColor,
    			headerFontColor: request.body.headerFontColor,
    			footerBackgroundColor: request.body.footerBackgroundColor,
			    footerFontColor: request.body.footerFontColor


              });
              
           try {
           	console.log(request.files);
              newInstitute.save(function(error, result) {
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

exports.updateInstitute = function (request, response, fileKey) {
	
	if (fileKey == undefined ) { 
		if (request.body.logoKey) {
			//request.body.logoKey = request.body.logoKey.replace(/\.[^/.]+$/, "");
			fileKey = request.body.logoKey;
		}
		else {
			fileKey = '';
		}
		} 
		else 
		{
			console.log (fileKey);
			
			s3.deleteObjects({
    Bucket: 'shenkar-show2',
    Delete: {
        Objects: [
             { Key: request.body.logoKey}
        ]
    }
}, function(err, data) {

    if (err)
        return console.log(err);

    console.log('success');

});
			}

	 var query = institute.findOne().where ('name', request.body.name);
	 
	 query.exec (function (err,doc) {
	
	try {	
	 	var query = doc.update ({
	 		$set: {
	 				name :request.body.name,
	                manager :request.body.manager,
	                logo :fileKey,
	                maps :request.body.maps,
	                routes :request.body.routes,
	                
	                headerBackgroundColor: request.body.headerBackgroundColor,
    				headerFontColor: request.body.headerFontColor,
    				footerBackgroundColor: request.body.footerBackgroundColor,
			    	footerFontColor: request.body.footerFontColor
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


exports.deleteInstitute = function (request, response) {

	var query = institute.findOne().where ('name', request.body.name);
	
	 query.exec (function (err,doc) {
	 		try {	
			
			s3.deleteObjects({
    Bucket: 'shenkar-show2',
    Delete: {
        Objects: [
             { Key: doc.logo}
        ]
    }
}, function(err, data) {

    if (err)
        return console.log(err);

    console.log('success');

});
	 			
	 	var query = doc.remove (function (err, deletedDoc) {
	 		institute.findOne ({name: request.body.name}, function (err, doc) {
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
