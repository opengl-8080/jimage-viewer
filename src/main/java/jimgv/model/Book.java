package jimgv.model;

import jimgv.model.config.BookConfig;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Book {

    private Set<Runnable> changeListeners = new HashSet<>();
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
        return this.getFile(rightIndex);
    }

    public Optional<File> getLeft() {
        int leftIndex = this.pages.getLeftIndex();
        return this.getFile(leftIndex);
    }

    private Optional<File> getFile(int index) {
        return index == -1 ? Optional.empty() : Optional.of(this.files[index]);
    }

    public void nextPage() {
        this.changePage(this.pages::nextPage);
    }

    public void previousPage() {
        this.changePage(this.pages::previousPage);
    }

    public void home() {
        this.changePage(this.pages::home);
    }

    public void end() {
        this.changePage(this.pages::end);
    }

    private void changePage(Runnable change) {
        int oldValue = this.pages.getCurrentPageNumber();
        change.run();
        int newValue = this.pages.getCurrentPageNumber();

        if (oldValue != newValue) {
            this.changeListeners.forEach(Runnable::run);
        }
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
        bookConfig.currentPageNumber = this.pages.getCurrentPageNumber();

        return bookConfig;
    }

    public boolean isStartWithLeft() {
        return this.pages.isStartWithLeft();
    }

    public void addChangeListener(Runnable listener) {
        this.changeListeners.add(listener);
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.pages.setCurrentPageNumber(currentPageNumber);
    }
}
