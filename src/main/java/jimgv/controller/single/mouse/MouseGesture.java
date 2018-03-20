package jimgv.controller.single.mouse;

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

    public void bind(Node root) {
        root.setOnMouseReleased(e -> onMouseReleased());
        root.setOnMouseDragged(this::onMouseDragged);
        root.setOnScroll(this::onScrolled);
        root.setOnMousePressed(this::onMousePressed);
    }
    
    private BiConsumer<Double, Double> leftDraggedListener;
    public void onLeftDragged(BiConsumer<Double, Double> leftDraggedListener) {
        this.leftDraggedListener = leftDraggedListener;
    }
    
    private Consumer<Double> rightScrolledListener;
    public void onRightScrolled(Consumer<Double> rightScrolledListener) {
        this.rightScrolledListener = rightScrolledListener;
    }
    
    private Consumer<Double> scrolledListener;
    public void onScrolled(Consumer<Double> scrolledListener) {
        this.scrolledListener = scrolledListener;
    }
    
    private BiConsumer<Double, Double> mousePressedListener;
    public void onMousePressed(BiConsumer<Double, Double> mousePressedListener) {
        this.mousePressedListener = mousePressedListener;
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
            leftDraggedListener.accept(dx, dy);
            previousScreenX = e.getScreenX();
            previousScreenY = e.getScreenY();
        }
    }
    
    private void onScrolled(ScrollEvent e) {
        if (rightClicked) {
            rightScrolledListener.accept(e.getDeltaY());
        }
        
        if (!leftClicked && !rightClicked) {
            scrolledListener.accept(e.getDeltaY());
        }
    }
}
