package jimgv.model;

import jimgv.model.config.BookConfig;
import jimgv.util.UUIDs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * config/
 *   |-jimg.xml              グローバルな各種設定
 *   `-books-config/
 *       |-mapping.txt       ブックのパスと uuid のマッピングを保存
 *       |-(uuid)/           ブックごとの uuid
 *       |  `-book.xml       ブックごとの設定
 *       |-...
 */
public class BookRepositoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private BookRepository repository;
    private File booksConfigDir;
    private BookConfigMap bookConfigMap;

    @Before
    public void setUp() throws Exception {
        booksConfigDir = new File(folder.getRoot(), "config/books-config");
        bookConfigMap = new BookConfigMap(new File(booksConfigDir, "mapping.txt"));
        repository = new BookRepository(bookConfigMap);

        TestConfiguration testConfiguration = new TestConfiguration();
        testConfiguration.setBooksConfigDirectory(booksConfigDir);
        testConfiguration.setConfigDir(new File(folder.getRoot(), "config"));

        Configuration.setTestInstance(testConfiguration);
    }

    @Test
    public void 保存されているブックの情報を検索できる_存在する場合() throws Exception {
        // setup
        File bookXml = new File(booksConfigDir, "testuuid/book.xml");
        bookXml.getParentFile().mkdirs();
        BookConfig bookConfig = new BookConfig();
        bookConfig.path = "path-value";
        bookConfig.startWithLeft = true;
        JAXB.marshal(bookConfig, bookXml);

        File keyFile = new File("path1");
        bookConfigMap.put(keyFile, "testuuid");

        // exercise
        Optional<Book> book = repository.find(keyFile);

        // verify
        assertThat(book.get().getDirectory()).as("directory").isEqualTo(new File("path-value"));
        assertThat(book.get().isStartWithLeft()).as("startWithLeft").isTrue();
    }

    @Test
    public void 保存されているブックの情報を検索できる_保存されていない場合() throws Exception {
        // setup
        File keyFile = new File("path1");

        // exercise
        Optional<Book> book = repository.find(keyFile);

        // verify
        assertThat(book).isEmpty();
    }

    @Test
    public void ブックの情報を所定の場所に保存する() throws Exception {
        // setup
        File bookDir = writeBookFiles();

        UUIDs.setTestUUID("testuuid");

        Book book = new Book(bookDir);
        book.setStartWithLeft(true);

        // exercise
        repository.save(book);

        // verify
        BookConfig actual = JAXB.unmarshal(new File(booksConfigDir, "testuuid/book.xml"), BookConfig.class);

        BookConfig expected = new BookConfig();
        expected.path = bookDir.getAbsolutePath();
        expected.startWithLeft = true;
        expected.currentPageNumber = 1;

        assertThat(actual).isEqualTo(expected);
    }

    private File writeBookFiles() throws IOException {
        folder.newFolder("foo");
        folder.newFile("foo/001.jpg");
        folder.newFile("foo/002.jpg");
        folder.newFile("foo/003.jpg");

        return new File(folder.getRoot(), "foo");
    }
}