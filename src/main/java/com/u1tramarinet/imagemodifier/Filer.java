package com.u1tramarinet.imagemodifier;

import javafx.stage.FileChooser;

import java.io.File;

public class Filer {

    File chooseImageFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("画像選択");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("画像ファイル", "*.jpg", "*.png")
        );
        return chooser.showOpenDialog(null);
    }
}
