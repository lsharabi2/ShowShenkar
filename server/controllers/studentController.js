var mongoose = require ('../database');
var user = require ('../schemes/user');
var institute = require ('../schemes/institute');
var user = require ('../schemes/user');
var department = require ('../schemes/department');
var project = require ('../schemes/project');
var departmentController = require ('../controllers/departmentController');
var userController = require ('../controllers/userController');
var auth = require ('../controllers/authController');
var async = require("async");

exports.getIndex = function (req,res) {
	
	if (req.cookies.shenkarShowSession != undefined || req.cookies.shenkarShowUserId != undefined){
	auth.authCookies(req.cookies.shenkarShowSession, req.cookies.shenkarShowUserId, function (result) {
	console.log ("userType : " + result);
	if (result == 'student')
	{
		
		  var studentId =  req.cookies.shenkarShowUserId;
    		console.log ('User ID = ' + studentId);

user.findOne ({_id: studentId}).exec (function (err, student) {
	
	console.log ("STUDENT : " + student);
	console.log ("STUDENT project id  : " + student.project);
    project.findOne ({ _id : student.project}).populate('students', 'name').exec (function (err, docs) {
   
        console.log ('PROJECT: ' + docs);
        res.json (docs);
        return;
    });
		
	});
	
}
else {
	res.send ("not authorized!");
}
	}); }
	else {
		res.send ("not authorized!");
	}
}
