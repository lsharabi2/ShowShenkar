var mongoose = require ('mongoose');
var schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);


var departmentSchema = new schema (

{

    _id : {type: Number, required:true, index:1, unique:true, autoIncrement:true},
    name: String,
    projects : [{type: Number, ref: 'project'}],
    manager: {type: Number, ref: 'user'},
    description: String,
    logo : String, 
    images: [String]
}, 

{collection: 'departments'}

);

departmentSchema.plugin(autoIncrement.plugin, 'department');
var department = mongoose.model ('department', departmentSchema);

module.exports = department;


