package jimgv.controller.single.mouse;

import jimgv.controller.single.SingleImageControllerBase;

public class MouseGestureSingleImageController extends SingleImageControllerBase {
    private MouseGesture mouseGesture = new MouseGesture();

    @Override
    public void initGestureHandlers() {
        mouseGesture.bind(root);

        mouseGesture.onMousePressed((x, y) -> {
            imageViewModel.finishZoom();
            contextMenu.hide();
        });
        
        mouseGesture.onLeftDragged((dx, dy) -> {
            if (imageViewModel.isZoomed()) {
                imageViewModel.translate(dx, dy);
            }
        });

        mouseGesture.onRightScrolled(deltaY -> {
            if (deltaY < 0) {
                imageViewModel.zoomOut();
            } else {
                imageViewModel.zoomIn();
            }
        });

        mouseGesture.onScrolled(delta -> {
            if (delta < 0) {
                imageViewModel.loadPreviousImage();
            } else {
                imageViewModel.loadNextImage();
            }
        });
    }
}