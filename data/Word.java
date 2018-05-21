package com.zotmer.heit.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Word contains czech translation, english translation and definition
 * as well as part of speech and unit it belongs to.
 */
public class Word {
	private String[] czWords;
	private String[] engWords;
	private String definition;
	private List<Sentence> sentences = new ArrayList<>();
	private PartOfSpeech partOfSpeech;
	private Unit unit;
	private int id;
	public Word(String eng, String czech,String definition, Unit unit, String exampleSentences, int id) {
		this(eng,czech,definition,unit, exampleSentences);
		this.id=id;
	}
	public Word(String eng, String czech,String definition, Unit unit, String exampleSentences) {
		this.unit=unit;
		engWords=eng.split(Constants.DELIMITER);
		for(int i=0;i<engWords.length;i++)engWords[i]=engWords[i].trim();
		czWords = czech.split(Constants.DELIMITER);
		for(int i=0;i<czWords.length;i++)czWords[i]=czWords[i].trim();
		if(definition!=null) this.definition =definition.trim();
		else this.definition = "";
		partOfSpeech = PartOfSpeech.getPartOfSpeech(this);
        if(exampleSentences!=null && !exampleSentences.equals("")) sentences = Sentence.constructSentences(exampleSentences,this);
	}

    //*******************************************CZECH*****************************************************

	/**
	 * Sets czech translation for the word and updates database.
	 * @param cz czech translation
	 * @param db database connection
	 */
	public void setCz(String cz, SQLiteDb db) {
		db.executeUpdate("UPDATE words SET czech='"+cz.replaceAll("'", "''")+"' WHERE ID="+id+";");
		czWords=cz.split(Constants.DELIMITER);
		for(int i=0;i<czWords.length;i++) {
			czWords[i]=czWords[i].trim();
		}
	}

	/**
     * Gets all czech translations as a single String separated by semicolon
	 * @return All czech translations formatted as "First; Second; Third"
	 */
	public String getAllCzFormatted() {
		StringBuilder finalWord = new StringBuilder();
		for(String word : czWords) {
			finalWord.append(word);
			finalWord.append(Constants.DELIMITER);
			finalWord.append(" ");
		}
		return finalWord.substring(0, finalWord.length()-2);
	}

    /**
     * Gets all czech translations
     * @return List of all czech translations
     */
	public List<String> getAllCz(){
		return new ArrayList<>(Arrays.asList(czWords));
	}

    /**
     * Gets random czech translation.
     * @return Random czech translation
     */
	public String getCz(){
		return czWords[new Random().nextInt(czWords.length)];
	}

    /**
     * Test if the word contains czech translation(s)
     * @param word Czech word or words being tested
     * @return true if the word contains the czech translation
     */
    public boolean containsCz(String word){
        return (word.contains(Constants.DELIMITER) && getAllCzFormatted().equalsIgnoreCase(word)) || Arrays.asList(czWords).contains(word);
    }

    //*******************************************ENGLISH*****************************************************

    /**
     * Sets english translation for the word and updates database.
     * @param eng english translation
     * @param db database connection
     */
	public void setEng(String eng, SQLiteDb db) {
		db.executeUpdate("UPDATE words SET english='"+eng.replaceAll("'", "''")+"' WHERE ID="+id+";");
		engWords=eng.split(Constants.DELIMITER);
		for(int i=0;i<engWords.length;i++) engWords[i]=engWords[i].trim();
		
	}

    /**
     * Gets all english translations as a single String separated by semicolon
     * @return All english translations formatted as "First; Second; Third"
     */
    public String getAllEngFormatted() {
		StringBuilder finalWord = new StringBuilder();
		for(String word : engWords) {
			finalWord.append(word);
			finalWord.append(Constants.DELIMITER);
			finalWord.append(" ");
		}
		return finalWord.substring(0, finalWord.length()-2);
	}

    /**
     * Gets all english translations
     * @return List of all english translations
     */
	public List<String> getAllEng(){
		return new ArrayList<>(Arrays.asList(engWords));
	}

    /**
     * Gets random english translation.
     * @return Random english translation
     */
	public String getEng() {
		return engWords[new Random().nextInt(engWords.length)];
	}

    /**
     * Test if the word contains english translation(s)
     * @param word English word or words being tested
     * @return true if the word contains the english translation
     */
	public boolean containsEng(String word){
		return (word.contains(Constants.DELIMITER)&& getAllEngFormatted().equals(word))||Arrays.asList(engWords).contains(word);
	}

    //*******************************************DEFINITION*****************************************************

