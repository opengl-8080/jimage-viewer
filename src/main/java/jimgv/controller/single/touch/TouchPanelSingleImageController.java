package jimgv.controller.single.touch;

import jimgv.controller.single.SingleImageControllerBase;
import jimgv.log.Logger;

public class TouchPanelSingleImageController extends SingleImageControllerBase {
    private static final Logger logger = Logger.INSTANCE;
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
            logger.debug("onSingleTouchPressed(%s, %s)", x, y);
            contextMenu.hide();
        });
        
        touchGesture.onDoubleTapped((x, y) -> {
            logger.debug("onDoubleTapped(%s, %s)", x, y);
            imageViewModel.reset();
        });

        touchGesture.onSingleTouchMoved((dx, dy) -> {
            logger.debug("onSingleTouchMoved(%s, %s)", dx, dy);
            if (imageViewModel.isZoomed()) {
                logger.debug("  isZoomed");
                imageViewModel.translate(dx, dy);
            } else {
                logger.debug("  !isZoomed");
                imageViewModel.translate(dx, 0);
                animateMovingPage();
            }
        });
        
        touchGesture.onSingleTouchReleased((dx, dy) -> {
            logger.debug("onSingleTouchReleased(%s, %s)", dx, dy);
            if (!imageViewModel.isZoomed()) {
                logger.debug("  isZoomed");
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
        logger.debug("animateMovingPage");
        double movedRate = Math.abs(imageViewModel.getTranslateX()) / stage.getWidth();

        if (movedRate < DX_FROM) {
            logger.debug("  setOpacity(1.0)");
            imageViewModel.setOpacity(1.0);
        } else if (DX_FROM <= movedRate && movedRate < DX_TO) {
            logger.debug(() -> "setOpacity(" + (DY*(DX_TO-movedRate)/DX + OPACITY_TO) + ")");
            imageViewModel.setOpacity(DY*(DX_TO-movedRate)/DX + OPACITY_TO);
        } else {
            logger.debug(() -> "setOpacity(" + OPACITY_TO + ")");
            imageViewModel.setOpacity(OPACITY_TO);
        }
    }

    private void movePage() {
        logger.debug("movePage");
        double movedRate = Math.abs(imageViewModel.getTranslateX()) / stage.getWidth();

        if (movedRate <= DX_FROM) {
            logger.debug("movedRate <= DX_FROM");
            return;
        }

        if (imageViewModel.getTranslateX() < 0) {
            logger.debug("loadPreviousImage");
            imageViewModel.loadPreviousImage();
        } else {
            logger.debug("loadNextImage");
            imageViewModel.loadNextImage();
        }
    }
}
