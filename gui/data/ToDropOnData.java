package com.zotmer.heit.gui.data;

import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

public class ToDropOnData extends Label{
    private int index;
    private ToDragData assignedTo;
    private ToDragData correctAnswer;

    public ToDropOnData(String text, int index, ToDragData correctAnswer, int width) {
        super(text);
        this.index = index;
        this.correctAnswer = correctAnswer;
        initializeDropOn();
        setPrefWidth(width);
        getStyleClass().add("to-drop-on");
    }


    private void initializeDropOn(){
        setOnDragOver(e->{
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });
        setOnDragEntered(e-> setTextFill(Color.GREEN));
        setOnDragExited(e-> setTextFill(Color.BLACK));
        setOnDragDropped(e->{
            ToDragData toDrag =((ToDragData) e.getGestureSource());

            if(assignedTo!=null){
                assignedTo.removeAssignedTo();
                removeAssignedTo();
            }
            toDrag.setAssignedTo(this);
            setAssignedTo(toDrag);

            e.setDropCompleted(true);
            e.consume();
        });
    }

    public int getIndex() {
        return index;
    }

    public ToDragData getAssignedTo() {
        return assignedTo;
    }

    private void setAssignedTo(ToDragData assignedTo) {
        this.assignedTo = assignedTo;
        setId("to-drop-on-assigned");
    }

    public void removeAssignedTo(){
        assignedTo = null;
        setId("");
    }

    public ToDragData getCorrectAnswer() {
        return correctAnswer;
    }
}
