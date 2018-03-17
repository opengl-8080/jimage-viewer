package jimgv.model;

import jimgv.model.config.BookConfig;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Book {

    private Set<Runnable> changeListeners = new HashSet<>();
    private final File directory;
    private List<Page> pageList;
    private BookPages pages;

    public Book(File directory) {
        this.directory = directory;
        File[] files = directory.listFiles(File::isFile);

        if (files == null) {
            files = new File[0];
        }
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        this.pageList = Stream.of(files).map(Page::new).collect(Collectors.toList());
        this.pages = new BookPages(files.length);
    }

    public boolean switchStartWithLeft() {
        this.pages.switchStartWithLeft();
        return this.pages.isStartWithLeft();
    }

    public Optional<Page> getRight() {
        int rightIndex = this.pages.getRightIndex();
        return this.getFile(rightIndex);
    }

    public Optional<Page> getLeft() {
        int leftIndex = this.pages.getLeftIndex();
        return this.getFile(leftIndex);
    }

    private Optional<Page> getFile(int index) {
        return index == -1 ? Optional.empty() : Optional.of(this.pageList.get(index));
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
        return this.pageList.size();
    }

    void setStartWithLeft(boolean startWithLeft) {
        this.pages.setStartWithLeft(startWithLeft);
    }

    File getDirectory() {
        return this.directory;
    }

    BookConfig toBookConfig() {
        BookConfig bookConfig = new BookConfig();
        bookConfig.path = this.directory.getAbsolutePath();
        bookConfig.startWithLeft = this.pages.isStartWithLeft();
        bookConfig.currentPageNumber = this.pages.getCurrentPageNumber();

        return bookConfig;
    }

    public boolean isStartWithLeft() {
        return this.pages.isStartWithLeft();
    }

    void addChangeListener(Runnable listener) {
        this.changeListeners.add(listener);
    }

    void setCurrentPageNumber(int currentPageNumber) {
        this.pages.setCurrentPageNumber(currentPageNumber);
    }

    public void addIgnorePageAtCurrentPage() {
        int leftIndex = this.pages.getLeftIndex();
        this.pageList.add(leftIndex, Page.ignore());
        this.pages.setMaxSize(this.pageList.size());
    }

    public void removeIgnorePageAtCurrentPage() {
        int leftIndex = this.pages.getLeftIndex();
        this.pageList.remove(leftIndex);
        this.pages.setMaxSize(this.pageList.size());
    }

    public boolean hasIgnorePageAtCurrentPage() {
        return this.getLeft().map(Page::isIgnorePage).orElse(false);
    }

    public void addIgnorePages(List<Integer> ignorePageIndexes) {
        for (Integer ignorePageIndex : ignorePageIndexes) {
            this.pageList.add(ignorePageIndex, Page.ignore());
        }
        this.pages.setMaxSize(this.pageList.size());
    }
}
