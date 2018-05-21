package com.zotmer.heit.gui;

import com.zotmer.heit.data.Constants;
import com.zotmer.heit.data.Unit;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Selection {
	private ChoiceBox<String> units;
	private RadioButton toEng;
    private RadioButton hard;
	private boolean isConfirmed;
	private Selection(SelectionBuilder builder) {
		this.units=builder.units;
		this.toEng=builder.toEng;
		this.hard=builder.hard;
		this.isConfirmed=builder.isConfirmed;
	}
	public static class SelectionBuilder {
		private ChoiceDialog<Boolean> dialog;
		private ChoiceBox<String> units;
		private RadioButton toEng;
        private RadioButton hard;
		private GridPane rootLayout;
		private boolean isConfirmed;
		public SelectionBuilder(String title) {
			rootLayout=new GridPane();
			rootLayout.setAlignment(Pos.CENTER);
			rootLayout.setVgap(20);
			rootLayout.setHgap(10);
			dialog = new ChoiceDialog<>();
			dialog.setHeaderText("");
			dialog.setTitle(title);
			dialog.getDialogPane().setContent(rootLayout);
            GuiUtils.addIcon((Stage) dialog.getDialogPane().getScene().getWindow());
			dialog.setResultConverter(dialogBtn->dialogBtn == ButtonType.OK);
		}
		public SelectionBuilder addUnits() {
			rootLayout.add(new Label("Select unit:"), 0, 0);
			units = new ChoiceBox<>();
			for(Unit unit : Unit.values()) units.getItems().add(unit.toString());
			units.setPrefWidth(200);
			units.setValue(units.getItems().get(0));
			rootLayout.add(units, 1, 0, 2, 1);
			return this;
		}
		public SelectionBuilder addDifficulty() {
			hard = new RadioButton("Hard");
			RadioButton easy = new RadioButton("Easy");
			ToggleGroup btns = new ToggleGroup();
			easy.setSelected(true);
			easy.setToggleGroup(btns);
			hard.setToggleGroup(btns);
			rootLayout.add(new Label("Difficulty:"),0,1);
			rootLayout.add(easy, 1, 1);
			rootLayout.add(hard, 2, 1);
			return this;
		}
		public SelectionBuilder addLanguage(String text) {
			toEng = new RadioButton("English");
			RadioButton toCz = new RadioButton("Czech");
			ToggleGroup btns = new ToggleGroup();
			toCz.setSelected(true);
			toEng.setToggleGroup(btns);
			toCz.setToggleGroup(btns);
			rootLayout.add(new Label(text),0,2);
			rootLayout.add(toCz,1,2);
			rootLayout.add(toEng,2,2);
			return this;
		}
		public Selection buildSelection() {
		    dialog.getDialogPane().getStylesheets().add(Constants.LOCATION_STYLESHEET);
			isConfirmed = dialog.showAndWait().orElse(false);
			return new Selection(this);
		}
	}
	public boolean isHard() {
		return hard.isSelected();
	}
	public boolean isEng() {
		return toEng.isSelected();
	}
	public Unit getUnit(){
		return Unit.getUnit(units.getValue());
	}
	public boolean isConfirmed() {
		return isConfirmed;
	}
}
