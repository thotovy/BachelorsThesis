package com.zotmer.heit.gui.exercise;

import com.zotmer.heit.gui.AlertMessage;
import com.zotmer.heit.gui.Main;
import com.zotmer.heit.gui.data.MultipleChoiceData;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceGUI implements ExerciseGUI {
    private VBox layoutQuestions = new VBox(4);
    private ScrollPane mainPane;
    private List<RadioButton> correctOptions = new ArrayList<>();
    private List<ToggleGroup> buttonGroups = new ArrayList<>();

    public MultipleChoiceGUI() {
        layoutQuestions.setPadding(new Insets(5,5,0,10));
        layoutQuestions.prefWidthProperty().bind(Main.getStage().widthProperty()
                .subtract(layoutQuestions.getPadding().getLeft())
                .subtract(layoutQuestions.getPadding().getRight())
                .subtract(17)); //subtract by vertical scrollbar of 17 pixels
        layoutQuestions.setMinWidth(600);
        mainPane = new ScrollPane(layoutQuestions);
    }

    /**
     * Creates question that consists of stem and several options in random order.
     * @param multipleChoiceData data that the question should be constructed from
     */
    public void createQuestion(MultipleChoiceData multipleChoiceData){
        HBox layoutOptions = new HBox(10);
        ToggleGroup buttons = new ToggleGroup();
        for(String text : multipleChoiceData.getAllOptionsRandom()){
            RadioButton option = new RadioButton(text);
            option.setToggleGroup(buttons);
            option.setWrapText(true);
            layoutOptions.getChildren().add(option);
            if(option.getText().equals(multipleChoiceData.getCorrectOption())) correctOptions.add(option);
        }

        layoutOptions.setPadding(new Insets(0,0,13,0));
        Label stem = new Label(multipleChoiceData.getStem());
        layoutQuestions.getChildren().add(stem);
        layoutQuestions.getChildren().add(layoutOptions);
        buttonGroups.add(buttons);
    }

    /**
     * Gets the layout which contains the questions of the exercise.
     * @return layout to be displayed
     */
    public ScrollPane getDisplay() {
        return mainPane;
    }

    /**
     * Displays the result of the exercise to the user. If the exercise is not finished the user will be asked for confirmation.
     * @return true if the exercise is finished and the result is successfully displayed to user.
     */
    public boolean getResult(){
        if(isFilled()) {
            AlertMessage.information(String.format(AlertMessage.INFORMATION_RESULT, getCorrectAnswers(), buttonGroups.size()),"Result");
            return true;
        }
        return false;
    }

    /**
     * If there are some missing answers in the exercise, asks user if he really wants to finish. Otherwise return true.
     * @return true if all answers are filled or if user confirmed to get results despite missing answers
     */
    private boolean isFilled(){
        for (ToggleGroup btnGroup : buttonGroups) {
            if(btnGroup.getSelectedToggle()==null) {
                return AlertMessage.confirmation(AlertMessage.CONFIRMATION_SUBMIT);
            }
        }
        return true;
    }

    /**
     * Gets the number of correctly guessed answers.
     * @return number of correct answers
     */
    private int getCorrectAnswers(){
        int correct=0;
        for (ToggleGroup buttonGroup : buttonGroups) {
            for (Toggle toggle : buttonGroup.getToggles()) {
                RadioButton btn = ((RadioButton) toggle);
                btn.setMouseTransparent(true);
                btn.setFocusTraversable(false);
                if (correctOptions.contains(btn)) {
                    btn.setId("correct-answer");
                    if (btn.isSelected()){
                        correct++;
                    }
                } else if (btn.isSelected()){
                    btn.setId("incorrect-answer");
                }
            }
        }
        return correct;
    }
}
