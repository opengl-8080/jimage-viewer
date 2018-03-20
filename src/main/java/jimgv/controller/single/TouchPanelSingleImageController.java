package jimgv.controller.single;

public class TouchPanelSingleImageController extends SingleImageControllerBase {
    private TouchGesture touchGesture = new TouchGesture();

    @Override
    protected ImageViewModel createImageViewModel() {
        return new TouchControlImageViewModel(stage, imageView);
    }

    @Override
    protected void initGestureHandlers() {
        touchGesture.bind(root);

        touchGesture.onSingleTouchPressed((x, y) -> {
            contextMenu.hide();
        });
        
        touchGesture.onDoubleTapped((x, y) -> {
            imageViewModel.reset();
        });

        touchGesture.onSingleTouchMoved((dx, dy) -> {
            imageViewModel.translate(dx, dy);
        });
        
        touchGesture.onSingleTouchReleased((dx, dy) -> {
            if (!imageViewModel.isZoomed()) {
                imageViewModel.reset();
                double rate = Math.abs(dx) / stage.getWidth();

                double E = 0.15;

                if (E < rate) {
                    if (dx < 0) {
                        imageViewModel.loadPreviousImage();
                    } else {
                        imageViewModel.loadNextImage();
                    }
                }
            }

            imageView.setOpacity(1.0);
        });
        
        touchGesture.onZoomed(rate -> {
            if (rate < 1.0) {
                imageViewModel.zoomDown();
            } else {
                imageViewModel.zoomUp();
            }
        });

        touchGesture.onZoomFinished(() -> {
            imageViewModel.finishZoom();
        });
    }
}
