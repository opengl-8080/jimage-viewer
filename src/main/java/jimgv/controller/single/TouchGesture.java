package jimgv.controller.single;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TouchGesture {
    private boolean touched;
    private double screenX;
    private double screenY;
    private double previousScreenX;
    private double previousScreenY;

    private Runnable zoomFinishedListener;
    public void setZoomFinishedListener(Runnable zoomFinishedListener) {
        this.zoomFinishedListener = zoomFinishedListener;
    }

    private Consumer<Double> zoomListener;
    public void setZoomListener(Consumer<Double> zoomListener) {
        this.zoomListener = zoomListener;
    }

    private BiConsumer<Double, Double> doubleTapListener;
    public void setDoubleTapListener(BiConsumer<Double, Double> doubleTapListener) {
        this.doubleTapListener = doubleTapListener;
    }

    private BiConsumer<Double, Double> touchMoveListener;
    public void setTouchMoveListener(BiConsumer<Double, Double> touchMoveListener) {
        this.touchMoveListener = touchMoveListener;
    }

    private BiConsumer<Double, Double> touchReleasedListener;
    public void setTouchReleasedListener(BiConsumer<Double, Double> touchReleasedListener) {
        this.touchReleasedListener = touchReleasedListener;
    }

    public void onTouchPressed(TouchEvent e) {
        if (e.getTouchCount() == 1) {
            touched = true;
            screenX = e.getTouchPoint().getScreenX();
            screenY = e.getTouchPoint().getScreenY();
            previousScreenX = screenX;
            previousScreenY = screenY;
        }
    }

    public void onTouchMoved(TouchEvent e) {
        if (touched && e.getTouchCount() == 1) {
            double dx = e.getTouchPoint().getScreenX() - previousScreenX;
            double dy = e.getTouchPoint().getScreenY() - previousScreenY;
            touchMoveListener.accept(dx, dy);
            previousScreenX = e.getTouchPoint().getScreenX();
            previousScreenY = e.getTouchPoint().getScreenY();
        }
    }

    public void onTouchReleased(TouchEvent e) {
        if (touched && e.getTouchCount() == 1) {
            double dx = e.getTouchPoint().getScreenX() - screenX;
            double dy = e.getTouchPoint().getScreenY() - screenY;
            touchReleasedListener.accept(dx, dy);
        }

        touched = false;
    }

    public void onMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            doubleTapListener.accept(e.getScreenX(), e.getScreenY());
        }
    }

    public void onZoom(ZoomEvent e) {
        zoomListener.accept(e.getZoomFactor());
    }

    public void onZoomFinished() {
        zoomFinishedListener.run();
    }
}
