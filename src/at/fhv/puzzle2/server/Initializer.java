package at.fhv.puzzle2.server;

import at.fhv.puzzle2.logging.Exception.LogFormatterUnknownException;
import at.fhv.puzzle2.logging.LogLevel;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.logging.LoggerFactory;
import at.fhv.puzzle2.logging.formatter.LogFormatter;
import at.fhv.puzzle2.logging.sink.ConsoleSink;
import at.fhv.puzzle2.server.database.Database;

import java.sql.SQLException;
import java.util.Optional;

public class Initializer {
    public static void initialize() throws ConfigurationException, SQLException, LogFormatterUnknownException {
        //Initialize configuration and database
        Configuration.initConfiguration();
        Database.initDatabase();

        //Instantiate the loggers now
        Configuration loggerConfig = Configuration.getInstance().getConfiguration("logger");

        LogFormatter formatter = LoggerFactory.createFormatter(loggerConfig.getStringOrDefault("formatter", "SimpleFormatter"));

        String logLevelString = loggerConfig.getStringOrDefault("logLevel", "Warning");
        Optional<LogLevel> logLevel = LogLevel.stringToLevel(logLevelString);
        if(!logLevel.isPresent()) {
            throw new ConfigurationException("Loglevel '" + logLevelString + "' is unknown");
        }


        Logger.createLogger(formatter, logLevel.get());

        //TODO read log sinks
        Logger.appendLogSink(new ConsoleSink());
    }
}
