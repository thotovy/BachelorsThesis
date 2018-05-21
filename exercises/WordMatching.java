package com.zotmer.heit.exercises;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.data.Word;
import com.zotmer.heit.gui.*;
import com.zotmer.heit.gui.controls.TopBar;
import com.zotmer.heit.gui.exercise.DragAndDropGUI;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class WordMatching {
	private Selection selection;
	private DragAndDropGUI dragAndDropGUI = new DragAndDropGUI(200);

	public WordMatching(Selection select) {
		selection = select;
		BorderPane layoutMain = new BorderPane();
        addQuestions();

		layoutMain.setTop(new TopBar.TopBarBuilder().mainMenu().submit((e->{
			if(dragAndDropGUI.getResult()) {
				Button btn = (Button) e.getSource();
				btn.setText(Constants.REPEAT_BUTTON_TEXT);
				btn.setOnAction(event->new WordMatching(selection));
			}
		})).build(Help.USER_WORD_MATCHING));

		layoutMain.setCenter(dragAndDropGUI.getDisplay());
		GuiUtils.createAndInitializeScene(layoutMain);
		Main.getStage().setTitle(Constants.EXERCISE_NAME_WORD_MATCH + " - " + selection.getUnit().toString());
	}

	private void addQuestions() {
        ArrayList<Word> wordsUsed = new ArrayList<>();
        Word word;
        int noOfOptions=10;
		if(selection.isHard()) noOfOptions=15;

		for(int i=0; i<noOfOptions; i++){
			do{word = selection.getUnit().getRandomWord();}while(wordsUsed.contains(word));
			wordsUsed.add(word);
            dragAndDropGUI.addOption(word.getEng(), word.getCz());
		}
	}

}
