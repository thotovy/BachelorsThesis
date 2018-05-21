package com.zotmer.heit.exercises;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.data.PartOfSpeech;
import com.zotmer.heit.data.Word;
import com.zotmer.heit.gui.*;
import com.zotmer.heit.gui.controls.TopBar;
import com.zotmer.heit.gui.exercise.DragAndDropGUI;
import com.zotmer.heit.gui.exercise.ExerciseGUI;
import com.zotmer.heit.gui.exercise.MultipleChoiceGUI;
import com.zotmer.heit.gui.data.MultipleChoiceData;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.util.Collections;
import java.util.List;

public class MatchDefinition{
	private Selection selection;

    public MatchDefinition(Selection select) {
        ExerciseGUI exerciseGUI;
		selection=select;
		if(selection.isHard()) exerciseGUI = createDragAndDrop();
        else exerciseGUI = createMultipleChoice();
		BorderPane layoutMain = new BorderPane();

		layoutMain.setTop(new TopBar.TopBarBuilder().mainMenu().submit((e->{
			if(exerciseGUI.getResult()) {
				Button btn = (Button) e.getSource();
				btn.setText(Constants.REPEAT_BUTTON_TEXT);
				btn.setOnAction(event->new MatchDefinition(selection));
			}
		})).build(Help.USER_DEFINITION_MATCHING));
		layoutMain.setCenter(exerciseGUI.getDisplay());
        GuiUtils.createAndInitializeScene(layoutMain);
		Main.getStage().setTitle(Constants.EXERCISE_NAME_DEFINITION + " - " + selection.getUnit().toString());
	}
	
	private ExerciseGUI createMultipleChoice(){
        MultipleChoiceGUI multipleChoiceGUI = new MultipleChoiceGUI();

        for(List<Word> options : PartOfSpeech.groupWordsByPartOfSpeech(selection.getUnit().getAllWordsWithDefinition(), 4, 10)) {
            MultipleChoiceData data = new MultipleChoiceData();
            for (int j = 0; j < options.size(); j++) {
                Word option = options.get(j);
                String optionText;
                if (selection.isEng()) optionText = option.getEng();
                else optionText = option.getCz();

                if (j == 0) {
                    data.setCorrectOption(optionText);
                    data.setStem(option.getDefinition());
                }
                else data.addOption(optionText);

            }
            multipleChoiceGUI.createQuestion(data);
        }
        return multipleChoiceGUI;
	}

    private ExerciseGUI createDragAndDrop(){
        DragAndDropGUI dragAndDropGUI = new DragAndDropGUI(400);
        List<Word> wordsUsed = selection.getUnit().getAllWordsWithDefinition();
        Collections.shuffle(wordsUsed);

        for(int i=0; i<10 && i<wordsUsed.size(); i++){
            Word word = wordsUsed.get(i);
            if(selection.isEng()) dragAndDropGUI.addOption(word.getEng(), word.getDefinition());
            else dragAndDropGUI.addOption(word.getCz(), word.getDefinition());
        }
        return dragAndDropGUI;
    }

}
