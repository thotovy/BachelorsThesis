package com.zotmer.heit.gui;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.exercises.*;
import com.zotmer.heit.gui.controls.TopBar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application{
	private static Stage window;
	private static Scene mainScene;
	private static boolean admin;
	private static final VBox layoutButtons = new VBox(20);
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage newWindow){
		Main.initialize(newWindow);
	}

    private static void initialize(Stage stage){
        Main.window = stage;
        stage.setWidth(960);
        stage.setHeight(640);
        stage.setMinWidth(320);
        stage.setMinHeight(210);
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        layoutButtons.setAlignment(Pos.TOP_CENTER);
        layoutButtons.setPadding(new Insets(20));
        BorderPane layoutMain = new BorderPane();
        layoutMain.setCenter(layoutButtons);
        if(isAdmin()) layoutMain.setTop(new TopBar.TopBarBuilder().build(Help.ADMINISTRATOR_MAIN_SECTION));
        else layoutMain.setTop(new TopBar.TopBarBuilder().build(Help.USER_MAIN_SECTION));
        GuiUtils.addIcon(stage);
        addButtons();
        addShortcuts();
        mainScene = GuiUtils.createAndInitializeScene(layoutMain);
        stage.setTitle(Constants.APPLICATION_NAME);
        stage.show();
    }

	private static void addButtons() {
		ArrayList<Button> buttons = new ArrayList<>();
		Button btnVocabulary = new Button(Constants.EXERCISE_NAME_VOCABULARY);
		btnVocabulary.setOnAction(e -> {
			Selection unitSel = new Selection.SelectionBuilder(Constants.EXERCISE_NAME_VOCABULARY).addUnits().buildSelection();
			if(unitSel.isConfirmed())new Vocabulary(unitSel.getUnit());
		});
		buttons.add(btnVocabulary);
		Button btnMatchDef = new Button(Constants.EXERCISE_NAME_DEFINITION);
		btnMatchDef.setOnAction(e -> {
			Selection unitSel = new Selection.SelectionBuilder(Constants.EXERCISE_NAME_DEFINITION).addUnits().addDifficulty().addLanguage("Match with: ").buildSelection();
			if(unitSel.isConfirmed())new MatchDefinition(unitSel);
		});
		buttons.add(btnMatchDef);
		Button btnScrambled = new Button(Constants.EXERCISE_NAME_TRANSLATION);
		btnScrambled.setOnAction(e -> {
			Selection unitSel = new Selection.SelectionBuilder(Constants.EXERCISE_NAME_TRANSLATION).addUnits().addDifficulty().buildSelection();
			if(unitSel.isConfirmed())new Translation(unitSel);
		});
		buttons.add(btnScrambled);
		Button btnConnect = new Button(Constants.EXERCISE_NAME_WORD_MATCH);
		btnConnect.setOnAction(e -> {
			Selection unitSel = new Selection.SelectionBuilder(Constants.EXERCISE_NAME_WORD_MATCH).addUnits().addDifficulty().buildSelection();
			if(unitSel.isConfirmed())new WordMatching(unitSel);
		});
		buttons.add(btnConnect);
		Button btnGapFilling = new Button(Constants.EXERCISE_NAME_SENTENCES);
		btnGapFilling.setOnAction(e->{
			Selection unitSel = new Selection.SelectionBuilder(Constants.EXERCISE_NAME_SENTENCES).addUnits().addDifficulty().buildSelection();
			if(unitSel.isConfirmed()) new GapFilling(unitSel);
		});
		buttons.add(btnGapFilling);
		for(Button btn : buttons){
		    btn.setMinSize(100, 25);
		    btn.getStyleClass().add("button-exercise");
        }
		layoutButtons.getChildren().addAll(buttons);
	}

	private static void addShortcuts() {
		window.addEventHandler(KeyEvent.KEY_PRESSED, e->{
			if(e.getCode()==KeyCode.D && e.isControlDown()) {
				if(admin) {
					AlertMessage.information(String.format(AlertMessage.INFORMATION_ADMIN_MODE,"de"));
					admin=false;
				}
				else {
					AlertMessage.information(String.format(AlertMessage.INFORMATION_ADMIN_MODE,""));
					admin=true;
				}
			}
		});
	}

	public static void returnToMainPage(){
		window.setTitle(Constants.APPLICATION_NAME);
		window.setScene(mainScene);
	}

	public static boolean isAdmin() {
		return admin;
	}

	public static Stage getStage(){
		return window;
	}
}
