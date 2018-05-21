package com.zotmer.heit.gui.controls;

import com.zotmer.heit.gui.Help;
import com.zotmer.heit.gui.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Specifically tweaked toolbar.
 */
public class TopBar extends ToolBar{
	private TopBar(ArrayList<Node> leftNodes,ArrayList<Node> centerNodes,ArrayList<Node> rightNodes) {
		Pane leftSpacer = new Pane();
		HBox.setHgrow(leftSpacer, Priority.SOMETIMES);
		Pane rightSpacer = new Pane();
		HBox.setHgrow(rightSpacer, Priority.SOMETIMES);
        getItems().addAll(leftNodes);
        getItems().add(leftSpacer);
        getItems().addAll(centerNodes);
        getItems().add(rightSpacer);
        getItems().addAll(rightNodes);
	}

	public static class TopBarBuilder{
	    private ArrayList<Node> centerNodes = new ArrayList<>();
        private ArrayList<Node> rightNodes = new ArrayList<>();
        private ArrayList<Node> leftNodes = new ArrayList<>();

        /**
         * Adds nodes to navigation bar
         * @param nodes collection of nodes to be put on the navigation bar
         */
	    public TopBarBuilder nodes(Collection<Node> nodes){
            centerNodes.addAll(nodes);
            return this;
        }

        /**
         * Adds Submit button to navigation bar
         * @param submitHandler the action that the submit button should perform
         */
        public TopBarBuilder submit(EventHandler<ActionEvent> submitHandler) {
            Button submit = new Button("Submit");
            submit.setOnAction(submitHandler);
            submit.setDefaultButton(true);
            centerNodes.add(submit);
            return this;
        }

        /**
         * Adds Main menu button to navigation bar
         */
        public TopBarBuilder mainMenu(){
            Button btnMenu = new Button("Main menu");
            btnMenu.setOnAction(e-> Main.returnToMainPage());
            btnMenu.setCancelButton(true);
            centerNodes.add(btnMenu);
            return this;
        }

        /**
         * Builds the navigation bar and adds help button
         * @return Navigation bar
         */
        public TopBar build(Help help){
            Button btnHelp = new Button("Help");
            btnHelp.setOnAction(e-> Help.displayHelp(help));
            leftNodes.add(btnHelp);
	        return new TopBar(leftNodes,centerNodes,rightNodes);
        }
    }
}
