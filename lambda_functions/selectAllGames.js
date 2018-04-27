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
    
    // Select all rows from "games" table
    var sql = "SELECT * FROM games WHERE attendees < capacity AND gid NOT IN (SELECT gid FROM joins WHERE joins.phone = " + event.body.phone + ")";
    db.query(sql, function(error, rows, fields) {
        callback(error, rows);
    });
};