    /**
     * Sets definition for the word and updates database
     * @param definition definition
     * @param db database connection
     */
	public void setDefinition(String definition, SQLiteDb db) {
		db.executeUpdate("UPDATE words SET definition='"+definition.replaceAll("'", "''").trim()+"' WHERE ID="+id+";");
		this.definition =definition.trim();
	}

    /**
     * Gets definition of the word as a String
     * @return definition of the word
     */
	public String getDefinition(){
		return definition;
	}

    /**
     * Tests whether the word has any definition
     * @return true if the word has definition specified
     */
	public boolean hasDefinition() {
		return !definition.trim().equals("");
	}

    /**
     * Tests whether the word contains the definition specified
     * @param definition definition to test for
     * @return true if the word contains specified definition
     */
	public boolean containsDefinition(String definition){
		return this.definition.equals(definition);
	}

    //*******************************************UNIT*****************************************************

    /**
     * Set unit of the word and update it in the database.
     * Also removes the word from previous unit.
     * @param unit unit to assign the number to
     * @param db database connection
     */
    public void setUnit(Unit unit, SQLiteDb db) {
        this.unit.removeWord(this);
        db.executeUpdate("UPDATE words SET unit="+unit.getNumber()+" WHERE ID="+id+";");
        unit.addWord(this);
        this.unit=unit;
    }

    /**
     * Gets unit number of the word
     * @return Unit number
     */
	public int getUnitNo() {
		return unit.getNumber();
	}

    /**
     * Gets unit enum of the word
     * @return Unit enum
     */
	public Unit getUnit() {
	    return unit;
    }

    //*******************************************SENTENCE*****************************************************

    /**
     * Gets all example sentences
     * @return List of all example sentences
     */
    public List<Sentence> getAllSentences() {
        return new ArrayList<>(sentences);
    }

    /**
     * Gets random example sentence.
     * @return Random example sentence
     */
    public Sentence getSentence() {
        return sentences.get(new Random().nextInt(sentences.size()));
    }

    /**
     * Gets if the word contains any example sentences.
     * @return true if the word contains at least one sentence
     */
    public boolean hasSentence(){
        return !sentences.isEmpty();
    }

    /**
     * Gets all example sentences as a single String separated by semicolon
     * @return All example sentences formatted as "First; Second; Third"
     */
    public String getAllSentencesFormatted() {
        if(!hasSentence()) return "";
        StringBuilder finalString = new StringBuilder();
        for(Sentence sentence : sentences) {
            finalString.append(sentence.getText());
            finalString.append(Constants.DELIMITER);
            finalString.append(" ");
        }
        return finalString.substring(0, finalString.length()-2);
    }

    /**
     * Sets example sentences for the word and updates database.
     * @param exampleSentences example sentences separated by semicolon ";"
     * @param db database connection
     */
    public void setSentences(String exampleSentences, SQLiteDb db) {
        db.executeUpdate("UPDATE words SET sentences='"+exampleSentences.replaceAll("'", "''")+"' WHERE ID="+id+";");
        sentences = Sentence.constructSentences(exampleSentences,this);
    }

    //*********************************************OTHERS******************************************************

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    /**
     * Deletes word from unit, revision and database.
     */
    public void delete(){
        SQLiteDb db = new SQLiteDb();
        db.executeUpdate("DELETE FROM words WHERE ID="+ id +";" );
        db.closeConnection();
        Unit.REVISION.removeWord(this);
        unit.removeWord(this);
    }

    /**
     * Saves new word to database, retrieves its ID and adds the word to appropriate unit.
     */
    public void initialize(){
        SQLiteDb db = new SQLiteDb();
        db.executeUpdate("INSERT INTO words (unit,english,czech,definition,sentences) VALUES ("
                + unit.getNumber() + ", '"
                + getAllEngFormatted().replaceAll("'", "''") + "', '"
                + getAllCzFormatted().replaceAll("'", "''") + "', '"
                + definition.replaceAll("'", "''").trim() + "', '"
                + getAllSentencesFormatted().replaceAll("'","''") + "')");
        try {
            ResultSet rs = db.executeResultQuery("SELECT seq FROM sqlite_sequence WHERE name='words';");
            rs.next();
            id = Integer.parseInt(rs.getString("seq"));
        } catch (SQLException e) {e.printStackTrace();}
        db.closeConnection();
        unit.addWord(this);
        if(unit!=Unit.REVISION && unit!=Unit.EXTRA_READING) Unit.REVISION.addWord(this);
    }

}
