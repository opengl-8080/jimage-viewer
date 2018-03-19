package jimgv.controller.single;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public abstract class ImageViewModelBase implements ImageViewModel {
    protected final ImageView imageView;
    private OpenedDirectory openedDirectory;
    private boolean zooming;
    private DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);

    protected ImageViewModelBase(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.scaleXProperty().bind(zoomScale);
        this.imageView.scaleYProperty().bind(zoomScale);
    }

    @Override
    public void open(Path imagePath) {
        loadImage(imagePath);
        openedDirectory = new OpenedDirectory(imagePath);
    }
    
    private void loadImage(Path imagePath) {
        Image image = new Image(imagePath.toUri().toString());
        imageView.setImage(image);
        reset();
    }

    @Override
    public void reset() {
        zoomScale.set(1.0);
        imageView.setTranslateX(0.0);
        imageView.setTranslateY(0.0);
        finishZoom();
    }

    @Override
    public void loadPreviousImage() {
        loadImage(openedDirectory.previous());
    }

    @Override
    public void loadNextImage() {
        loadImage(openedDirectory.next());
    }

    @Override
    public void zoomUp() {
        double currentScale = zoomScale.get();
        double scale = currentScale + 0.1;
        if (5.0 < scale) {
            scale = 5.0;
        }
        zoomScale.set(scale);
        zooming = true;
    }

    @Override
    public void zoomDown() {
        double currentScale = zoomScale.get();
        double scale = currentScale - 0.1;
        if (scale < 1.0) {
            scale = 1.0;
        }
        zoomScale.set(scale);
    }

    @Override
    public boolean isZooming() {
        return zooming;
    }

    @Override
    public boolean isZoomed() {
        return !isZooming() && 1.0 < zoomScale.get();
    }

    @Override
    public void finishZoom() {
        zooming = false;
    }
    
    @Override
    public void translate(double dx, double dy) {
        imageView.setTranslateX(imageView.getTranslateX() + dx);
        imageView.setTranslateY(imageView.getTranslateY() + dy);
    }
}
