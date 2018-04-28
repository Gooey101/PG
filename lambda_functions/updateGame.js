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

    // Join/unjoin game
    if (event.body.join == 1) {
        // Increase game attendees by 1
        var sql1 = "UPDATE games SET attendees = attendees + 1 WHERE gid = " + event.body.gid;
        db.query(sql1);

        // Insert row into "joins" table
        var sql2 = "INSERT INTO joins VALUES('" + event.body.phone + "', '" + event.body.gid + "')";
        db.query(sql2, function(error, rows, fields) {
            callback(null);
        });
    } else if (event.body.join == 0) {
        // Decrease game attendees by 1
        var sql3 = "UPDATE games SET attendees = attendees - 1 WHERE gid = " + event.body.gid;
        db.query(sql3);

        // Delete row from "joins" table
        var sql4 = "DELETE FROM joins WHERE phone = '" + event.body.phone + "' AND gid = " + event.body.gid + " LIMIT 1";
        db.query(sql4, function(error, rows, fields) {
            callback(null);
        });
    }
};