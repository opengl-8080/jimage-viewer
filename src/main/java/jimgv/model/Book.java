package jimgv.model;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Book {

    private File directory;
    private File[] files;
    private BookPages pages = new BookPages();

    public Book(File directory) {
        this.directory = directory;
        this.files = this.directory.listFiles(File::isFile);
        Arrays.sort(this.files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
    }

    public boolean switchStartWithLeft() {
        this.pages.switchStartWithLeft();
        return this.pages.isStartWithLeft();
    }

    public Optional<File> getRight() {
        if (this.files.length == 0 || this.pages.getRightIndex() < 0) {
            return Optional.empty();
        }

        return Optional.of(this.files[this.pages.getRightIndex()]);
    }

    public Optional<File> getLeft() {
        if (this.files.length <= this.pages.getLeftIndex()) {
            return Optional.empty();
        }

        return Optional.of(this.files[this.pages.getLeftIndex()]);
    }

    public void nextPage() {
        this.pages.nextPage();

        if (this.files.length <= this.pages.getRightIndex()) {
            this.pages.previousPage();
        }
    }

    public void previousPage() {
        this.pages.previousPage();

        if (this.pages.getRightIndex() < -1) {
            this.pages.nextPage();
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
}
