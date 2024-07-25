package jimgv.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class Logger {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS");

    public static final Logger INSTANCE = new Logger();

    private LogLevel level = LogLevel.DEBUG;

    public void debug(String message) {
        debug(() -> message);
    }

    public void debug(String message, Object... args) {
        debug(() -> String.format(message, args));
    }

    public void debug(Supplier<String> messageSupplier) {
        if (this.level.isEnable(LogLevel.DEBUG)) {
            final String message = messageSupplier.get();
            debug(message);
        }
    }

    public void setLogLevel(LogLevel level) {
        this.level = level;
    }

    private void log(String level, String message, Object... args) {
        final String formatted = String.format(message, args);
        final String dateTime = DATE_FORMATTER.format(LocalDateTime.now());
        System.out.printf("[%s] %s - " + formatted + "%n", level, dateTime);
    }
}
