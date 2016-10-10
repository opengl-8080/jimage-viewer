package jimgv.model;

import jimgv.model.config.BookConfig;
import jimgv.util.FileUtils;
import jimgv.util.UUIDs;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.Optional;

public class BookRepository {
    private static final String CONFIG_FILE_NAME = "book.xml";

    private BookConfigMap bookConfigMap;

    public BookRepository(BookConfigMap bookConfigMap) {
        this.bookConfigMap = bookConfigMap;
    }

    public void save(Book book) {
        this.saveConfigMapIfNotExists(book);

        String uuid = this.bookConfigMap.get(book.getDirectory());

        File bookXml = this.createConfigFile(uuid);
        FileUtils.createNewFile(bookXml);

        JAXB.marshal(book.toBookConfig(), bookXml);
    }

    private void saveConfigMapIfNotExists(Book book) {
        if (this.bookConfigMap.exists(book.getDirectory())) {
            return;
        }

        String uuid = UUIDs.newUUID();
        this.bookConfigMap.put(book.getDirectory(), uuid);
        this.bookConfigMap.save();
    }

    public Optional<Book> find(File bookDir) {
        if (!this.bookConfigMap.exists(bookDir)) {
            return Optional.empty();
        }

        String uuid = this.bookConfigMap.get(bookDir);

        File bookXml = this.createConfigFile(uuid);

        BookConfig bookConfig = JAXB.unmarshal(bookXml, BookConfig.class);
        Book book = new Book(new File(bookConfig.path));
        book.setStartWithLeft(bookConfig.startWithLeft);
        return Optional.of(book);
    }

    private File createConfigFile(String uuid) {
        File booksConfigDir = Configuration.getInstance().getBooksConfigDirectory();
        File bookXml = new File(booksConfigDir, uuid + "/" + CONFIG_FILE_NAME);
        return bookXml;
    }
}
