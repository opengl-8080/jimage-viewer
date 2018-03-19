package jimgv.controller.single;

import java.nio.file.Path;

public interface ImageViewModel {
    /**
     * 指定した画像を読み込む.
     * @param imagePath 画像ファイルのパス
     */
    void open(Path imagePath);

    /**
     * 前の画像を読み込む.
     */
    void loadPreviousImage();

    /**
     * 次の画像を読み込む.
     */
    void loadNextImage();

    /**
     * 画像の位置・ズームを初期状態に戻す.
     */
    void reset();

    /**
     * 現在ズーム操作の最中かどうかを確認する.
     * @return ズーム中の場合は true
     */
    boolean isZooming();

    /**
     * ズーム操作が終了し、画像が拡大表示されていることを確認する.
     * @return 拡大表示されている場合は true
     */
    boolean isZoomed();

    /**
     * 画像を１段階拡大する.
     */
    void zoomUp();

    /**
     * 画像を１段階縮小する.
     */
    void zoomDown();

    /**
     * ズーム操作が終了したことを通知する.
     * <p>
     * この操作が終了するまで、ズーム操作中の扱いになり、 {@link #isZooming()} は true を返します.
     * </p>
     */
    void finishZoom();

    /**
     * 画像の位置を移動させる.
     * @param dx 横軸方向の変位
     * @param dy 縦軸方向の変位
     */
    void translate(double dx, double dy);
}
