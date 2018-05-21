package com.zotmer.heit.gui.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class for MultipleChoiceGUI
 */
public class MultipleChoiceData {
    private String stem;
    private String correctOption;
    private List<String> otherOptions = new ArrayList<>();

    /**
     * Gets stem of multiple choice
     * @return stem
     */
    public String getStem() {
        return stem;
    }

    /**
     * Sets stem of the multiple choice.
     * @param stem stem
     */
    public void setStem(String stem) {
        this.stem = stem;
    }

    /**
     * Gets correct option of data
     * @return correct option
     */
    public String getCorrectOption() {
        return correctOption;
    }

    /**
     * Sets correct option of data.
     * @param correctOption correct option
     */
    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    /**
     * Sets all incorrect options.
     * @param otherOptions list of incorrect options
     */
    public void setOtherOptions(List<String> otherOptions) {
        this.otherOptions = otherOptions;
    }

    /**
     * Adds INCORRECT option to data.
     * Use setCorrectOption to add correct option instead.
     * @param option incorrect option
     */
    public void addOption(String option){
        otherOptions.add(option);
    }

    /**
     * Gets a list of all the options including the correct one in random order.
     * @return list of options in random order
     */
    public List<String> getAllOptionsRandom(){
        ArrayList<String> list = new ArrayList<>(otherOptions);
        list.add(correctOption);
        Collections.shuffle(list);
        return list;
    }
}
