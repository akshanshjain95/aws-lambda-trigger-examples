package com.knoldus.aws.utils.loggerhelper;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoggerFactory implements LoggerService {

    private Logger logger;

    public LoggerFactory(String className) {
        logger = LogManager.getLogger(className);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message, Throwable throwable) {
        Optional<Throwable> maybeThrowable = Optional.ofNullable(throwable);

        if (maybeThrowable.isPresent()) {
            logger.error(message, throwable);
        } else {
            logger.error(message);
        }
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(Throwable throwable) {
        logger.error("", throwable);
    }

    public void info(String message) {
        logger.info(message);

    }

    public void warn(String message) {
        logger.warn(message);
    }

    public static LoggerService getLogService(final String className) {
        return new LoggerFactory(className);
    }
}
