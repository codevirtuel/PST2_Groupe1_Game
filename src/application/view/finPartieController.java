package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class finPartieController {
	
	public static Stage primaryStage;
	
	public static List<Question> listQuestions = new ArrayList<Question>();
	public static List<Boolean> reponseQuestions = new ArrayList<Boolean>();
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView list;
	
	@FXML
	AnchorPane anchor;
	
	@FXML
	Label nbBonnesQuestions;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		afficherNbBonnesReponces();
		afficherListe();
	}
	
	@FXML
	public void afficherNbBonnesReponces() {
		String modele1 = "Vous avez ";
		String modele2 = " bonnes réponces";
		int nb = 0;
		for(boolean b : reponseQuestions) {
			if(b) nb++;
		}
		nbBonnesQuestions.setText(modele1+nb+modele2);
	}
	
	@FXML
	public void afficherListe() {
		ObservableList<String> liste = FXCollections.observableArrayList();
		for(int i=0;i<listQuestions.size();i++) {
			String text = listQuestions.get(i).getIntitule()+"								";
			if(reponseQuestions.get(i)) {
				text += "true";
			}
			else {
				text += "false";
			}
			liste.add(text);
		}
		list.setItems(liste);
	}
	
	@FXML 
	public void goToAccueil() throws IOException {
		VBox root = new VBox();
		accueilController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Jeu - Accueil.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
