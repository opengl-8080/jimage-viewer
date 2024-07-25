package jimgv.log;

public enum LogLevel {
    FATAL(4),
    ERROR(3),
    WARN(2),
    INFO(1),
    DEBUG(0),
    ;

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public boolean isEnable(LogLevel target) {
        return this.level <= target.level;
    }
}
