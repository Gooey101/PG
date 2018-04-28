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

    // Insert an emergency contact into "emergencyContacts" table
    var sql = "INSERT INTO emergencyContacts VALUES('" + event.body.phone + "', '" +
        event.body.fName + "', '" + event.body.relationship + "', '" + event.body.ecPhone + "')";
    db.query(sql, function(error, rows, fields) {
        callback(null);
    });
};