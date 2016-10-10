package jimgv.model;

import jimgv.model.config.BookConfig;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Book {

    private final File directory;
    private File[] files;
    private BookPages pages;

    public Book(File directory) {
        this.directory = directory;
        this.files = directory.listFiles(File::isFile);

        if (this.files == null) {
            this.files = new File[0];
        }
        Arrays.sort(this.files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        this.pages = new BookPages(this.files.length);
    }

    public boolean switchStartWithLeft() {
        this.pages.switchStartWithLeft();
        return this.pages.isStartWithLeft();
    }

    public Optional<File> getRight() {
        int rightIndex = this.pages.getRightIndex();
        return rightIndex == -1 ? Optional.empty() : Optional.of(this.files[rightIndex]);
    }

    public Optional<File> getLeft() {
        int leftIndex = this.pages.getLeftIndex();
        return leftIndex == -1 ? Optional.empty() : Optional.of(this.files[leftIndex]);
    }

    public void nextPage() {
        this.pages.nextPage();
    }

    public void previousPage() {
        this.pages.previousPage();
    }

    int getRightIndex() {
        return this.pages.getRightIndex();
    }

    int size() {
        return this.files.length;
    }

    void setStartWithLeft(boolean startWithLeft) {
        this.pages.setStartWithLeft(startWithLeft);
    }

    public File getDirectory() {
        return this.directory;
    }

    public BookConfig toBookConfig() {
        BookConfig bookConfig = new BookConfig();
        bookConfig.path = this.directory.getAbsolutePath();
        bookConfig.startWithLeft = this.pages.isStartWithLeft();
        return bookConfig;
    }

    public boolean isStartWithLeft() {
        return this.pages.isStartWithLeft();
    }
}
