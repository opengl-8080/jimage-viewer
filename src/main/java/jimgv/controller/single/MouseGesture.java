package jimgv.controller.single;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MouseGesture {
    private boolean leftClicked;
    private boolean rightClicked;
    private double previousScreenX;
    private double previousScreenY;
    private BiConsumer<Double, Double> leftDragListener;
    private Consumer<Double> rightScrollListener;
    private Consumer<Double> scrollListener;
    private BiConsumer<Double, Double> mousePressedListener;

    public void onMousePressed(BiConsumer<Double, Double> mousePressedListener) {
        this.mousePressedListener = mousePressedListener;
    }
    
    public void onLeftDrag(BiConsumer<Double, Double> leftDragListener) {
        this.leftDragListener = leftDragListener;
    }

    public void onRightScroll(Consumer<Double> rightScrollListener) {
        this.rightScrollListener = rightScrollListener;
    }

    public void onScroll(Consumer<Double> scrollListener) {
        this.scrollListener = scrollListener;
    }

    private void onMousePressed(MouseEvent e) {
        leftClicked = e.isPrimaryButtonDown();
        rightClicked = e.isSecondaryButtonDown();
        previousScreenX = e.getScreenX();
        previousScreenY = e.getScreenY();
        mousePressedListener.accept(e.getScreenX(), e.getScreenY());
    }
    
    private void onMouseReleased() {
        leftClicked = false;
        rightClicked = false;
    }
    
    private void onMouseDragged(MouseEvent e) {
        if (leftClicked) {
            double dx = e.getScreenX() - previousScreenX;
            double dy = e.getScreenY() - previousScreenY;
            leftDragListener.accept(dx, dy);
            previousScreenX = e.getScreenX();
            previousScreenY = e.getScreenY();
        }
    }
    
    private void onScroll(ScrollEvent e) {
        if (rightClicked) {
            rightScrollListener.accept(e.getDeltaY());
        }
        
        if (!leftClicked && !rightClicked) {
            scrollListener.accept(e.getDeltaY());
        }
    }

    public void bind(Node root) {
        root.setOnMouseReleased(e -> onMouseReleased());
        root.setOnMouseDragged(this::onMouseDragged);
        root.setOnScroll(this::onScroll);
        root.setOnMousePressed(this::onMousePressed);
    }
}
