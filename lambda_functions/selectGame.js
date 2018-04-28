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

    // Select specific row from "games" table
    var sql = "SELECT games.gid, games.sport, games.description, games.gameDate, games.startTime, " +
    "games.endTime, games.attendees, games.filled, courts.address FROM games, courts WHERE EXISTS " +
    "(SELECT * FROM holds WHERE games.gid = holds.gid AND holds.cid = courts.cid AND gid = " + event.gid + ")";
    db.query(sql, function(error, rows, fields) {
        callback(null, rows);
    });
};