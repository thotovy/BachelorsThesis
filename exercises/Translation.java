package com.zotmer.heit.exercises;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.data.Word;
import com.zotmer.heit.gui.*;
import com.zotmer.heit.gui.controls.TopBar;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;

public class Translation {
    private ArrayList<Word> usedWords = new ArrayList<>();
    private ArrayList<TextField> fields = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();
    private GridPane layoutText = new GridPane();
    private Selection selection;

	public Translation(Selection select) {
		selection=select;
		BorderPane layoutMain = new BorderPane();
        for(int i=0;i<10;i++)addQuestion(i);
        VBox layoutQuestions = new VBox(4);
        ScrollPane scroll = new ScrollPane(layoutQuestions);
        layoutText.setVgap(5);
        layoutText.setHgap(10);
        layoutText.setPadding(new Insets(10));
        layoutQuestions.getChildren().add(layoutText);
		layoutMain.setTop(new TopBar.TopBarBuilder().mainMenu().submit((e->{
			if(getResult()) {
				Button btn = (Button) e.getSource();
				btn.setText(Constants.REPEAT_BUTTON_TEXT);
				btn.setOnAction(event->new Translation(selection));
			}
		})).build(Help.USER_TRANSLATION));
		layoutMain.setCenter(scroll);
        VBox help = new VBox(1);
        if(selection.isHard()) layoutMain.setRight(help);
        GuiUtils.createAndInitializeScene(layoutMain);
		Main.getStage().setTitle(Constants.EXERCISE_NAME_TRANSLATION + " - " + selection.getUnit().toString());
	}
	
	private void addQuestion(int position){
		Word word;
        do{word = selection.getUnit().getRandomWord();}while(usedWords.contains(word));
        usedWords.add(word);
        Label toTranslate = new Label();
        TextField translation = new TextField();
        String answer = word.getEng();
        answers.add(answer);
        if(selection.isHard()) toTranslate.setText(word.getCz());
        else{
            toTranslate.setText(scramble(answer));
            translation.setPromptText(word.getCz());
        }
        translation.setMinWidth(300);
        fields.add(translation);
        GridPane.setConstraints(toTranslate, 0 ,position);
        GridPane.setConstraints(translation, 1 ,position);
        layoutText.getChildren().addAll(toTranslate,translation);
	}
	
	private boolean getResult(){
		int correct=0;
		for (TextField answer : fields) {
			if(answer.getText().equals("")) {
				if(AlertMessage.confirmation(AlertMessage.CONFIRMATION_SUBMIT))break;
				else return false;
			}
		}
		for(int i=0;i<10;i++){
			TextField field = fields.get(i);
			if(field.getText().equalsIgnoreCase(answers.get(i)) || selection.isHard() && usedWords.get(i).containsEng(field.getText())) {
				correct++;
				field.setId("correct-answer");
			}
			else {
				field.setText(answers.get(i));
				field.setId("incorrect-answer");
			}
			field.setEditable(false);
		}
		AlertMessage.information(String.format(AlertMessage.INFORMATION_RESULT, correct, "10"),"Result");
		return true;
	}

	private String scramble(String toScramble) {
		StringBuilder scrambled = new StringBuilder();
        for (String word : toScramble.split(" ")) {
            ArrayList<Character> letters = new ArrayList<>();
            word = word.toLowerCase();
            for (char c : word.toCharArray()) letters.add(c);
            Collections.shuffle(letters);
            for (char c : letters) scrambled.append(c);
            scrambled.append(" ");
        }

		return scrambled.toString();
	}
}
