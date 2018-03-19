package jimgv.controller.single;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MouseGesture {
    private boolean leftClicked;
    private boolean rightClicked;
    private double screenX;
    private double screenY;
    private BiConsumer<Double, Double> leftDragListener;
    private Consumer<Double> rightScrollListener;
    private Consumer<Double> scrollListener;

    public void setLeftDragListener(BiConsumer<Double, Double> leftDragListener) {
        this.leftDragListener = leftDragListener;
    }

    public void setRightScrollListener(Consumer<Double> rightScrollListener) {
        this.rightScrollListener = rightScrollListener;
    }

    public void setScrollListener(Consumer<Double> scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void onMousePressed(TouchEvent e) {
//        leftClicked = e.isPrimaryButtonDown();
//        rightClicked = e.isSecondaryButtonDown();
        if (e.getTouchCount() == 1) {
            leftClicked = true;
            screenX = e.getTouchPoint().getScreenX();
            screenY = e.getTouchPoint().getScreenY();
        }
    }
    
    public void onMouseReleased() {
        leftClicked = false;
        rightClicked = false;
    }
    
    public void onMouseDragged(TouchEvent e) {
        if (leftClicked && e.getTouchCount() == 1) {
            double dx = e.getTouchPoint().getScreenX() - screenX;
            double dy = e.getTouchPoint().getScreenY() - screenY;
            leftDragListener.accept(dx, dy);
        }
    }
    
    public void onScroll(ScrollEvent e) {
        if (rightClicked) {
            rightScrollListener.accept(e.getDeltaY());
        }
        
        if (!leftClicked && !rightClicked) {
            scrollListener.accept(e.getDeltaY());
        }
    }
}
