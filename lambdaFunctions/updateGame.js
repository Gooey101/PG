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

    // Select attendees from specific row of "games" table
    var sql1 = "SELECT attendees FROM games WHERE gid = " + event.body.gid;
    db.query(sql1, function(error, rows, fields) {
        var attendees = rows[0]['attendees'];
        var newAttendees = 0;

        // Join or unjoin game
        if (event.body.join == 1) {
            // Increase game attendees by 1
            newAttendees = attendees + 1;
            var sql2 = "UPDATE games SET attendees = " + newAttendees + " WHERE gid = " + event.body.gid;
            db.query(sql2);

            // Insert row into "joins" table
            var sql3 = "INSERT INTO joins VALUES('" + event.body.phone + "', '" + event.body.gid + "')";
            db.query(sql3, function(error, rows, fields) {
                callback(error, "Success");
            });
        } else if (event.body.join == 0) {
            // Decrease game attendees by 1
            newAttendees = attendees - 1;
            var sql4 = "UPDATE games SET attendees = " + newAttendees + " WHERE gid = " + event.body.gid;
            db.query(sql4);

            // Delete row from "joins" table
            var sql5 = "DELETE FROM joins WHERE phone = '" + event.body.phone + "' AND gid = " + event.body.gid + " LIMIT 1";
            db.query(sql5, function(error, rows, fields) {
                callback(error, "Success");
            });
        }
    });
};