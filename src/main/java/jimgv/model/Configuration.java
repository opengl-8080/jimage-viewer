package jimgv.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public class Configuration {
    private static final File FILE_PATH = new File("./config/jimg.xml");
    private static final Configuration instance = new Configuration();

    public static Configuration getInstance() {
        return Configuration.instance;
    }

    private Properties prop;

    private Configuration() {
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

    private void createDirectoriesIfNotExists() {
        if (!FILE_PATH.getParentFile().exists()) {
            FILE_PATH.getParentFile().mkdirs();
        }
    }
}
