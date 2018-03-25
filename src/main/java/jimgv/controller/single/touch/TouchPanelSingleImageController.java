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

        root.setOnContextMenuRequested(e -> {
            contextMenu.hide();
            contextMenu.show(root, e.getScreenX(), e.getScreenY());
        });
        
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
                imageViewModel.translate(dx, 0);
                animateMovingPage();
            }
        });
        
        touchGesture.onSingleTouchReleased((dx, dy) -> {
            if (!imageViewModel.isZoomed()) {
                movePage();
                imageViewModel.reset();
            }

            imageViewModel.setOpacity(1.0);
        });
        
        touchGesture.onZoomed(zoomFactor -> {
            imageViewModel.zoom(zoomFactor);
        });

        touchGesture.onZoomFinished(() -> {
            imageViewModel.finishZoom();
        });
    }

    private void animateMovingPage() {
        double movedRate = Math.abs(imageViewModel.getTranslateX()) / stage.getWidth();

        if (movedRate < DX_FROM) {
            imageViewModel.setOpacity(1.0);
        } else if (DX_FROM <= movedRate && movedRate < DX_TO) {
            imageViewModel.setOpacity(DY*(DX_TO-movedRate)/DX + OPACITY_TO);
        } else {
            imageViewModel.setOpacity(OPACITY_TO);
        }
    }

    private void movePage() {
        double movedRate = Math.abs(imageViewModel.getTranslateX()) / stage.getWidth();

        if (movedRate <= DX_FROM) {
            return;
        }

        if (imageViewModel.getTranslateX() < 0) {
            imageViewModel.loadPreviousImage();
        } else {
            imageViewModel.loadNextImage();
        }
    }
}
