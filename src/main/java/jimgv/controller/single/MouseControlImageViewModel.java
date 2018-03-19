package jimgv.controller.single;

import javafx.scene.image.ImageView;

public class MouseControlImageViewModel extends ImageViewModelBase {

    public MouseControlImageViewModel(ImageView imageView) {
        super(imageView);
    }

    @Override
    public void translate(double dx, double dy) {
        if (isZoomed()) {
            super.translate(dx, dy);
        }
    }
}
