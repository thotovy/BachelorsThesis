package com.zotmer.heit.gui.controls;

import javafx.scene.control.TextArea;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Standard text area with added Drag&Drop functionality.
 */
public class CustomTextArea extends TextArea {
    public CustomTextArea(){
        this("");
    }
    public CustomTextArea(String text){
        super(text);
        setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });
        setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();
            if (e.getTransferMode() == TransferMode.COPY && dragboard.hasString()) {
                appendText(dragboard.getString());
                e.setDropCompleted(true);
            }
            e.consume();
        });
    }
}
