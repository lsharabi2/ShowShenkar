var mongoose = require ('mongoose');
var schema = mongoose.Schema;
var autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);


var instituteSchema = new schema (


{

	_id : {type: Number, required:true, unique:true, autoIncrement:true},
    name: String,
    manager: {type: Number, ref: 'user'},
    logo: String,
    departments: [{type: Number, ref: 'department'}],
    maps: String,
    routes : String,
    
    //Style
    headerBackgroundColor: String,
    headerFontColor: String,
    footerBackgroundColor: String,
    footerFontColor: String


}, 

{collection: 'institutes'}

);


instituteSchema.plugin(autoIncrement.plugin, 'institute');
var institute = mongoose.model ('institute', instituteSchema);

module.exports = institute;


