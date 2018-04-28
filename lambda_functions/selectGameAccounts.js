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

    // Select all phone numbers associated with a game from "joins" table
    var phones = "";
    var sql = "SELECT phone FROM joins WHERE gid = " + event.gid;
    db.query(sql, function(error, rows, fields) {
        phones += "(";
        for (var i = 0; i < rows.length; i++) {
            phones += rows[i]["phone"];
            if (i != rows.length - 1) {
                phones += ",";
            }
        }
        phones += ")";
        
        // Select all rows that are in phones from "accounts" table
        var sql2 = "SELECT * FROM accounts WHERE phone IN " + phones;
        db.query(sql2, function(error, rows, fields) {
            callback(error, rows);
        });
    });
};