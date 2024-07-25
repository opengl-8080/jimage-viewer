package jimgv.log;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogLevelTest {

    @Test
    void testDebug() {
        assertThat(LogLevel.DEBUG.isEnable(LogLevel.DEBUG)).isTrue();
        assertThat(LogLevel.DEBUG.isEnable(LogLevel.INFO)).isTrue();
        assertThat(LogLevel.DEBUG.isEnable(LogLevel.WARN)).isTrue();
        assertThat(LogLevel.DEBUG.isEnable(LogLevel.ERROR)).isTrue();
        assertThat(LogLevel.DEBUG.isEnable(LogLevel.FATAL)).isTrue();
    }

    @Test
    void testInfo() {
        assertThat(LogLevel.INFO.isEnable(LogLevel.DEBUG)).isFalse();
        assertThat(LogLevel.INFO.isEnable(LogLevel.INFO)).isTrue();
        assertThat(LogLevel.INFO.isEnable(LogLevel.WARN)).isTrue();
        assertThat(LogLevel.INFO.isEnable(LogLevel.ERROR)).isTrue();
        assertThat(LogLevel.INFO.isEnable(LogLevel.FATAL)).isTrue();
    }

    @Test
    void testWarn() {
        assertThat(LogLevel.WARN.isEnable(LogLevel.DEBUG)).isFalse();
        assertThat(LogLevel.WARN.isEnable(LogLevel.INFO)).isFalse();
        assertThat(LogLevel.WARN.isEnable(LogLevel.WARN)).isTrue();
        assertThat(LogLevel.WARN.isEnable(LogLevel.ERROR)).isTrue();
        assertThat(LogLevel.WARN.isEnable(LogLevel.FATAL)).isTrue();
    }

    @Test
    void testError() {
        assertThat(LogLevel.ERROR.isEnable(LogLevel.DEBUG)).isFalse();
        assertThat(LogLevel.ERROR.isEnable(LogLevel.INFO)).isFalse();
        assertThat(LogLevel.ERROR.isEnable(LogLevel.WARN)).isFalse();
        assertThat(LogLevel.ERROR.isEnable(LogLevel.ERROR)).isTrue();
        assertThat(LogLevel.ERROR.isEnable(LogLevel.FATAL)).isTrue();
    }

    @Test
    void testFatal() {
        assertThat(LogLevel.FATAL.isEnable(LogLevel.DEBUG)).isFalse();
        assertThat(LogLevel.FATAL.isEnable(LogLevel.INFO)).isFalse();
        assertThat(LogLevel.FATAL.isEnable(LogLevel.WARN)).isFalse();
        assertThat(LogLevel.FATAL.isEnable(LogLevel.ERROR)).isFalse();
        assertThat(LogLevel.FATAL.isEnable(LogLevel.FATAL)).isTrue();
    }
}