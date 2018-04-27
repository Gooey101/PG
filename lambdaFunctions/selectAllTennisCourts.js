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
    
    // Select all tennis courts from the "courts" table
    var sql = "SELECT courts.cid, courts.address, courts.public, courts.outside, courts.openTime, " +
        "courts.closeTime, tennisCourts.numNets FROM courts, tennisCourts WHERE EXISTS " +
        "(SELECT * FROM tennisCourts WHERE courts.cid = tennisCourts.cid)";
    db.query(sql, function(error, rows, fields) {
        callback(error, rows);
    });
};