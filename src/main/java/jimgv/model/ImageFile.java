package jimgv.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ImageFile {
    private static final List<String> EXTENSIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif");

    /**
     * 指定したパスがサポートされている画像ファイルかどうかを判定する.
     * @param path パス
     * @return 画像ファイルの場合は true
     */
    public static boolean isImageFile(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }

        String name = path.getFileName().toString();
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex == -1) {
            return false;
        }
        
        String extension = name.substring(dotIndex);
        return EXTENSIONS.contains(extension.toLowerCase());
    }
}
