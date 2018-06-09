package application.view;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class finPartieController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView list;
	
	@FXML
	AnchorPane anchor;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML
	public void pop( ) {
//		 JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
//		 String nom = jop.showInputDialog(null, "Username : ", "Enregistrez-vous ",JOptionPane.DEFAULT_OPTION);
//		    jop2.showMessageDialog(null, "Votre score est bien sauvegardé, " + nom, "Sauvegarde réussi", JOptionPane.INFORMATION_MESSAGE);
//		
	    String[] sauvegarde = {"Enregistrez", "Annuler"};
	    JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
	    int rang = jop.showOptionDialog(null, "Username :",
	      "Enregistrez-vous !",
	      JOptionPane.YES_NO_CANCEL_OPTION,
	      JOptionPane.QUESTION_MESSAGE,
	      null,
	      sauvegarde,
	      sauvegarde[1]);
	    
	    jop2.showMessageDialog(null, "MERCI D'AVOIR JOUER" , "Sauvegarde réussi", JOptionPane.INFORMATION_MESSAGE);
	}
}
