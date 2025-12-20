package dev.m3v;

import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Log {
    private static final String PATH = "data/logback.xml";

    public static void load() {
        File file = new File(PATH);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                try (FileWriter writer = new FileWriter(PATH)) {
                    writer.write("""
<configuration>
    <appender name="CONSOLE"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/holoBot-latest.log</file>

        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/holoBot-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>14</maxHistory>
        </rollingPolicy>

        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
                    """);
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.shutdown(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(String level, String message, Class<?> location, Exception e) {
        log(level, message, location, (Throwable) e);
    }

    public static void log(String level, String message, Class<?> location, Throwable t) {
        Logger logger = LoggerFactory.getLogger(location);
        boolean hasThrowable = t != null;

        switch (level) {
            case "ERROR" -> {
                if (hasThrowable) logger.error(message, t); else logger.error(message);
            }
            case "WARN" -> {
                if (hasThrowable) logger.warn(message, t); else logger.warn(message);
            }
            case "DEBUG" -> {
                if (hasThrowable) logger.debug(message, t); else logger.debug(message);
            }
            default -> {
                if (hasThrowable) logger.info(message, t); else logger.info(message);
            }
        }
    }

    public static void error(Class<?> location, String message, Throwable t) {
        log("ERROR", message, location, t);
    }

    public static void warn(Class<?> location, String message, Throwable t) {
        log("WARN", message, location, t);
    }

    public static void info(Class<?> location, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(location);
        logger.info(message, args);
    }

    public static void debug(Class<?> location, String message, Object... args) {
        Logger logger = LoggerFactory.getLogger(location);
        logger.debug(message, args);
    }
}
