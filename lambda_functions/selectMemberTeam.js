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
    
    // Select account team from "teams" and "members" tables
    var sql = "SELECT * FROM teams WHERE tid IN (SELECT tid FROM members WHERE phone = '" + event.phone + "')";
    db.query(sql, function(error, rows, fields) {
        callback(null, rows);
    });
};