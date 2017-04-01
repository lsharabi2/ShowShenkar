var express= require ('express');
var app = express();
var AWS = require('aws-sdk');
var multer  = require('multer');
var multerS3 = require('multer-s3');
var mime = require('mime-types');
var credentials = require ('./credentials.js');
var cookieParser = require('cookie-parser');


AWS.config.update({
    accessKeyId: credentials.accessKeyId,
    secretAccessKey: credentials.secretAccessKey
});

var s3 = new AWS.S3();

var upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'shenkar-show2',
    
      limits: { fileSize: 1000*1000*1 },
   fileFilter: function (req, file, cb) {
 if (file.mimetype !== 'image/png' || file.originalname =='') {
 	
 	console.log ("ERRORRRRRRR: " + file.originalname);
  req.fileValidationError = 'goes wrong on the mimetype';
  return cb(null, false, new Error('goes wrong on the mimetype'));
 }
 console.log ("NOOOOOOO: " + file.originalname);
 file.key = '';
 cb(null, true);
},
      
    metadata: function (req, file, cb) {
      cb(null, {fieldName: file.fieldname});
    },
  
    key: function (req, file, cb) {
      cb(null, Date.now().toString() + '.' + mime.extension(file.mimetype))
    }
  })
})

// var deleteImage = s3.deleteObjects({
    // Bucket: 'myprivatebucket/some/subfolders',
    // Delete: {
        // Objects: [
             // { Key: 'nameofthefile1.extension' }
        // ]
    // }
// }, function(err, data) {
// 
    // if (err)
        // return console.log(err);
// 
    // console.log('success');
// 
// });


//controllers
var userController = require ('./controllers/userController');
var instituteController = require ('./controllers/instituteController');
var departmentController = require ('./controllers/departmentController');
var projectController = require ('./controllers/projectController');
var adminController = require ('./controllers/adminController');
var instituteManagerController = require ('./controllers/instituteManagerController');
var departmentManagerController = require ('./controllers/departmentManagerController');
var studentController = require ('./controllers/studentController');

var bodyParser = require('body-parser');
app.use( bodyParser.json() );       
app.use(bodyParser.urlencoded({ extended: true})); 

app.use(cookieParser());

//schemes
var user = require ('./schemes/user');
var institute = require ('./schemes/institute');
var department = require ('./schemes/department');
var project = require ('./schemes/project');

var port = process.env.PORT || 3000;
app.set ('port',port);
app.use ('/' , express.static ('./public'));

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  app.set ('json spaces', 4);
  res.set ("Content-Type", "application/json");
  next();
});

//users
app.get ('/allUsers', userController.getAllUsers);
app.get ('/users/id/:userId', userController.getUserById);
app.get ('/users/name/:userName', userController.getUserByName);
app.post ('/users/auth', userController.auth);
app.post ('/users/create', userController.createUser);
app.post ('/users/update', userController.updateUser);
app.post ('/users/delete', userController.deleteUser);

//institutes
app.get ('/allInstitutes', instituteController.getAllInstitutes);
app.get ('/institutes/id/:instituteId', instituteController.getInstituteById);
app.get ('/institutes/name/:instituteName', instituteController.getInstituteByName);
app.post ('/institutes/create', upload.single('logo') , function(req, res){instituteController.createInstitute(req,res, req.file.key);});
app.post ('/institutes/update', upload.single('logo') , 
function(req, res){
	if (req.file){
		
	instituteController.updateInstitute(req,res, req.file.key);}
	else {
		
		instituteController.updateInstitute(req,res);
	}
	});
app.post ('/institutes/delete', instituteController.deleteInstitute);

//departments
app.get ('/allDepartments', departmentController.getAllDepartments);
app.get ('/departments/id/:departmentId', departmentController.getDepartmentById);
app.get ('/departments/name/:departmentName', departmentController.getDepartmentByName);
app.post ('/departments/create', upload.fields([{name : 'logo', maxCount : 1 } ,{name : 'images', maxCount : 5 }]) ,function(req, res){departmentController.createDepartment(req,res, req.files);});
app.post ('/departments/update',  upload.array('images', 5) ,
function(req, res){
	if (req.files){
	departmentController.updateDepartment(req,res, req.files);
	}
	
	else {
		
		instituteController.updateDepartment(req,res);
	}
	
	});
app.post ('/departments/delete', departmentController.deleteDepartment);

//projects
app.get ('/allProjects', projectController.getAllProjects);
app.get ('/projects/id/:projectId', projectController.getProjectById);
app.get ('/projects/name/:projectName', projectController.getProjectByName);
app.post ('/projects/create', projectController.createProject);
app.post ('/projects/update', projectController.updateProject);
app.post ('/projects/delete', projectController.deleteProject);

//admin
app.get ('/admin', adminController.getIndex);

//institute manager 
app.get ('/institute', instituteManagerController.getIndex);
app.get ('/institute/update',  instituteManagerController.getUpdate);
app.get ('/institute/departments',  instituteManagerController.getDepartments);
app.get ('/institute/users',  instituteManagerController.getUsers);

//department manager 
app.get ('/department', departmentManagerController.getIndex);
app.get ('/department/projects', departmentManagerController.getProjects);
app.get ('/department/users', departmentManagerController.getUsers);



//student
app.get ('/student', studentController.getIndex);

app.get ('/', function (req,res) {
    res.send ("Welcome to Shenkar show app.please refer to the api for usage instructions.");
});

app.all ('*', function (req,res) {
    res.send ("404: Page Not found.");
});

app.listen (port);
console.log ("service is listening on port " + port);
