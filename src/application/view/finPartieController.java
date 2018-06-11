package application.view;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import application.Main;
import application.gestionThemes.Theme;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class finPartieController {
	
	public static Stage primaryStage;
	public static Theme nomTheme;
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
	public void pop( ) throws SQLException {

		TextInputDialog nomJoueur = new TextInputDialog("");
		nomJoueur.setTitle("Enregistrez le score");
		nomJoueur.setHeaderText("Entrez votre nom ");
		nomJoueur.setGraphic(null);
		nomJoueur.setContentText("Pseudo :");
	
		Optional<String> textIn = nomJoueur.showAndWait();
		if (textIn.isPresent()) {
			System.out.println("INSERT INTO JOUEUR(SCORE_JOUEUR, TEMPS_JOUEUR, NOM_THEME, NOM_JOUEUR)"
					+ "VALUES(" + 0 + "," + 0 + "," + "'" + nomTheme.getNom() + "'" + "," + "'" + textIn.get() + "'"
					+ ")");
//			ResultSet result = Main.bdd.executeCmd("INSERT INTO JOUEUR(SCORE_JOUEUR, TEMPS_JOUEUR, NOM_THEME, NOM_JOUEUR) "
//					+ " VALUES ("+0+ "," + 0 + "," + "'" + nomTheme.getNom() +"'"+ "," + "'"+ textIn.get()+ "' "+")");
			}
	}
}

