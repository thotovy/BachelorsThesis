package com.zotmer.heit.gui;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


public enum Help {
    USER_MAIN_SECTION ("/com/zotmer/heit/resources/guide_user_mainSection.png"),
    USER_VOCABULARY ("/com/zotmer/heit/resources/guide_user_vocabulary.png"),
    USER_DEFINITION_MATCHING("/com/zotmer/heit/resources/guide_user_definitionMatching.png"),
    USER_TRANSLATION ("/com/zotmer/heit/resources/guide_user_translation.png"),
    USER_WORD_MATCHING ("/com/zotmer/heit/resources/guide_user_wordMatching.png"),
    USER_GAP_FILLING ("/com/zotmer/heit/resources/guide_user_gapFilling.png"),
    ADMINISTRATOR_MAIN_SECTION ("/com/zotmer/heit/resources/guide_administrator_mainSection.png"),
    ADMINISTRATOR_VOCABULARY ("/com/zotmer/heit/resources/guide_administrator_vocabulary.png");

    String imagePath;

    Help(String imagePath){
        this.imagePath = imagePath;
    }

    private String getImagePath() {
        return imagePath;
    }

    public static void displayHelp(Help help){
        if(Main.isAdmin()){
            if(help==USER_MAIN_SECTION) help = ADMINISTRATOR_MAIN_SECTION;
            else if(help==USER_VOCABULARY) help = ADMINISTRATOR_VOCABULARY;
        }

        ImageView imageView = new ImageView(new Image(Main.class.getResourceAsStream(help.getImagePath())));
        ScrollPane rootPane = new ScrollPane(imageView);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(rootPane));
        dialog.setHeight(500);
        dialog.setWidth(641);
        dialog.setTitle("Help");
        GuiUtils.addIcon(dialog);
        dialog.setResizable(false);
        dialog.addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode()== KeyCode.ESCAPE) dialog.close();
        });
        dialog.showAndWait();
    }
}
