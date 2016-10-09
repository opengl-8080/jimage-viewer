package jimgv.model;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class Book {

    private File directory;
    private File[] files;
    private int currentRightIndex;

    public void setDirectory(File directory) {
        this.directory = directory;
        this.files = this.directory.listFiles(File::isFile);
        Arrays.sort(this.files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        this.currentRightIndex = 0;
    }

    public Optional<File> getRight() {
        if (this.directory == null) {
            throw new IllegalStateException("ディレクトリが設定されていません");
        }

        if (this.files.length == 0) {
            return Optional.empty();
        }

        return Optional.of(this.files[this.currentRightIndex]);
    }

    public Optional<File> getLeft() {
        if (this.directory == null) {
            throw new IllegalStateException("ディレクトリが設定されていません");
        }

        if (this.files.length <= this.currentRightIndex + 1) {
            return Optional.empty();
        }

        return Optional.of(this.files[this.currentRightIndex + 1]);
    }

    public void nextPage() {
        if (this.directory == null) {
            throw new IllegalStateException("ディレクトリが設定されていません");
        }

        if (this.currentRightIndex + 2 < this.files.length) {
            this.currentRightIndex += 2;
        }
    }

    public void previousPage() {
        if (this.directory == null) {
            throw new IllegalStateException("ディレクトリが設定されていません");
        }

        if (0 <= this.currentRightIndex - 2) {
            this.currentRightIndex -= 2;
        }
    }

    int getRightIndex() {
        return this.currentRightIndex;
    }

    int size() {
        return this.files.length;
    }

    public boolean isInitialized() {
        return this.directory != null;
    }
}
