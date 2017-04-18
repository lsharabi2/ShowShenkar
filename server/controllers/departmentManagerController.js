var mongoose = require ('../database');
var user = require ('../schemes/user');
var institute = require ('../schemes/institute');
var user = require ('../schemes/user');
var department = require ('../schemes/department');
var departmentController = require ('../controllers/departmentController');
var projectController = require ('../controllers/projectController');
var userController = require ('../controllers/userController');
var auth = require ('../controllers/authController');
var async = require("async");

exports.getIndex = function (req,res) {
	
	if (req.cookies.shenkarShowSession != undefined || req.cookies.shenkarShowUserId != undefined){
	auth.authCookies(req.cookies.shenkarShowSession, req.cookies.shenkarShowUserId, function (result) {
	console.log ("userType : " + result);
	if (result == 'department manager')
	{
		
		  var managerId =  req.cookies.shenkarShowUserId;
    		console.log ('User ID = ' + managerId);

    department.find ({ manager : managerId}).populate('manager', 'name').
    where('department').ne ('PRIVATE').
    exec (function (err, docs) {
        Data = docs;
        console.log ('docs: ' + docs);
        res.json (docs);
        return;
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


exports.getProjects = function (req,res) {
	
	if (req.cookies.shenkarShowSession != undefined || req.cookies.shenkarShowUserId != undefined){
	auth.authCookies(req.cookies.shenkarShowSession, req.cookies.shenkarShowUserId, function (result) {
	console.log ("userType : " + result);
	if (result == 'department manager')
	{
		var myProjects = [];
		var managerId =  req.cookies.shenkarShowUserId;
		console.log ('User ID = ' + managerId);

	    department.findOne ({ manager : managerId}).populate('manager', 'name').
	    where('department').ne ('PRIVATE').
	    exec (function (err, doc) {
	    	
	    	projects = doc.projects;   

		async.each(projects, function(proj, callback){
		projectController.getProjectById2(proj, function (pro){
		      
		    myProjects.push (pro);
		    callback();
    });
  },

  function(err){
    res.json (myProjects);
  }
);

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



exports.getUsers = function (req, res) {
	
	if (req.cookies.shenkarShowSession != undefined || req.cookies.shenkarShowUserId != undefined){
	auth.authCookies(req.cookies.shenkarShowSession, req.cookies.shenkarShowUserId, function (result) {
	console.log ("userType : " + result);
	if (result == 'department manager')
	{
		var myUsers = [];
		var managerId =  req.cookies.shenkarShowUserId;
		console.log ('User ID = ' + managerId);

	    department.findOne ({ manager : managerId}).
	    where('department').ne ('PRIVATE').
	    exec (function (err, doc) {
	    	
	    	console.log ("department : " + doc)
	    	
	    	user.find ({ department : doc._id, role: "student"}).populate('department', 'name').
    where('user').ne ('PRIVATE').
    exec (function (err, departmentUsers) {
        console.log ('department Users: ' + departmentUsers);
        res.send (departmentUsers);
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

