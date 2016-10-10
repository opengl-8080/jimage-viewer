package jimgv.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;
import java.util.StringJoiner;

public class Configuration {
    private static final File CONFIG_DIR_PATH = new File("./config");
    private static final File FILE_PATH = new File(CONFIG_DIR_PATH, "jimg.xml");
    private static final Configuration instance = new Configuration();
    private static Configuration testInstance;

    public static Configuration getInstance() {
        if (Configuration.testInstance != null) {
            return Configuration.testInstance;
        }

        if (!Configuration.instance.isLoaded()) {
            Configuration.instance.load();
        }

        return Configuration.instance;
    }

    public static void setTestInstance(Configuration testInstance) {
        Configuration.testInstance = testInstance;
    }

    private Properties prop;

    private void load() {
        prop = new Properties();

        if (!FILE_PATH.exists()) {
            this.save();
        }

        try {
            prop.loadFromXML(new BufferedInputStream(new FileInputStream(FILE_PATH)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isLoaded() {
        return this.prop != null;
    }

    public File getBooksConfigDirectory() {
        String value = this.prop.getProperty("books-config-dir", new File(CONFIG_DIR_PATH, "books-conf").getPath());
        return new File(value);
    }

    public File getInitialDirectory() {
        String value = this.prop.getProperty("initial-directory");
        if (value == null || value.isEmpty()) {
            return new File(System.getProperty("user.home"));
        }

        File file = new File(value);

        if (!file.exists()) {
            return new File(System.getProperty("user.home"));
        }

        return new File(value);
    }

    public void setInitialDirectory(File directory) {
        this.prop.setProperty("initial-directory", directory.getAbsolutePath());
    }

    public void save() {
        try {
            this.createDirectoriesIfNotExists();
            this.prop.storeToXML(new BufferedOutputStream(new FileOutputStream(FILE_PATH)), null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public File getConfigDir() {
        return CONFIG_DIR_PATH;
    }

    private void createDirectoriesIfNotExists() {
        if (!FILE_PATH.getParentFile().exists()) {
            FILE_PATH.getParentFile().mkdirs();
        }
    }

    public boolean isMaximized() {
        String value = this.prop.getProperty("maximized", "false");
        return Boolean.parseBoolean(value);
    }

    public void setMaximized(boolean maximized) {
        this.prop.setProperty("maximized", String.valueOf(maximized));
    }

    public double getWindowWidth() {
        String value = this.prop.getProperty("window.width", "1000.0");
        return Double.parseDouble(value);
    }

    public void setWindowWidth(double width) {
        this.prop.setProperty("window.width", String.valueOf(width));
    }

    public double getWindowHeight() {
        String value = this.prop.getProperty("window.height", "600.0");
        return Double.parseDouble(value);
    }

    public void setWindowHeight(double height) {
        this.prop.setProperty("window.height", String.valueOf(height));
    }
}
