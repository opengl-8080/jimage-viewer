package jimgv.controller.single;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class SingleImage {
    private ImageView imageView;
    private boolean zoom;
    private double translateX;
    private double translateY;

    public SingleImage(ImageView imageView) {
        this.imageView = imageView;
    }
    
    public void load(Path imagePath) {
        Image image = new Image(imagePath.toUri().toString());
        imageView.setImage(image);
        resetZoom();
    }
    
    public void startZoom(double delta) {
        zoom = true;

        double scale = imageView.getScaleX() + (delta / 400.0);

        if (0.5 <= scale && scale <= 100.0) {
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
        }
    }

    public boolean isZooming() {
        return zoom;
    }
    
    public void stopZoom() {
        zoom = false;
    }
    
    public void startTranslate() {
        translateX = imageView.getTranslateX();
        translateY = imageView.getTranslateY();
    }
    
    public void stopTranslate(double dx, double dy) {
        imageView.setTranslateX(translateX + dx);
        imageView.setTranslateY(translateY + dy);
    }
    
    public void resetZoom() {
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
        imageView.setTranslateX(0.0);
        imageView.setTranslateY(0.0);
    }
}
