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

    // Delete rows from "holds", "joins", and "games" tables
    var sql = "DELETE FROM holds WHERE gid = " + event.body.gid;
    db.query(sql);
    var sql2 = "DELETE FROM joins WHERE gid = " + event.body.gid;
    db.query(sql2);
    var sql3 = "DELETE FROM games WHERE gid = " + event.body.gid;
    db.query(sql3, function(error, rows, fields) {
        callback(null);
    });
};