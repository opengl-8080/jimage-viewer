package jimgv.controller;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jimgv.Main;
import jimgv.model.Book;
import jimgv.model.BookConfigMap;
import jimgv.model.BookRepository;
import jimgv.model.Configuration;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane imageParentPane;
    @FXML
    private ImageView leftImage;
    @FXML
    private ImageView rightImage;
    @FXML
    private MenuItem startWithLeftPageMenuItem;

    private Book book;
    private BookRepository repository;
    private Configuration config = Configuration.getInstance();

    @FXML
    public void onOpenFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("フォルダを選択");
        chooser.setInitialDirectory(this.config.getInitialDirectory());
        File directory = chooser.showDialog(Main.getStage());

        if (directory == null) {
            return;
        }

        Optional<Book> book = this.repository.find(directory);

        if (book.isPresent()) {
            this.book = book.get();
        } else {
            this.book = new Book(directory);
            this.repository.save(this.book);
        }

        Main.setTitle(directory.getName());

        this.config.setInitialDirectory(directory);
        this.config.save();

        this.setStartWithLeftPageMenuItemLabel();
        this.startWithLeftPageMenuItem.setDisable(false);

        this.refreshImage();
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

    @FXML
    public void onClickStartWithLeftPage() {
        if (this.book == null) {
            return;
        }

        this.book.switchStartWithLeft();
        this.repository.save(this.book);
        this.setStartWithLeftPageMenuItemLabel();

        this.refreshImage();
    }

    @FXML
    public void onClickFullScreen() {
        Stage stage = Main.getStage();
        boolean fullScreen = !stage.isFullScreen();
        Main.getStage().setFullScreen(fullScreen);
    }

    @FXML
    public void onMouseMoved(MouseEvent event) {
        if (Main.getStage().isFullScreen()) {
            if (event.getY() == 0.0) {
                this.showMenuBar();
            }
        }
    }

    @FXML
    public void onMouseExitedFromMenuBar() {
        if (Main.getStage().isFullScreen()) {
            this.hideMenuBar();
        }
    }

    private void hideMenuBar() {
        this.borderPane.setTop(null);
    }

    private void showMenuBar() {
        this.borderPane.setTop(this.menuBar);
    }

    private void setStartWithLeftPageMenuItemLabel() {
        if (this.book.isStartWithLeft()) {
            this.startWithLeftPageMenuItem.setText("左から開始 : ON");
        } else {
            this.startWithLeftPageMenuItem.setText("左から開始 : OFF");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.leftImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.rightImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.startWithLeftPageMenuItem.setDisable(true);
        this.repository = new BookRepository(BookConfigMap.getDefault());

        ReadOnlyBooleanProperty fullScreenProperty = Main.getStage().fullScreenProperty();
        fullScreenProperty.addListener((observable, oldValue, isFullScreen) -> {
            if (isFullScreen) {
                hideMenuBar();
            } else {
                showMenuBar();
            }
        });
    }
}
