package jimgv.controller.single;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

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

    public void onMousePressed(MouseEvent e) {
        leftClicked = e.isPrimaryButtonDown();
        rightClicked = e.isSecondaryButtonDown();
        screenX = e.getScreenX();
        screenY = e.getScreenY();
    }
    
    public void onMouseReleased() {
        leftClicked = false;
        rightClicked = false;
    }
    
    public void onMouseDragged(MouseEvent e) {
        if (leftClicked) {
            double dx = e.getScreenX() - screenX;
            double dy = e.getScreenY() - screenY;
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
