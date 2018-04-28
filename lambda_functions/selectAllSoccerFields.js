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
    
    // Select all soccer fields from the "courts" table
    var sql = "SELECT courts.cid, courts.address, courts.public, courts.outside, courts.openTime, " +
        "courts.closeTime, soccerFields.grass FROM courts, soccerFields WHERE courts.cid = soccerFields.cid";
    db.query(sql, function(error, rows, fields) {
        callback(null, rows);
    });
};