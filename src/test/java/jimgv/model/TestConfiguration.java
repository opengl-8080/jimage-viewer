package jimgv.model;

import java.io.File;

public class TestConfiguration extends Configuration {

    private File initialDirectory;
    private File booksDirectory;
    private File configDir;

    @Override
    public File getBooksConfigDirectory() {
        return booksDirectory;
    }

    public void setBooksConfigDirectory(File booksDirectory) {
        this.booksDirectory = booksDirectory;
    }

    @Override
    public File getInitialDirectory() {
        return this.initialDirectory;
    }

    @Override
    public void setInitialDirectory(File directory) {
        this.initialDirectory = directory;
    }

    @Override
    public File getConfigDir() {
        return this.configDir;
    }

    public void setConfigDir(File configDir) {
        this.configDir = configDir;
    }

    @Override
    public void save() {
        // ignore
    }
}
