var mongoose = require ('mongoose');
var  autoIncrement = require('mongoose-auto-increment');
config = {
    mongoUrl : 'mongodb://shenkarShow:shenkarShow1@ds145009.mlab.com:45009/shenkar_show'
}

var options = {
    server : {
        auto_reconnect :true,
    }
};

mongoose.connect (config.mongoUrl, options);
db = mongoose.connection;

autoIncrement.initialize(db);


db.on ('error', function (err) {
    console.log ('Mongoose : Error: ' + err);
});

db.on ('open', function () {
    console.log ('Mongoose: connection established');
})

db.on ('disconnected', function () {
    console.log ('Mongoose: Connection stopped, reconnect');
    mongoose.connect (config.mongoUurl, options);
});

db.on ('reconnected', function () {
    console.info ('Mongoose reconnected!');
});