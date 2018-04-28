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
    
    if (event.aggregate == 0) { // Select all rows from "teams" table
        db.query('SELECT * FROM teams', function(error, rows, fields) {
            callback(null, rows);
        });
    } else if (event.aggregate == 1) {  // Select max numMembers from "teams" table
        db.query('SELECT MAX(numMembers) FROM teams', function(error, rows, fields) {
            // Select teams with Max numMembers from "teams" table
            var sql = "SELECT * FROM teams WHERE numMembers = " + rows[0]["MAX(numMembers)"];
            db.query(sql, function(error, rows, fields) {
                callback(null, rows);
            });
        });
    } else if (event.aggregate == 2) {  // Select min numMembers from "teams" table
        db.query('SELECT MIN(numMembers) FROM teams', function(error, rows, fields) {
            // Select teams with Min numMembers from "teams" table
            var sql2 = "SELECT * FROM teams WHERE numMembers = " + rows[0]["MIN(numMembers)"];
            db.query(sql2, function(error, rows, fields) {
                callback(null, rows);
            });
        });
    }
};