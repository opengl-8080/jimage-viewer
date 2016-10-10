package jimgv.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BookConfigMap {
    private final File file;
    private Map<File, String> map = new HashMap<>();

    public static BookConfigMap getDefault() {
        File booksConfigDir = Configuration.getInstance().getBooksConfigDirectory();
        File mappingFile = new File(booksConfigDir, "mapping.txt");
        return new BookConfigMap(mappingFile);
    }

    public BookConfigMap(File file) {
        this.file = file;

        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        try (Stream<String> lines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                String[] split = line.split("\\|");
                this.map.put(new File(split[0]), split[1]);
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void put(File bookDir, String uuid) {
        this.map.put(bookDir, uuid);
    }

    public boolean exists(File bookDir) {
        return this.map.containsKey(bookDir);
    }

    public String get(File bookDir) {
        return this.map.get(bookDir);
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(this.file.toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            for (Map.Entry<File, String> entry : this.map.entrySet()) {
                writer.write(entry.getKey().getAbsolutePath() + "|" + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
