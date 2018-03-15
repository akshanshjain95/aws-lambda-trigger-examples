package com.knoldus.aws.utils.confighelper;

import com.knoldus.aws.utils.loggerhelper.LoggerFactory;
import com.knoldus.aws.utils.loggerhelper.LoggerService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.Optional;

public final class ConfigReader {

    private Config config;
    private final LoggerService logger = new LoggerFactory(ConfigReader.class.getName());
    private final Optional<String> environment = Optional.ofNullable(System.getenv("ENVIRONMENT"));

    private static final Config BASE = ConfigFactory.load();

    private ConfigReader(final String setting) {
        String env = environment.orElse("dev");
        config = BASE.getConfig(setting);
        logger.info("Environment: " + env);
        if (config.hasPath(env)) {
            config = config.getConfig(env).withFallback(config).withFallback(config);
        }
    }

    public static ConfigReader getConfigReader(final String setting) {
        return new ConfigReader(setting);
    }

    public String getProperty(final String key) {
        return config.getString(key);
    }

}
