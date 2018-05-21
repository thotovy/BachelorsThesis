package com.zotmer.heit.gui.exercise;

import com.zotmer.heit.gui.AlertMessage;
import com.zotmer.heit.gui.Main;
import com.zotmer.heit.gui.data.ToDragData;
import com.zotmer.heit.gui.data.ToDropOnData;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DragAndDropGUI implements ExerciseGUI {
    private VBox layoutUnassigned = new VBox(4);
    private GridPane layoutAssigned = new GridPane();
    private ScrollPane mainPane = new ScrollPane();
    private ArrayList<ToDropOnData> dropOnList = new ArrayList<>();
    private int dropOnWidth;

    public DragAndDropGUI(int dropOnWidth){
        this.dropOnWidth = dropOnWidth;
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(dropOnWidth);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.prefWidthProperty().bind(Main.getStage().widthProperty().subtract(col1.prefWidthProperty()).subtract(layoutUnassigned.prefWidthProperty()).subtract(45));
        col2.setMinWidth(100);
        layoutAssigned.getColumnConstraints().addAll(col1,col2);
        layoutAssigned.setPadding(new Insets(0,10,0,0));
        layoutAssigned.setHgap(3);
        layoutUnassigned.getStyleClass().add("to-drag-unassigned");
        layoutUnassigned.setPrefWidth(200);
        onDropElsewhere();
    }

    public void addOption(String toDrag, String toDropOn){
        ToDragData toDragData = new ToDragData(toDrag, layoutAssigned, layoutUnassigned);
        ToDropOnData toDropOnData =new ToDropOnData(toDropOn, dropOnList.size(), toDragData, dropOnWidth);
        GridPane.setConstraints(toDropOnData, 0, dropOnList.size());
        dropOnList.add(toDropOnData);
        layoutUnassigned.getChildren().add(toDragData);
        layoutAssigned.getChildren().add(toDropOnData);
        setToDrag(toDragData);
    }

    private void onDropElsewhere() {
        mainPane.setOnDragOver(e->{
            if(e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        mainPane.setOnDragDropped(e->{
            if(e.getGestureSource() instanceof ToDragData){
                ((ToDragData) e.getGestureSource()).removeAssignedToBidirectional();
            }
        });
    }

    private void setToDrag(ToDragData toDrag) {
        toDrag.setOnDragDetected(e->{
            Dragboard db = toDrag.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(toDrag.getText());
            db.setContent(content);

            for(ToDropOnData dropOn : dropOnList){
                dropOn.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.DASHED,new CornerRadii(3), new BorderWidths(1), new Insets(3))));
            }
            e.consume();
        });

        toDrag.setOnDragDone(e->{
            for(ToDropOnData dropOn : dropOnList) {
                dropOn.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.DASHED,new CornerRadii(3), new BorderWidths(1), new Insets(3))));
            }
            e.consume();
        });
    }

    public ScrollPane getDisplay() {
        FXCollections.shuffle(layoutUnassigned.getChildren());
        BorderPane pane = new BorderPane();
        pane.setLeft(layoutAssigned);
        pane.setRight(layoutUnassigned);
        pane.setPadding(new Insets(5));
        mainPane.setContent(pane);
        return mainPane;
    }

    public boolean getResult() {
        if(isFilled()){
            AlertMessage.information(String.format(AlertMessage.INFORMATION_RESULT, getCorrectAnswers(), dropOnList.size()),"Result");
            return true;
        }
        return false;
    }

    private boolean isFilled(){
        for(ToDropOnData dropOn : dropOnList){
            if(dropOn.getAssignedTo()==null) return AlertMessage.confirmation(AlertMessage.CONFIRMATION_SUBMIT);
        }
        return true;
    }

    private int getCorrectAnswers(){
        int correct=0;
        for(ToDropOnData dropOn : dropOnList){
            ToDragData correctAnswer = dropOn.getCorrectAnswer();
            if(dropOn.getAssignedTo()!=null && correctAnswer.equals(dropOn.getAssignedTo())){
                correct++;
                correctAnswer.setId("correct-answer");
            }
            else{
                correctAnswer.setId("incorrect-answer");
                if(!layoutAssigned.getChildren().contains(correctAnswer)) layoutAssigned.getChildren().add(correctAnswer);
                GridPane.setConstraints(correctAnswer, 1, dropOn.getIndex());
            }
            correctAnswer.setMouseTransparent(true);
            correctAnswer.setFocusTraversable(true);
        }
        return correct;
    }
}
