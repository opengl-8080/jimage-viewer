package jimgv.controller.single;

public class TouchPanelSingleImageController extends SingleImageControllerBase {
    private TouchGesture touchGesture = new TouchGesture();

    @Override
    protected ImageViewModel createImageViewModel() {
        return new TouchControlImageViewModel(stage, imageView);
    }

    @Override
    protected void initGestureHandlers() {
        touchGesture.setDoubleTapListener((x, y) -> {
            imageViewModel.reset();
        });

        touchGesture.setTouchMoveListener((dx, dy) -> {
            imageViewModel.translate(dx, dy);
        });
        
        touchGesture.setTouchReleasedListener((dx, dy) -> {
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
        
        touchGesture.setZoomListener(rate -> {
            if (rate < 1.0) {
                imageViewModel.zoomDown();
            } else {
                imageViewModel.zoomUp();
            }
        });

        touchGesture.setZoomFinishedListener(() -> {
            imageViewModel.finishZoom();
        });
        
        root.setOnTouchPressed(e -> {
            contextMenu.hide();
            touchGesture.onTouchPressed(e);
        });
        
        root.setOnTouchReleased(e -> {
            touchGesture.onTouchReleased(e);
        });
        
        root.setOnTouchMoved(e -> {
            touchGesture.onTouchMoved(e);
        });
        
        root.setOnMouseClicked(e -> {
            touchGesture.onMouseClicked(e);
        });
        
        root.setOnZoom(e -> {
            touchGesture.onZoom(e);
        });
        
        root.setOnZoomFinished(e -> {
            touchGesture.onZoomFinished();
        });
    }
}
