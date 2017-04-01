var mongoose = require ('mongoose');
var schema = mongoose.Schema;


var roleSchema = new schema (

{

    id : {type: Number, required:true, index:1, unique:true},
    name: String

}, 

{collection: 'roles'}

);

var role = mongoose.model ('role',roleSchema);

module.exports = role;


