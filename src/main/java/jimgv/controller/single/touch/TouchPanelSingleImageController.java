package jimgv.controller.single.touch;

import jimgv.controller.single.SingleImageControllerBase;

public class TouchPanelSingleImageController extends SingleImageControllerBase {
    private TouchGesture touchGesture = new TouchGesture();
    
    private static final double OPACITY_FROM = 0.5;
    private static final double OPACITY_TO = 0.2;
    private static final double DY = OPACITY_FROM - OPACITY_TO;
    
    private static final double DX_FROM = 0.15;
    private static final double DX_TO = 0.4;
    private static final double DX = DX_TO - DX_FROM;
    
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
            if (imageViewModel.isZoomed()) {
                imageViewModel.translate(dx, dy);
            } else {
                double rate = Math.abs(imageView.getTranslateX()) / stage.getWidth();

                if (rate < DX_FROM) {
                    imageView.setOpacity(1.0);
                } else if (DX_FROM <= rate && rate < DX_TO) {
                    imageView.setOpacity(DY*(DX_TO-rate)/DX + OPACITY_TO);
                } else {
                    imageView.setOpacity(OPACITY_TO);
                }
                imageViewModel.translate(dx, 0);
            }
        });
        
        touchGesture.onSingleTouchReleased((dx, dy) -> {
            if (!imageViewModel.isZoomed()) {
                imageViewModel.reset();
                double rate = Math.abs(dx) / stage.getWidth();

                if (DX_FROM < rate) {
                    if (dx < 0) {
                        imageViewModel.loadPreviousImage();
                    } else {
                        imageViewModel.loadNextImage();
                    }
                }
            }

            imageView.setOpacity(1.0);
        });
        
        touchGesture.onZoomed(zoomFactor -> {
            imageViewModel.zoom(zoomFactor);
        });

        touchGesture.onZoomFinished(() -> {
            imageViewModel.finishZoom();
        });
    }
}
