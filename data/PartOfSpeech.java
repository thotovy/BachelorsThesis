package com.zotmer.heit.data;

import java.util.*;

public enum PartOfSpeech {
	NOUN(), VERB(), OTHERS();

    /**
     * Finds out the part of speech of input word based on several methods.
     * @param word to test
     * @return PartOfSpeech enum
     */
	public static PartOfSpeech getPartOfSpeech(Word word){
	    if(word.hasDefinition()) {
            String definition = word.getDefinition();
            if (definition.startsWith("To ")) return VERB;
            else if (definition.startsWith("A ") || definition.startsWith("An ") || definition.startsWith("The "))
                return NOUN;
        }
        for(String eng : word.getAllEng()) {
            if (eng.endsWith("ing")) return NOUN;
        }
        return OTHERS;
    }

    /**
     * Assigns words into groups of specified size. Words within the group will be of the same part of speech.
     * @param words which should be assigned to groups
     * @param groupBy the amount of words each group should have
     * @param maxLimit maximum number of groups, if the unit contains enough words the amount of groups will be equal to this number
     * @return List of groups containing words
     */
    public static List<List<Word>> groupWordsByPartOfSpeech(List<Word> words, int groupBy, int maxLimit){
        List<List<Word>> groupedWords = new ArrayList<>();
        for (ArrayList<Word> values : getWordsByPartOfSpeech(words).values()) {
            Collections.shuffle(values);
            for(int i=groupBy; i<values.size();i+=groupBy){
                groupedWords.add(new ArrayList<>(values.subList(i-groupBy,i)));
            }
        }
        Collections.shuffle(groupedWords);
        return groupedWords.size() > maxLimit ? groupedWords.subList(0, maxLimit) : groupedWords;
    }

    private static Map<PartOfSpeech,ArrayList<Word>> getWordsByPartOfSpeech(List<Word> words){
        EnumMap<PartOfSpeech,ArrayList<Word>> partsOfSpeech = new EnumMap<>(PartOfSpeech.class);
        ArrayList<Word> verbs = new ArrayList<>();
        ArrayList<Word> nouns = new ArrayList<>();
        ArrayList<Word> others = new ArrayList<>();
        for(Word word : words) {
            if(word.getPartOfSpeech().equals(PartOfSpeech.VERB)) verbs.add(word);
            else if(word.getPartOfSpeech().equals(PartOfSpeech.NOUN)) nouns.add(word);
            else if(word.hasDefinition()) others.add(word);
        }
        partsOfSpeech.put(PartOfSpeech.VERB, verbs);
        partsOfSpeech.put(PartOfSpeech.NOUN, nouns);
        partsOfSpeech.put(PartOfSpeech.OTHERS, others);
        return partsOfSpeech;
    }
}
