package jimgv.controller;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import jimgv.Main;
import jimgv.model.Book;
import jimgv.model.Configuration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private GridPane imageParentPane;
    @FXML
    private ImageView leftImage;
    @FXML
    private ImageView rightImage;
    @FXML
    private MenuItem startWithLeftPageMenuItem;

    private Book book;

    @FXML
    public void onOpenFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("フォルダを選択");
        chooser.setInitialDirectory(Configuration.getInstance().getInitialDirectory());
        File directory = chooser.showDialog(Main.getStage());

        if (directory != null) {
            Main.setTitle(directory.getName());
            this.book = new Book(directory);
            this.refreshImage();
            Configuration.getInstance().setInitialDirectory(directory);
            Configuration.getInstance().save();
            this.startWithLeftPageMenuItem.setDisable(false);
        }
    }

    private void setRightImage(File file) {
        this.setImage(file, this.rightImage);
    }

    private void setLeftImage(File file) {
        this.setImage(file, this.leftImage);
    }

    private void setImage(File file, ImageView imageView) {
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    public void onImageScroll(ScrollEvent event) {
        if (this.book == null) {
            return;
        }

        if (event.getDeltaY() < 0) {
            this.book.previousPage();
        } else {
            this.book.nextPage();
        }
        this.refreshImage();
    }

    @FXML
    public void onLeftImageClicked() {
        this.book.nextPage();
        this.refreshImage();
    }

    @FXML
    public void onRightImageClicked() {
        this.book.previousPage();
        this.refreshImage();
    }

    private void refreshImage() {
        this.rightImage.setImage(null);
        this.leftImage.setImage(null);
        this.book.getRight().ifPresent(this::setRightImage);
        this.book.getLeft().ifPresent(this::setLeftImage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.leftImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.rightImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.startWithLeftPageMenuItem.setDisable(true);
    }

    @FXML
    public void onClickStartWithLeftPage() {
        if (this.book == null) {
            return;
        }

        boolean startWithLeft = this.book.switchStartWithLeft();

        String text = this.startWithLeftPageMenuItem.getText();
        this.startWithLeftPageMenuItem.setText(text.replaceAll(" : .*", (startWithLeft ? " : ON" : " : OFF")));

        this.refreshImage();
    }
}
