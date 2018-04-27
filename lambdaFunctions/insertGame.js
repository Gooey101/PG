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

    // Insert row into "games" table
    var sql1 = "INSERT INTO games (sport, description, gameDate, startTime, endTime, minAge, maxAge, minSkillLevel, capacity, creator) VALUES ('" +
        event.body.sport + "', '" + event.body.description + "', '" + event.body.gameDate + "', '" +
        event.body.startTime + "', '" + event.body.endTime + "', " + event.body.minAge + ", " +
        event.body.maxAge + ", " + event.body.minSkillLevel + ", " + event.body.capacity + ", '" +
        event.body.phone + "')";
    db.query(sql1);

    // Select ID of inserted row
    var sql2 = "(SELECT LAST_INSERT_ID())";
    db.query(sql2, function(error, rows, fields) {
        var gid = rows[0]['LAST_INSERT_ID()'];

        // Insert row into "holds" table
        var sql3 = "INSERT INTO holds VALUES(" + event.body.cid + ", " + gid + ")";
        db.query(sql3);

        // Insert row into "joins" table
        var sql4 = "INSERT INTO joins VALUES('" + event.body.phone + "', '" + gid + "')";
        db.query(sql4, function(error, rows, fields) {
            callback(error, "Success");
        });
    });
};