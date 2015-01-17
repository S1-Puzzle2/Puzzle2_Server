package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;

public class DbController {
    protected DatabaseConnection _connection;

    protected DbController(DatabaseConnection connection) {
        _connection = connection;
    }
}
