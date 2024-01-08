package com.jewelbackend.backend.common.config;

import java.time.LocalDate;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelperUtils {

    private HelperUtils() {
    }

    static final Logger logger = LogManager.getLogger();

    public static void logMessage(Level level, String message) {
        logger.log(level, message);
    }

    public static String localDateToString(LocalDate localDate){
        return String.format("%s/%s/%s", localDate.getDayOfMonth(), localDate.getMonthValue(),
                localDate.getYear());
    }
}
