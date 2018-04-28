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

    // Select specific row from "accounts" table
    var sql = "SELECT * FROM accounts WHERE phone = " + event.phone;
    db.query(sql, function(error, rows, fields) {

        // If account exists
        if (rows.length > 0) {
            var str = rows[0]["dob"];
            var year = str.getUTCFullYear();
            var month = str.getUTCMonth() + 1;
            var day = str.getUTCDate();
            var dob = year + "-" + month + "-" + day;
            var sql2 = "SELECT DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT('" + dob + "', '%Y') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT('" + dob + "', '00-%m-%d')) AS age";
            
            // Calculate age of birthdate
            db.query(sql2, function(error, rows2, fields) {
                rows[0]["age"] = parseInt(rows2[0]["age"], 10);
                callback(null, rows);
            });
        } else {
            // If account doesn't exist, return empty array
            callback(null, rows);
        }
    });
};