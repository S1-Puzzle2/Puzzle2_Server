package at.fhv.puzzle2.server.entity.manager;

import at.fhv.puzzle2.server.database.Database;

abstract class EntityManager {
    final Database _database;

    EntityManager(Database database) {
        _database = database;
    }
}
