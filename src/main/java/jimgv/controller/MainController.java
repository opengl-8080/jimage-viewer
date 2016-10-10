package jimgv.controller;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        if (this.book != null) {
            this.repository.save(this.book);
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

    private void setStartWithLeftPageMenuItemLabel() {
        if (this.book.isStartWithLeft()) {
            this.startWithLeftPageMenuItem.setText("左から開始 : ON");
        } else {
            this.startWithLeftPageMenuItem.setText("左から開始 : OFF");
        }
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

    @FXML
    public void onHome() {
        if (this.book == null) {
            return;
        }
        this.book.home();
        this.refreshImage();
    }

    @FXML
    public void onEnd() {
        if (this.book == null) {
            return;
        }
        this.book.end();
        this.refreshImage();
    }

    @FXML
    public void onMouseClickOnGridPane(MouseEvent event) {
        if (this.book == null || !this.isPrimaryMouseClick(event)) {
            return;
        }

        double x = event.getX();
        double width = this.imageParentPane.getWidth();

        if (x < (width / 2.0)) {
            this.onLeft();
        } else {
            this.onRight();
        }
    }

    @FXML
    public void onLeft() {
        if (this.book == null) {
            return;
        }

        this.book.nextPage();
        this.refreshImage();
    }

    @FXML
    public void onRight() {
        if (this.book == null) {
            return;
        }

        this.book.previousPage();
        this.refreshImage();
    }

    private boolean isPrimaryMouseClick(Event event) {
        if (!(event instanceof MouseEvent)) {
            return false;
        }

        MouseEvent mouseEvent = (MouseEvent) event;
        return mouseEvent.getButton() == MouseButton.PRIMARY;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.leftImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.rightImage.fitHeightProperty().bind(this.imageParentPane.heightProperty());
        this.startWithLeftPageMenuItem.setDisable(true);
        this.repository = new BookRepository(BookConfigMap.getDefault());

        Stage stage = Main.getStage();

        ReadOnlyBooleanProperty fullScreenProperty = stage.fullScreenProperty();
        fullScreenProperty.addListener((observable, oldValue, isFullScreen) -> {
            if (isFullScreen) {
                hideMenuBar();
            } else {
                showMenuBar();
            }
        });

        stage.setWidth(this.config.getWindowWidth());
        stage.setHeight(this.config.getWindowHeight());
        stage.setMaximized(this.config.isMaximized());

        stage.widthProperty().addListener((a, b, newWidth) -> {
            if (!stage.isMaximized()) {
                this.config.setWindowWidth(newWidth.doubleValue());
            }
        });

        stage.heightProperty().addListener((a, b, newHeight) -> {
            if (!stage.isMaximized()) {
                this.config.setWindowHeight(newHeight.doubleValue());
            }
        });

        stage.setOnCloseRequest((e) -> {
            this.config.setMaximized(stage.isMaximized());
            this.config.save();

            if (this.book != null) {
                this.repository.save(this.book);
            }
        });
    }

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> future;

    @FXML
    public void onMouseMovedOnImage() {
        this.imageParentPane.setCursor(Cursor.HAND);

        if (this.future != null) {
            this.future.cancel(true);
        }

        this.future = executorService.schedule(() -> {
            this.imageParentPane.setCursor(Cursor.NONE);
        }, 1500, TimeUnit.MILLISECONDS);
    }
}
