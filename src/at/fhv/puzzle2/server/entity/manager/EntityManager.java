package at.fhv.puzzle2.server.entity.manager;

import at.fhv.puzzle2.server.database.Database;

public abstract class EntityManager {
    protected Database _database;

    protected EntityManager(Database database) {
        _database = database;
    }
}
