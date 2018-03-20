package jimgv.model.single;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OpenedDirectory {
    private static final List<String> IMAGE_EXTENSTIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif");
    private final List<Path> imagePathList;
    private int currentSelectedIndex;
    
    public OpenedDirectory(Path imagePath) {
        Path directory = imagePath.getParent();
        try {
            imagePathList = Files.list(directory)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        int lastIndex = fileName.lastIndexOf(".");
                        if (lastIndex == -1) {
                            return false;
                        }
                        String extension = fileName.substring(lastIndex);
                        return IMAGE_EXTENSTIONS.contains(extension);
                    })
                    .sorted()
                    .collect(Collectors.toList());
            currentSelectedIndex = imagePathList.indexOf(imagePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public Path previous() {
        currentSelectedIndex--;
        if (currentSelectedIndex < 0) {
            currentSelectedIndex = imagePathList.size() - 1;
        }
        
        return imagePathList.get(currentSelectedIndex);
    }
    
    public Path next() {
        currentSelectedIndex++;
        if (imagePathList.size() <= currentSelectedIndex) {
            currentSelectedIndex = 0;
        }

        return imagePathList.get(currentSelectedIndex);
    }
}
