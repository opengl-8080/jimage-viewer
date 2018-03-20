package jimgv.controller.single;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jimgv.model.single.OpenedDirectory;

import java.nio.file.Path;

public class ImageViewModel {
    private final ImageView imageView;
    private OpenedDirectory openedDirectory;
    private boolean zooming;
    private DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);

    protected ImageViewModel(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.scaleXProperty().bind(zoomScale);
        this.imageView.scaleYProperty().bind(zoomScale);
    }

    /**
     * 指定した画像を読み込む.
     * @param imagePath 画像ファイルのパス
     */
    public void open(Path imagePath) {
        loadImage(imagePath);
        openedDirectory = new OpenedDirectory(imagePath);
    }

    /**
     * 前の画像を読み込む.
     */
    public void loadPreviousImage() {
        loadImage(openedDirectory.previous());
    }

    /**
     * 次の画像を読み込む.
     */
    public void loadNextImage() {
        loadImage(openedDirectory.next());
    }

    private void loadImage(Path imagePath) {
        Image image = new Image(imagePath.toUri().toString());
        imageView.setImage(image);
        reset();
    }

    /**
     * 画像の位置・ズームを初期状態に戻す.
     */
    public void reset() {
        zoomScale.set(1.0);
        finishZoom();
        resetTranslation();
    }

    /**
     * 画像の位置を初期位置に戻す.
     */
    private void resetTranslation() {
        imageView.setTranslateX(0.0);
        imageView.setTranslateY(0.0);
    }

    /**
     * 拡大率を指定して画像を拡大・縮小する.
     * <p>
     * 現在のスケールに、指定した拡大率を乗算した値が新しいスケールとして設定されます.<br>
     * つまり、 0.0 - 1.0 の間は縮小、 1.0 より大きい場合は拡大になります.
     * <p>
     * 拡大・縮小の結果が規定のサイズを超える場合は、規定のサイズ内に収まるように
     * スケールは調整されます.
     *
     * @param rate 拡大率 (0.0 より大きい値)
     */
    public void zoom(double rate) {
        double scale = zoomScale.get() * rate;
        if (scale < 1.0) {
            scale = 1.0;
        } else if (5.0 < scale) {
            scale = 5.0;
        }
        zoomScale.set(scale);
    }

    /**
     * 画像を１段階拡大する.
     */
    public void zoomIn() {
        double currentScale = zoomScale.get();
        double scale = currentScale + 0.1;
        if (5.0 < scale) {
            scale = 5.0;
        }
        zoomScale.set(scale);
        zooming = true;
    }

    /**
     * 画像を１段階縮小する.
     */
    public void zoomOut() {
        double currentScale = zoomScale.get();
        double scale = currentScale - 0.1;
        if (scale < 1.0) {
            scale = 1.0;
        }
        zoomScale.set(scale);
    }

    /**
     * 現在ズーム操作の最中かどうかを確認する.
     * @return ズーム中の場合は true
     */
    public boolean isZooming() {
        return zooming;
    }

    /**
     * ズーム操作が終了し、画像が拡大表示されていることを確認する.
     * @return 拡大表示されている場合は true
     */
    public boolean isZoomed() {
        return !isZooming() && 1.0 < zoomScale.get();
    }

    /**
     * ズーム操作が終了したことを通知する.
     * <p>
     * この操作が終了するまで、ズーム操作中の扱いになり、 {@link #isZooming()} は true を返します.
     * </p>
     */
    public void finishZoom() {
        zooming = false;
        if (!isZoomed()) {
            resetTranslation();
        }
    }

    /**
     * 画像の位置を移動させる.
     * @param dx 横軸方向の変位
     * @param dy 縦軸方向の変位
     */
    public void translate(double dx, double dy) {
        imageView.setTranslateX(imageView.getTranslateX() + dx);
        imageView.setTranslateY(imageView.getTranslateY() + dy);
    }
}
