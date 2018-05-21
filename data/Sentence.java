package com.zotmer.heit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Sentence {
    private String text;
    private String keyWordContained;
    private Word word;

    private Sentence(String sentence, String englishWordContained, Word word) {
        this.text = sentence;
        this.keyWordContained = englishWordContained;
        this.word = word;
    }

    /**
     * Constructs list of sentences from given formatted sentence(s). Returns empty list if there are no sentences containing english words.
     * @param formattedSentences sentences to be constructed from
     * @param word word that the sentences belong to
     * @return list of sentence enums
     */
    public static List<Sentence> constructSentences(String formattedSentences, Word word){
        List<Sentence> listOfSentences = new ArrayList<>();
        String[] sentences = formattedSentences.split(Constants.DELIMITER);
        for (String sentence : sentences) {
            sentence = sentence.trim();
            for (String eng : word.getAllEng()) {
                if (Pattern.compile(Pattern.quote(eng), Pattern.CASE_INSENSITIVE).matcher(sentence).find()) {
                    listOfSentences.add(new Sentence(sentence, eng, word));
                    break;
                }
            }
        }
        return listOfSentences;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKeyWordContained() {
        return keyWordContained;
    }

    public void setKeyWordContained(String keyWordContained) {
        this.keyWordContained = keyWordContained;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    /**
     * Gets example text with "........" instead of word.
     * @return example text with ellipsis instead of word
     */
    public String getTextWithoutKeyWord(){
        return text.replaceAll("(?i)"+keyWordContained,"............");

    }
}
