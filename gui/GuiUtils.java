package com.zotmer.heit.gui;

import com.zotmer.heit.data.Constants;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiUtils {
    private GuiUtils(){}

    public static Scene createAndInitializeScene(Pane pane){
        Scene scene = new Scene(pane, Main.getStage().getWidth(), Main.getStage().getHeight());
        scene.getStylesheets().add(Constants.LOCATION_STYLESHEET);
        Main.getStage().setScene(scene);
        return scene;
    }

    /**
     * Changes the icon of the window to the default english flag.
     * @param stage window to be set the icon to
     */
    public static void addIcon(Stage stage){
        stage.getIcons().add(new Image(Main.class.getResourceAsStream(Constants.LOCATION_ICON)));
    }
}
