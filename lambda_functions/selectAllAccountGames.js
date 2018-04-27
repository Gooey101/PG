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
    
    // Select all games of a specific account from the "games" table
    var sql = "SELECT * FROM games WHERE EXISTS (SELECT * FROM joins WHERE games.gid = joins.gid AND phone = '" + event.phone + "')";
    db.query(sql, function(error, rows, fields) {
        callback(error, rows);
    });
};