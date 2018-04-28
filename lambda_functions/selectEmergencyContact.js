var mysql = require('mysql');
var db = mysql.createConnection({
    host: "mydbinstance.cfkigfxfroyb.us-east-2.rds.amazonaws.com",
    user: "christophergu",
    password: "christophergu777",
    database: "pg"
});

exports.handler = (event, context, callback) => {
    // Freeze process after callback is called
    context.callbackWaitsForEmptyEventLoop = false;

    // Select account emergency contact from "emergencyContacts" table
    var sql = "SELECT * FROM emergencyContacts WHERE phone = " + event.phone;
    db.query(sql, function(error, rows, fields) {
        callback(null, rows);
    });
};