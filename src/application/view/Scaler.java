package application.view;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
		scale(root,factor);
	}
	
	public static void scale(Node obj, double factor) {
		
		if(obj.getParent() instanceof AnchorPane) {
			//AnchorPane parent = (AnchorPane) obj.getParent();
			double anchorFactor = Math.round(factor);
			//Left anchor
			if(AnchorPane.getLeftAnchor(obj) != null) AnchorPane.setLeftAnchor(obj, AnchorPane.getLeftAnchor(obj)*anchorFactor);
			//Right anchor
			if(AnchorPane.getRightAnchor(obj) != null) AnchorPane.setRightAnchor(obj, AnchorPane.getRightAnchor(obj)*anchorFactor);
			//Top anchor
			if(AnchorPane.getTopAnchor(obj) != null) AnchorPane.setTopAnchor(obj, AnchorPane.getTopAnchor(obj)*anchorFactor);
			//Bottom anchor
			if(AnchorPane.getBottomAnchor(obj) != null) AnchorPane.setBottomAnchor(obj, AnchorPane.getBottomAnchor(obj)*anchorFactor);
		}
		
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
			((Region) obj).setPadding(new Insets(
					((Region) obj).getPadding().getTop()*factor,
					((Region) obj).getPadding().getRight()*factor,
					((Region) obj).getPadding().getBottom()*factor,
					((Region) obj).getPadding().getLeft()*factor)
			);
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
