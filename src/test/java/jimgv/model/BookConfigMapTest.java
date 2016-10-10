package jimgv.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;

public class BookConfigMapTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void name() throws Exception {
        // setup
        File mapFile = new File(folder.getRoot(), "map.txt");

        // exercise
        BookConfigMap map = new BookConfigMap(mapFile);
        File book1 = new File(folder.getRoot(), "book1");
        File book2 = new File(folder.getRoot(), "book2");
        map.put(book1, "uuid1");
        map.put(book2, "uuid2");
        map.save();

        // verify
        assertThat(Files.readAllLines(mapFile.toPath(), StandardCharsets.UTF_8))
                .containsExactlyInAnyOrder(book1.getAbsolutePath() + "|uuid1", book2.getAbsolutePath() + "|uuid2");
    }
}