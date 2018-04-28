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
    
    // Insert row into "members" table
    var sql = "INSERT INTO members VALUES ('" + event.body.phone + "', " + event.body.tid + ")";
    db.query(sql);

    // Update a team's numMembers from "teams" table
    var sql2 = "UPDATE teams SET numMembers = numMembers + 1 WHERE tid = " + event.body.tid;
    db.query(sql2, function(error, rows, fields) {
        callback(null);
    });
};