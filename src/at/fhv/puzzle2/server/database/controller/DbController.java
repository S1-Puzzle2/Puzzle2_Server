package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;

class DbController {
    final DatabaseConnection _connection;

    DbController(DatabaseConnection connection) {
        _connection = connection;
    }
}
