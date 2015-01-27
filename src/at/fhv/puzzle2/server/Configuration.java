package at.fhv.puzzle2.server;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class Configuration {
    private final Toml _config;

    private Configuration(Toml config) {
        _config = config;
    }

    public String getStringOrDefault(String key, String defaultValue) {
        String value = _config.getString(key);
        if(value == null) {
            return defaultValue;
        }

        return value;
    }

    public Integer getIntegerOrDefault(String key, Integer defaultValue) {
        Long value = _config.getLong(key);
        if(value == null) {
            return defaultValue;
        }

        return new BigDecimal(value).intValueExact();
    }

    public <T> List<T> getList(String key, Class<T> valueClass) {
        return _config.getList(key, valueClass);
    }

    public Configuration getConfiguration(String key) {
        return new Configuration(_config.getTable(key));
    }

    public static void initConfiguration() throws ConfigurationException {
        File configFile = new File("config.toml");
        if(!configFile.isFile() && !configFile.exists()) {
            throw new ConfigurationException("Konnte die Konfigurationsdatei nicht finden");
        }

        try {
            Toml config = new Toml().parse(configFile);

            _instance = new Configuration(config);
        } catch (IllegalStateException e) {
            throw new ConfigurationException("Die Konfigurationsdatei ist nicht korrekt formatiert");
        }
    }

    private static Configuration _instance = null;
    public static Configuration getInstance() {
        return _instance;
    }
}
