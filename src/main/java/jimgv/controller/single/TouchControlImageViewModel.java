package jimgv.controller.single;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TouchControlImageViewModel extends ImageViewModelBase {
    
    private final Stage stage;
    
    protected TouchControlImageViewModel(Stage stage, ImageView imageView) {
        super(imageView);
        this.stage = stage;
    }

    @Override
    public void translate(double dx, double dy) {
        if (!isZoomed()) {
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
            super.translate(dx, 0);
        } else {
            super.translate(dx, dy);
        }
    }
}
