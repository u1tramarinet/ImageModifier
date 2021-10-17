package com.u1tramarinet.imagemodifier;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuBar topMenuBar;

    @FXML
    private ImageView originalImageView;

    @FXML
    private ImageView modifiedImageView;

    @FXML
    private Label status;

    @FXML
    private VBox colorBoxOriginal;

    @FXML
    private TextField colorOriginal;

    @FXML
    private TextField alphaOriginal;

    @FXML
    private TextField redOriginal;

    @FXML
    private TextField greenOriginal;

    @FXML
    private TextField blueOriginal;

    @FXML
    private VBox colorBoxModified;

    @FXML
    private TextField colorModified;

    @FXML
    private TextField alphaModified;

    @FXML
    private TextField redModified;

    @FXML
    private TextField greenModified;

    @FXML
    private TextField blueModified;

    @FXML
    private Slider thresholdSlider;

    @FXML
    private Label thresholdValue;

    @FXML
    private TextField x;

    @FXML
    private TextField y;

    @FXML
    private TextField alpha;

    @FXML
    private TextField red;

    @FXML
    private TextField green;

    @FXML
    private TextField blue;

    @FXML
    private Button register;

    @FXML
    private Button find;

    @FXML
    private Label findResult;

    private WritableImage originalWritableImage;

    private WritableImage modifiedWritableImage;

    private Analyzer analyzer = new Analyzer();

    private ImageFactory imageFactory = new ImageFactory();

    private Filer filer = new Filer();

    private double latestMouseX;

    private double latestMouseY;

    private PixelInfo latestPixel;

    private final List<PixelInfo> findPixels = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpMenuBar();
        setUpImage();
        setUpThreshold();
        setUpGrouping();
    }

    public void initialize(Stage stage) {
        stage.sizeToScene();
    }

    private void setUpMenuBar() {
        Menu file = new Menu("File");
        MenuItem open = new MenuItem("Open");
        open.setOnAction(actionEvent -> chooseFile());
        MenuItem save = new MenuItem("Save");
        file.getItems().addAll(open, save);

        Menu edit = new Menu("Edit");
        MenuItem binarize = new MenuItem("Binarize");
        binarize.setOnAction(actionEvent -> executeThresholding());
        MenuItem monochrome = new MenuItem("Monochrome");
        monochrome.setOnAction(actionEvent -> executeMonochrome());
        MenuItem inverse = new MenuItem("Inverse");
        inverse.setOnAction(actionEvent -> executeInverse());
        edit.getItems().addAll(binarize, monochrome, inverse);

        topMenuBar.getMenus().addAll(file, edit);
    }

    private void setUpThreshold() {
        thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                thresholdValue.setText(String.valueOf(t1));
                executeGrouping();
            }
        });
    }

    private void setUpGrouping() {
        register.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                x.setText(String.valueOf(latestMouseX));
                y.setText(String.valueOf(latestMouseY));
                alpha.setText(alphaOriginal.getText());
                red.setText(redOriginal.getText());
                green.setText(greenOriginal.getText());
                blue.setText(blueOriginal.getText());
            }
        });
        find.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                executeGrouping();
            }
        });
    }

    private void setUpImage() {
        originalImageView.addEventHandler(MouseEvent.ANY, new ImageEventHandler() {
            @Override
            void onMoved(MouseEvent mouseEvent, double x, double y) {
                Argb argb = new Argb(originalWritableImage.getPixelReader().getArgb((int) x, (int) y));
                updateStatus("Mouse moved x=" + x + ", y=" + y + ", color=" + argb);
            }

            @Override
            void onClick(MouseEvent mouseEvent, double x, double y) {
                Argb argb = new Argb(originalWritableImage.getPixelReader().getArgb((int) x, (int) y));
                updateColorInfoOriginal(argb);
                latestMouseX = x;
                latestMouseY = y;
                latestPixel = new PixelInfo((int)x, (int)y, argb);
            }
        });
        modifiedImageView.addEventHandler(MouseEvent.ANY, new ImageEventHandler() {
            @Override
            void onClick(MouseEvent mouseEvent, double x, double y) {
                Argb argb = new Argb(modifiedWritableImage.getPixelReader().getArgb((int) x, (int) y));
                updateColorInfoModified(argb);
            }
        });
    }

    private void chooseFile() {
        File file = filer.chooseImageFile();
        if (file == null) return;
        Image image = new Image("file:" + file.getAbsolutePath());
        originalWritableImage = imageFactory.createWritableImage(image);
        modifiedWritableImage = imageFactory.copyWritableImage(originalWritableImage);
        setImages();
    }

    private void executeThresholding() {
        restoreModifiedImage();
        analyzer.binarize(originalWritableImage, modifiedWritableImage);
    }

    private void executeMonochrome() {
        restoreModifiedImage();
        analyzer.monochrome(originalWritableImage, modifiedWritableImage);
    }

    private void executeInverse() {
        restoreModifiedImage();
        analyzer.inverse(originalWritableImage, modifiedWritableImage);
    }

    private void executeGrouping() {
        findPixels.clear();
        findPixels.addAll(analyzer.findSimilarPixels(originalWritableImage, latestPixel, thresholdSlider.getValue(), new ArrayList<>()));
        restoreModifiedImage();
        analyzer.highlightFindPixels(modifiedWritableImage, findPixels);
        findResult.setText(findPixels.size() + " pixels found");
    }

    private void setImages() {
        originalImageView.setImage(originalWritableImage);
        modifiedImageView.setImage(modifiedWritableImage);
    }

    private void restoreModifiedImage() {
        modifiedWritableImage = imageFactory.copyWritableImage(originalWritableImage);
        modifiedImageView.setImage(modifiedWritableImage);
    }

    private void updateColorInfoOriginal(Argb color) {
        colorOriginal.setText(Color.color(Math.abs(color.a / 255f), Math.abs(color.r / 255f), Math.abs(color.g / 255f), Math.abs(color.b / 255f)).toString());
        alphaOriginal.setText(String.valueOf(color.a));
        redOriginal.setText(String.valueOf(color.r));
        greenOriginal.setText(String.valueOf(color.g));
        blueOriginal.setText(String.valueOf(color.b));
        colorBoxOriginal.setBackground(new Background(new BackgroundFill(Color.rgb(color.r, color.g, color.b), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void updateColorInfoModified(Argb color) {
        colorModified.setText(Color.color(Math.abs(color.a / 255f), Math.abs(color.r / 255f), Math.abs(color.g / 255f), Math.abs(color.b / 255f)).toString());
        alphaModified.setText(String.valueOf(color.a));
        redModified.setText(String.valueOf(color.r));
        greenModified.setText(String.valueOf(color.g));
        blueModified.setText(String.valueOf(color.b));
        colorBoxModified.setBackground(new Background(new BackgroundFill(Color.rgb(color.r, color.g, color.b), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void updateStatus(String statusMessage) {
        status.setText(statusMessage);
    }

    private static class ImageEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            EventType<MouseEvent> type = (EventType<MouseEvent>) mouseEvent.getEventType();
            if (type == MouseEvent.MOUSE_MOVED) {
                onMoved(mouseEvent, x, y);
            } else if (type == MouseEvent.MOUSE_CLICKED) {
                onClick(mouseEvent, x, y);
            }
        }

        void onMoved(MouseEvent mouseEvent, double x, double y) {

        }

        void onClick(MouseEvent mouseEvent, double x, double y) {

        }
    }
}