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

    // Update an emergency contact from "emergencyContacts" table
    var sql = "UPDATE emergencyContacts SET ecPhone = '" + event.body.ecPhone +
        "', fName = '" + event.body.fName + "', relationship = '" + event.body.relationship +
        "' WHERE phone = " + event.body.phone;
    db.query(sql, function(error, rows, fields) {
        callback(null);
    });
};