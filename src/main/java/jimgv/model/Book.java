package jimgv.model;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Book {

    private File[] files;
    private BookPages pages;

    public Book(File directory) {
        this.files = directory.listFiles(File::isFile);
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
}
