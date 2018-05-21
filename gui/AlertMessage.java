package com.zotmer.heit.gui;

import com.zotmer.heit.data.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AlertMessage {
    public static final String CONFIRMATION_SUBMIT = "You did not answer everything.\nDo you want to submit anyway?";
    public static final String CONFIRMATION_DELETE = "Are you sure you want to delete \"%s\"?";
    public static final String CONFIRMATION_CREATE_DB = "No external database present.\nDo you want to create a new one?";

    public static final String ERROR_NO_SELECTION = "No word selected!";
    public static final String ERROR_SENTENCE = "Sentence: \"%s\" does not contain an english translation!";
    public static final String ERROR_UNIT_MISSING = "Unit number missing or in a wrong format!";
    public static final String ERROR_NOT_ENOUGH_INFO = "Not enough information entered!";
    public static final String ERROR_INCORRECT_UNIT = "Incorrect unit number!";
    public static final String ERROR_NO_EXTERNAL_DB = "Please place the database file (EnglishForIT.db) in the same folder as your application and then try again.";

    public static final String INFORMATION_WORD_SUCCESSFULLY = "Word was successfully %s!";
    public static final String INFORMATION_ADMIN_MODE = "Admin tools were %sactivated.";
    public static final String INFORMATION_RESULT = "You got %s/%s correct answers!";

    private AlertMessage(){}

    public static boolean confirmation(String text){
        Alert alert = new Alert(AlertType.CONFIRMATION, text, ButtonType.YES,ButtonType.NO);
        initializeAlert(alert);
        return alert.showAndWait().get()==ButtonType.YES;
    }

    public static void information(String text){
        Alert alert = new Alert(AlertType.INFORMATION, text,ButtonType.OK);
        initializeAlert(alert);
        alert.setTitle("Information");
        alert.show();
    }
    public static void information(String text, String title){
        Alert alert = new Alert(AlertType.INFORMATION, text,ButtonType.OK);
        initializeAlert(alert);
        alert.setTitle(title);
        alert.show();
    }

    public static void error(String text) {
        Alert alert = new Alert(AlertType.ERROR, text,ButtonType.OK);
        initializeAlert(alert);
        alert.show();
    }

    private static void initializeAlert(Alert alert){
        alert.setHeaderText("");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().getStylesheets().add(Constants.LOCATION_STYLESHEET);
        GuiUtils.addIcon((Stage) alert.getDialogPane().getScene().getWindow());
    }
}
