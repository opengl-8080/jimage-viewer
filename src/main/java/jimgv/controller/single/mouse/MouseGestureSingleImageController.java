package jimgv.controller.single.mouse;

import jimgv.controller.single.SingleImageControllerBase;

public class MouseGestureSingleImageController extends SingleImageControllerBase {
    private MouseGesture mouseGesture = new MouseGesture();
    private boolean rightClicked;
    private boolean changingPage;

    @Override
    public void initGestureHandlers() {
        mouseGesture.bind(root);

        mouseGesture.onMousePressed(e -> {
            contextMenu.hide();
            
            rightClicked = e.isSecondaryButtonDown();
        });
        
        mouseGesture.onMouseReleased(e -> {
            contextMenu.hide();

            if (rightClicked && !changingPage) {
                contextMenu.show(root, e.getScreenX(), e.getScreenY());
            }

            rightClicked = false;
            changingPage = false;
        });
        
        mouseGesture.onLeftDragged((dx, dy) -> {
            if (imageViewModel.isZoomed()) {
                imageViewModel.translate(dx, dy);
            }
        });

        mouseGesture.onRightScrolled(delta -> {
            if (delta < 0) {
                imageViewModel.loadPreviousImage();
            } else {
                imageViewModel.loadNextImage();
            }
            changingPage = true;
        });

        mouseGesture.onScrolled(delta -> {
            if (delta < 0) {
                imageViewModel.zoomOut();
            } else {
                imageViewModel.zoomIn();
            }
            imageViewModel.finishZoom();
        });
    }
}
