package jimgv.controller.single.touch;

import jimgv.controller.single.SingleImageControllerBase;

public class TouchPanelSingleImageController extends SingleImageControllerBase {
    private TouchGesture touchGesture = new TouchGesture();

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
            if (!imageViewModel.isZoomed()) {
                double rate = Math.abs(imageView.getTranslateX()) / stage.getWidth();

                double Ys = 0.5;
                double Ye = 0.2;
                double Xs = 0.15;
                double Xe = 0.4;
                double DX = Xe-Xs;
                double DY = Ys-Ye;
                if (rate < Xs) {
                    imageView.setOpacity(1.0);
                } else if (Xs <= rate && rate < Xe) {
                    imageView.setOpacity(DY*(Xe-rate)/DX + Ye);
                } else {
                    imageView.setOpacity(Ye);
                }
                imageViewModel.translate(dx, 0);
            } else {
                imageViewModel.translate(dx, dy);
            }
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
        
        touchGesture.onZoomed(zoomFactor -> {
            imageViewModel.zoom(zoomFactor);
        });

        touchGesture.onZoomFinished(() -> {
            imageViewModel.finishZoom();
        });
    }
}
