package jimgv.controller.single.touch;

import javafx.scene.Node;
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

    public void bind(Node root) {
        root.setOnTouchPressed(this::onSingleTouchPressed);
        root.setOnTouchReleased(this::onTouchReleased);
        root.setOnTouchMoved(this::onTouchMoved);
        root.setOnMouseClicked(this::onMouseClicked);
        root.setOnZoom(this::onZoomed);
        root.setOnZoomFinished(e -> onZoomFinished());
    }
    
    private BiConsumer<Double, Double> singleTouchPressedListener;
    public void onSingleTouchPressed(BiConsumer<Double, Double> singleTouchPressedListener) {
        this.singleTouchPressedListener = singleTouchPressedListener;
    }

    private Runnable zoomFinishedListener;
    public void onZoomFinished(Runnable zoomFinishedListener) {
        this.zoomFinishedListener = zoomFinishedListener;
    }

    private Consumer<Double> zoomedListener;
    public void onZoomed(Consumer<Double> zoomedListener) {
        this.zoomedListener = zoomedListener;
    }

    private BiConsumer<Double, Double> doubleTappedListener;
    public void onDoubleTapped(BiConsumer<Double, Double> doubleTappedListener) {
        this.doubleTappedListener = doubleTappedListener;
    }

    private BiConsumer<Double, Double> singleTouchMovedListener;
    public void onSingleTouchMoved(BiConsumer<Double, Double> singleTouchMovedListener) {
        this.singleTouchMovedListener = singleTouchMovedListener;
    }

    private BiConsumer<Double, Double> singleTouchReleasedListener;
    public void onSingleTouchReleased(BiConsumer<Double, Double> singleTouchReleasedListener) {
        this.singleTouchReleasedListener = singleTouchReleasedListener;
    }

    private void onSingleTouchPressed(TouchEvent e) {
        if (e.getTouchCount() == 1) {
            touched = true;
            screenX = e.getTouchPoint().getScreenX();
            screenY = e.getTouchPoint().getScreenY();
            previousScreenX = screenX;
            previousScreenY = screenY;
            
            singleTouchPressedListener.accept(screenX, screenY);
        }
    }

    private void onTouchMoved(TouchEvent e) {
        if (touched && e.getTouchCount() == 1) {
            double dx = e.getTouchPoint().getScreenX() - previousScreenX;
            double dy = e.getTouchPoint().getScreenY() - previousScreenY;
            singleTouchMovedListener.accept(dx, dy);
            previousScreenX = e.getTouchPoint().getScreenX();
            previousScreenY = e.getTouchPoint().getScreenY();
        }
    }

    private void onTouchReleased(TouchEvent e) {
        if (touched && e.getTouchCount() == 1) {
            double dx = e.getTouchPoint().getScreenX() - screenX;
            double dy = e.getTouchPoint().getScreenY() - screenY;
            singleTouchReleasedListener.accept(dx, dy);
        }

        touched = false;
    }

    private void onMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            doubleTappedListener.accept(e.getScreenX(), e.getScreenY());
        }
    }

    private void onZoomed(ZoomEvent e) {
        zoomedListener.accept(e.getZoomFactor());
    }

    private void onZoomFinished() {
        zoomFinishedListener.run();
    }
}
