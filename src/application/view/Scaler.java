package application.view;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/*
 * 	Syntaxe :
 * 	updateSize(nouvelleLongueur,main container)
 */

public class Scaler {
	
	public static void updateSize(double newWidth,Pane root) {		
		double factor = newWidth/720;
		
		ArrayList<Node> nodes = getAllNodes(root);
		for(Node node : nodes) {
			if(!(node instanceof HBox)) {
				scale(node,factor);
			}
		}
	}
	
	public static void scale(Node obj, double factor) {
		if(obj instanceof Label) {
			((Labeled) obj).setFont(new Font(((Labeled) obj).getFont().getSize()*factor));
		}
		else if(obj instanceof ImageView) {
			((ImageView) obj).setFitHeight(((ImageView) obj).getFitHeight()*factor);
			((ImageView) obj).setFitWidth(((ImageView) obj).getFitWidth()*factor);
		}
		else if(obj instanceof Button) {
			((Button) obj).setFont(new Font(((Button) obj).getFont().getSize()*factor));
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
		else{
			((Region) obj).setPrefSize(((Region) obj).getPrefWidth()*factor, ((Region) obj).getPrefHeight()*factor);
		}
	}
	


	public static ArrayList<Node> getAllNodes(Parent root) {
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    addAllDescendents(root, nodes);
	    return nodes;
	}
	
	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
	    for (Node node : parent.getChildrenUnmodifiable()) {
	        nodes.add(node);
	        if (node instanceof Parent)
	            addAllDescendents((Parent)node, nodes);
	    }
	}
	
}
