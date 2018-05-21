package com.zotmer.heit.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enum containing Unit numbers and names.
 */
public enum Unit{
	UNIT1(1,"Unit 1: Introduction to IT"),
	UNIT2(2,"Unit 2: Career in IT"),
	UNIT3(3,"Unit 3: PC & Types of Computers"),
	UNIT4(4,"Unit 4: Motherboard"),
	UNIT5(5,"Unit 5: Input Devices"),
	UNIT6(6,"Unit 6: Output Devices"),
	UNIT7(7,"Unit 7: Storage Devices"),
	UNIT8(8,"Unit 8: Software"),
	UNIT9(9,"Unit 9: Windows Basics"),
	UNIT10(10,"Unit 10: Networks and Internet"),
	UNIT11(11,"Unit 11: World Wide Web"),
	UNIT12(12,"Unit 12: Internet Safety"),
	EXTRA_READING(13,"Extra reading"),
	REVISION("Revision");

	private ArrayList<Word> words = new ArrayList<>();
	private String name;
	private int number;

    static{
        for(Unit unit : Unit.values()){
            if(unit.number>0 && unit.number<=12){
                REVISION.words.addAll(unit.getAllWords());
            }
        }
    }

	Unit(int number, String name){
		this.name=name;
		this.number=number;
		try {
			SQLiteDb db = new SQLiteDb();
			ResultSet rs = db.executeResultQuery("SELECT * FROM words WHERE unit="+getNumber());
			while(rs.next()) {
				words.add(new Word(
				        rs.getString("english"),
                        rs.getString("czech"),
                        rs.getString("definition"),
                        this,
                        rs.getString("sentences"),
                        rs.getInt("ID")));
			}
			db.closeConnection();
		} catch (SQLException e) {e.printStackTrace();}
	}

	Unit(String name){
        this.name=name;
        this.number = 0;
    }

    /**
     * Adds word to the unit.
     * @param word word to add
     */
	public void addWord(Word word) {
		words.add(word);
	}

    /**
     * Removes the word from this unit
     * @param word word to remove
     */
	public void removeWord(Word word) {
		words.remove(word);
	}

	@Override
	public String toString(){
		return name;
	}

    /**
     * Gets number of this unit. May return null if called on revision.
     * @return unit number
     */
	public int getNumber() {
		return number;
	}

    /**
     * Gets random word from this unit
     * @return random word
     */
	public Word getRandomWord(){
		return words.get(new Random().nextInt(words.size()));
	}

    /**
     * Gets list of all words from this unit
     * @return list of words
     */
	@SuppressWarnings("unchecked")
	public List<Word> getAllWords(){
		return (List<Word>) words.clone();
	}

    /**
     * Gets list of all words from this unit that contain definition
     * @return list of words with definition
     */
    public List<Word> getAllWordsWithDefinition(){
        ArrayList<Word> wordsWithDefinition = new ArrayList<>(words);
        wordsWithDefinition.removeIf(word -> !word.hasDefinition());
        return wordsWithDefinition;
    }

    /**
     * Gets list of all words from this unit that contain example sentence
     * @return list of words with example sentence
     */
    public List<Word> getAllWordsWithSentence(){
        ArrayList<Word> wordsWithSentence = new ArrayList<>(words);
        wordsWithSentence.removeIf(word -> !word.hasSentence());
        return wordsWithSentence;
    }

    /**
     * Gets unit based on its name
     * @param name name of the unit
     * @return Unit enum or null if unit with such name does not exist.
     */
	public static Unit getUnit(String name) {
		for(Unit unit : Unit.values()) {
			if(unit.toString().equals(name)) return unit;
		}
		return null;
	}

    /**
     * Gets unit based on its number
     * @param number name of the unit
     * @return Unit enum or null if unit with such number does not exist.
     */
	public static Unit getUnit(int number){
        for(Unit unit : Unit.values()) {
            if(unit.number==number) return unit;
        }
        return null;
    }
}
