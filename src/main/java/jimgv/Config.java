package jimgv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.Properties;

public class Config {
    private static final Path CONFIG_FILE = Paths.get("./jimage-viewer.xml");
    private static final String LAST_OPENED_DIRECTORY = "last-opened-directory";
    
    public static Config getInstance() {
        return Config.instance;
    }

    private static final Config instance = new Config();
    private final Properties prop = new Properties();
    
    public Optional<Path> getLastOpenedDirectory() {
        String directory = this.prop.getProperty(LAST_OPENED_DIRECTORY);
        if (directory != null) {
            return Optional.of(Paths.get(directory));
        } else {
            return Optional.empty();
        }
    }
    
    public void setLastOpenedDirectory(Path path) {
        this.prop.setProperty(LAST_OPENED_DIRECTORY, path.toString());
        this.save();
    }
    
    private Config() {
        if (Files.exists(CONFIG_FILE)) {
            try (InputStream in = Files.newInputStream(CONFIG_FILE, StandardOpenOption.READ)) {
                prop.loadFromXML(in);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            this.save();
        }
    }
    
    private void save() {
        try {
            try (OutputStream out = Files.newOutputStream(CONFIG_FILE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                prop.storeToXML(out, "jimage-viewer");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
