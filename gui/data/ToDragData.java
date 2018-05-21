package com.zotmer.heit.gui.data;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ToDragData extends Label{
    private ToDropOnData assignedTo;
    private Pane layoutAssigned;
    private Pane layoutUnassigned;

    public ToDragData(String text, Pane layoutAssigned, Pane layoutUnassigned) {
        super(text);
        this.layoutAssigned = layoutAssigned;
        this.layoutUnassigned = layoutUnassigned;
        getStyleClass().add("to-drag");
    }

    public void removeAssignedTo(){
        layoutAssigned.getChildren().remove(this);
        if(!layoutUnassigned.getChildren().contains(this)){
            layoutUnassigned.getChildren().add(this);
        }
        assignedTo=null;
    }

    public void removeAssignedToBidirectional(){
       if(assignedTo!=null){
           assignedTo.removeAssignedTo();
           removeAssignedTo();
       }
    }

    public void setAssignedTo(ToDropOnData assignedTo) {
        if(this.assignedTo != null) this.assignedTo.removeAssignedTo();
        this.assignedTo = assignedTo;
        if(!layoutAssigned.getChildren().contains(this)) layoutAssigned.getChildren().add(this);
        GridPane.setConstraints(this, 1, assignedTo.getIndex());
    }
}
