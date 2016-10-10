package jimgv.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class FileUtils {

    public static void createNewFile(File file) {
        if (file.exists()) {
            return;
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private FileUtils() {}
}
