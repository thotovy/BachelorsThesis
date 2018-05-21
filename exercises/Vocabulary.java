package com.zotmer.heit.exercises;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.data.SQLiteDb;
import com.zotmer.heit.data.Unit;
import com.zotmer.heit.data.Word;
import com.zotmer.heit.gui.*;
import com.zotmer.heit.gui.controls.CustomTextArea;
import com.zotmer.heit.gui.controls.CustomTextField;
import com.zotmer.heit.gui.controls.TopBar;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Vocabulary {
		private TableView<Word> table;
		private ObservableList<Word> masterData = FXCollections.observableArrayList();
		private Unit unit;
		private int unitInput;
	public Vocabulary(Unit unit) {
		this.unit=unit;
		BorderPane layoutMain = new BorderPane();
		table=new TableView<>();
		masterData.setAll(unit.getAllWords());

		if(unit==Unit.REVISION) table.getColumns().add(newColumnUnit());
		table.getColumns().add(newColumn("English", "allEngFormatted"));
		table.getColumns().add(newColumn("Czech","allCzFormatted"));
		table.getColumns().add(newColumn("Definition","definition"));
		table.setItems(masterData);
		table.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
		table.getSortOrder().add(table.getColumns().get(0));
		layoutMain.setTop(toolbar());
		layoutMain.setCenter(table);
        GuiUtils.createAndInitializeScene(layoutMain);
		Main.getStage().setTitle(Constants.EXERCISE_NAME_VOCABULARY + " - "+unit.toString());
	}
	private TableColumn<Word, String> newColumn(String name,String data){
		TableColumn<Word, String> column = new TableColumn<>(name);
		column.setMinWidth(100);
		if(name.equals("Definition")) {
			DoubleBinding usedWidth = Bindings.createDoubleBinding(() -> 18d);
			for(TableColumn<Word, ?> col : table.getColumns()) usedWidth=usedWidth.add(col.widthProperty()); //gets width used
			column.prefWidthProperty().bind(table.widthProperty().subtract(usedWidth)); //rest of table width
		}
		else column.prefWidthProperty().bind(table.widthProperty().divide(5)); // 1/5 of table width
		column.setCellValueFactory(new PropertyValueFactory<>(data));
		column.setCellFactory(tc-> {
			TableCell<Word, String> cell = new TableCell<>();
		    Text text = new Text();
		    cell.setGraphic(text);
		    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		    text.setStyle("-fx-fill: -fx-text-background-color;");
		    text.setFontSmoothingType(FontSmoothingType.LCD);
		    text.wrappingWidthProperty().bind(column.widthProperty().subtract(5));
		    text.textProperty().bind(cell.itemProperty());
		    return cell;
		});
		return column;
	}
	private TableColumn<Word, Integer> newColumnUnit(){
		TableColumn<Word, Integer> column = new TableColumn<>("Unit");
		column.setMinWidth(50);
		column.setPrefWidth(50);
		column.setCellValueFactory(new PropertyValueFactory<>("unitNo"));
		column.setCellFactory(tc-> new TableCell<Word,Integer>() {
            private Text text = new Text();
            {
                this.setGraphic(text);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setStyle("-fx-fill: -fx-text-background-color;");
                text.setFontSmoothingType(FontSmoothingType.LCD);
                text.wrappingWidthProperty().bind(column.widthProperty().subtract(5));
            }
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    text.setText(null);
                } else {
                    text.setText(item.toString());
                }
            }
        });
		return column;
	}
	private TextField filter() {
		FilteredList<Word> filteredData = new FilteredList<>(masterData,e->true);
		SortedList<Word> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		TextField filter = new TextField();
		filter.setPromptText("Filter");
		filter.textProperty().addListener((observableValue,oldFilter,newFilter)->
			filteredData.setPredicate((Predicate<? super Word>) word-> word.getAllCzFormatted().toLowerCase().contains(newFilter.toLowerCase())
                || word.getAllEngFormatted().toLowerCase().contains(newFilter.toLowerCase())
                || unit==Unit.REVISION && String.valueOf(word.getUnitNo()).equals(newFilter))
		);
		return filter;
	}
	private TopBar toolbar() {
		ArrayList<Node> buttons = new ArrayList<>();
		buttons.add(filter());
		if(Main.isAdmin()) {
            Button addWord = new Button("Add");
            addWord.setOnAction(e->addWord());
            buttons.add(addWord);

            Button deleteWord = new Button("Delete");
            deleteWord.setOnAction(e->deleteWord());
            buttons.add(deleteWord);

            Button editWord = new Button("Edit");
            editWord.setOnAction(e->editWord());
			buttons.add(editWord);

            Button copyWord = new Button("Copy");
            copyWord.setOnAction(e->copyWord());
			buttons.add(copyWord);
		}
		return new TopBar.TopBarBuilder().mainMenu().nodes(buttons).build(Help.USER_VOCABULARY);
	}

	private Word dialog(String title, Unit unit, String cz,String en,String def, String sent) {
	    if(!SQLiteDb.createExternalIfNotPresent()){
	        AlertMessage.error(AlertMessage.ERROR_NO_EXTERNAL_DB);
	        return null;
        }
		ChoiceDialog<Word> dialog = new ChoiceDialog<>();
		GridPane layout= new GridPane();
		dialog.setTitle(title);
		dialog.setHeaderText("");
		TextField unitField = addDialogRow(new TextField(String.valueOf(unit.getNumber())),"Unit","Unit:", 0, layout);
		if(unit==Unit.REVISION) unitField.setText("");
		CustomTextField english= addDialogRow(new CustomTextField(en),"Separate synonyms with '" + Constants.DELIMITER + "' symbol","English:", 1, layout);
		CustomTextField czech = addDialogRow(new CustomTextField(cz),"Separate synonyms with '" + Constants.DELIMITER + "' symbol","Czech:", 2, layout);
		CustomTextArea definition = addDialogRow(new CustomTextArea(def),"Definition","Definition:", 3, layout);
        CustomTextArea sentences = addDialogRow(new CustomTextArea(sent),"Separate sentences with '" + Constants.DELIMITER + "' symbol","Sentences:", 4, layout);
		definition.setPrefRowCount(5);
		sentences.setPrefRowCount(5);
		layout.setVgap(5);
		layout.setHgap(5);
		dialog.getDialogPane().setContent(layout);
		dialog.getDialogPane().getStylesheets().add(Constants.LOCATION_STYLESHEET);
		dialog.setResultConverter(dialogBtn->{
            if(dialogBtn == ButtonType.OK && validateValues(english, czech, unitField)) {
                if(!sentences.getText().isEmpty() && !validateSentences(sentences.getText(), english.getText())) return null;
                return new Word(english.getText(), czech.getText(), definition.getText(), Unit.getUnit(unitInput), sentences.getText());

            }
			return null;
		});
		return dialog.showAndWait().orElse(null);
	}

	private <T extends TextInputControl> T addDialogRow(T field, String prompt, String label, int row, GridPane layout) {
		field.setPromptText(prompt);
		field.setPrefWidth(300);
		Label lbl = new Label(label);
		lbl.setPadding(new Insets(4,0,0,0));
		layout.getChildren().add(lbl);
		GridPane.setConstraints(lbl, 0, row, 1, 1, HPos.RIGHT, VPos.BASELINE);
		layout.add(field, 1, row);
		return field;
	}

	private void addWord(){
        Word word=dialog("Add word",unit,"","","","");
        if(word==null) return;
        word.initialize();
        masterData.add(word);
        table.sort();
        table.refresh();
        AlertMessage.information(String.format(AlertMessage.INFORMATION_WORD_SUCCESSFULLY, "added"));
    }

    private void deleteWord(){
        if(validateSelection()) return;
        else if(!SQLiteDb.createExternalIfNotPresent()){
            AlertMessage.error(AlertMessage.ERROR_NO_EXTERNAL_DB);
            return;
        }
        if(AlertMessage.confirmation(String.format(AlertMessage.CONFIRMATION_DELETE, table.getSelectionModel().getSelectedItem().getAllCzFormatted()))) {
            table.getSelectionModel().getSelectedItem().delete();
            masterData.remove(table.getSelectionModel().getSelectedItem());
            AlertMessage.information(String.format(AlertMessage.INFORMATION_WORD_SUCCESSFULLY, "deleted"));
        }
	}

    private void editWord() {
        if(validateSelection()) return;
        Word toEdit = table.getSelectionModel().getSelectedItem();
        Word temp = dialog("Edit word", toEdit.getUnit(), toEdit.getAllCzFormatted(), toEdit.getAllEngFormatted(), toEdit.getDefinition(), toEdit.getAllSentencesFormatted());
        if (temp == null) return;
        SQLiteDb db = new SQLiteDb();
        toEdit.setCz(temp.getAllCzFormatted(), db);
        toEdit.setEng(temp.getAllEngFormatted(), db);
        toEdit.setDefinition(temp.getDefinition(), db);
        toEdit.setUnit(temp.getUnit(), db);
        toEdit.setSentences(temp.getAllSentencesFormatted(), db);
        db.closeConnection();
        if(this.unit!=Unit.REVISION && this.unit!=toEdit.getUnit()) masterData.remove(toEdit);
        table.sort();
        table.refresh();
        AlertMessage.information(String.format(AlertMessage.INFORMATION_WORD_SUCCESSFULLY, "changed"));
    }

    private void copyWord(){
        if(validateSelection()) return;
        Word toCopy= table.getSelectionModel().getSelectedItem();
        Word newWord = dialog("Copy word", toCopy.getUnit(), toCopy.getAllCzFormatted(), toCopy.getAllEngFormatted(), toCopy.getDefinition(), toCopy.getAllSentencesFormatted());
        if(newWord==null) return;
        newWord.initialize();
        if(this.unit==Unit.REVISION || this.unit==newWord.getUnit()) masterData.add(newWord);
        table.sort();
        table.refresh();
        AlertMessage.information(String.format(AlertMessage.INFORMATION_WORD_SUCCESSFULLY, "copied"));
    }
    private boolean validateValues(TextField english, TextField czech, TextField unitField){
        try{
            unitInput = Integer.parseInt(unitField.getText());
        }catch(NumberFormatException e) {
            AlertMessage.error(AlertMessage.ERROR_UNIT_MISSING);
            return false;
        }
        if(english.getText().equals("") || czech.getText().equals("")){
            AlertMessage.error(AlertMessage.ERROR_NOT_ENOUGH_INFO);
            return false;
        }
        else if(unitInput >= Unit.values().length || unitInput == 0){
            AlertMessage.error(AlertMessage.ERROR_INCORRECT_UNIT);
            return false;
        }
        return true;
    }

    private boolean validateSelection(){
        if(table.getSelectionModel().getSelectedItem()==null) {
            AlertMessage.error(AlertMessage.ERROR_NO_SELECTION);
            return true;
        }
        return false;
    }

    private boolean validateSentences(String sentences, String english){
	    for(String sentence : sentences.split(";")){
            boolean contains=false;
            for(String eng : english.split(";")){
                eng = eng.trim();
                if(Pattern.compile(Pattern.quote(eng), Pattern.CASE_INSENSITIVE).matcher(sentence).find()){
                    contains = true;
                }
            }
            if(!contains){
                AlertMessage.error(String.format(AlertMessage.ERROR_SENTENCE,sentence));
                return false;
            }
	    }
        return true;
    }
}
