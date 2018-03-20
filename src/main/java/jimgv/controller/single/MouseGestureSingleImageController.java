package jimgv.controller.single;

public class MouseGestureSingleImageController extends SingleImageControllerBase {
    private MouseGesture mouseGesture = new MouseGesture();

    @Override
    protected ImageViewModel createImageViewModel() {
        return new MouseControlImageViewModel(imageView);
    }

    @Override
    public void initGestureHandlers() {
        mouseGesture.bind(root);

        mouseGesture.onMousePressed((x, y) -> {
            imageViewModel.finishZoom();
            contextMenu.hide();
        });
        
        mouseGesture.onLeftDragged((dx, dy) -> {
            imageViewModel.translate(dx, dy);
        });

        mouseGesture.onRightScrolled(deltaY -> {
            if (deltaY < 0) {
                imageViewModel.zoomDown();
            } else {
                imageViewModel.zoomUp();
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
