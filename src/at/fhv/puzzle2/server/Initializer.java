package at.fhv.puzzle2.server;

import at.fhv.puzzle2.logging.LogLevel;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.logging.formatter.SimpleFormatter;
import at.fhv.puzzle2.logging.sink.ConsoleSink;
import at.fhv.puzzle2.server.database.Database;

import java.sql.SQLException;

public class Initializer {
    public static void initialize() throws ConfigurationException, SQLException {
        //Initialize configuration and database
        Configuration.initConfiguration();
        Database.initDatabase();

        //Instantiate the loggers now
        //TODO read the configuration and use that one
        Logger.createLogger(new SimpleFormatter(), LogLevel.TRACE);
        Logger.appendLogSink(new ConsoleSink());
    }
}
