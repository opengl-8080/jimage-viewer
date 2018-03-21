package jimgv.model.book;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jimgv.model.ImageFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Book {
    
    private BooleanProperty startOnLeft = new SimpleBooleanProperty(false);
    private final Path directory;
    private List<Path> imageList;
    private int rightIndex;

    public Book(Path directory) {
        this.directory = directory;
        open();
    }
    
    private void open() {
        try {
            imageList = Files.list(directory)
                    .filter(ImageFile::isImageFile)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public Optional<Path> getRightImage() {
        if (isStartOnLeft()) {
            if (rightIndex == 0) {
                return Optional.empty();
            } else {
                return Optional.of(imageList.get(rightIndex - 1));
            }
        } else {
            return Optional.of(imageList.get(rightIndex));
        }
    }
    
    public Optional<Path> getLeftImage() {
        if (isStartOnLeft()) {
            return Optional.of(imageList.get(rightIndex));
        } else {
            int leftIndex = rightIndex + 1;
            if (leftIndex < imageList.size()) {
                return Optional.of(imageList.get(leftIndex));
            } else {
                return Optional.empty();
            }
        }
    }

    public void previousPage() {
        rightIndex -= 2;
        if (rightIndex < 0) {
            rightIndex = 0;
        }
    }

    public void nextPage() {
        rightIndex += 2;
        if (imageList.size() <= rightIndex) {
            rightIndex = imageList.size() - 1;
        }
    }
    
    public BooleanProperty startOnLeftProperty() {
        return startOnLeft;
    }
    
    public void setStartOnLeft(boolean startOnLeft) {
        this.startOnLeft.setValue(startOnLeft);
    }
    
    private boolean isStartOnLeft() {
        return startOnLeft.get();
    }
}
