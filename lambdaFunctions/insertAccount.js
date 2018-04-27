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
    
    // Insert row into "accounts" table
    var sql = "INSERT INTO accounts VALUES ('" + event.body.phone + "', '" +
        event.body.username + "', '" + event.body.dob + "')";
    db.query(sql, function(error, rows, fields) {
        callback(null);
    });
};