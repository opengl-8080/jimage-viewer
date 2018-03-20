package jimgv.controller.single;

import javafx.scene.image.ImageView;

public class MouseGestureSingleImageController extends SingleImageControllerBase {
    private MouseGesture mouseGesture = new MouseGesture();

    @Override
    protected ImageViewModel createImageViewModel(ImageView imageView) {
        return new MouseControlImageViewModel(imageView);
    }

    @Override
    public void initGestureHandlers() {
        root.setOnMousePressed(e -> {
            imageViewModel.finishZoom();
            contextMenu.hide();

            mouseGesture.onMousePressed(e);
        });

        root.setOnMouseReleased(e -> {
            mouseGesture.onMouseReleased();
        });

        root.setOnMouseDragged(e -> {
            mouseGesture.onMouseDragged(e);
        });

        root.setOnScroll(e -> {
            mouseGesture.onScroll(e);
        });
        
        mouseGesture.setLeftDragListener((dx, dy) -> {
            imageViewModel.translate(dx, dy);
        });

        mouseGesture.setRightScrollListener(deltaY -> {
            if (deltaY < 0) {
                imageViewModel.zoomDown();
            } else {
                imageViewModel.zoomUp();
            }
        });

        mouseGesture.setScrollListener(delta -> {
            if (delta < 0) {
                imageViewModel.loadPreviousImage();
            } else {
                imageViewModel.loadNextImage();
            }
        });
    }
}
