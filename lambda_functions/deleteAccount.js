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

    // Select all account gid's from "joins" table
    var gids = "";
    var sql = "SELECT gid FROM joins WHERE phone = " + event.phone;
    db.query(sql, function(error, rows, fields) {
        gids += "(";
        for (var i = 0; i < rows.length; i++) {
            gids += rows[i]["gid"];
            if (i != rows.length - 1) {
                gids += ",";
            }
        }
        gids += ")";
        
        // Update attendees of each account game
        var sql2 = "UPDATE games SET attendees = attendees - 1 WHERE gid IN " + gids;
        db.query(sql2);
    });
    
    // Delete rows from "joins" table
    var sql3 = "DELETE FROM joins WHERE phone = " + event.phone;
    db.query(sql3);

    // Delete row from "members" table
    var sql4 = "DELETE FROM members WHERE phone = " + event.phone;
    db.query(sql4);

    // Delete row from "accounts" table
    var sql5 = "DELETE FROM accounts WHERE phone = " + event.phone;
    db.query(sql5, function(error, rows, fields) {
        callback(null);
    });
};