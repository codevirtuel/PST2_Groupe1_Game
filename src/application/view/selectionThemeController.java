package application.view;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.gestionThemes.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class selectionThemeController {

	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView<String> listeTheme;
	
	@FXML
	TableView<Score> listeScore;
	@FXML
	TableColumn<Score, String> placementCol;
	@FXML
	TableColumn<Score, String> joueurCol;
	@FXML
	TableColumn<Score, String> scoreCol;
	
	@FXML
	public void initialize() {
		placementCol.setCellValueFactory(cellData -> cellData.getValue().placementProperty());
		joueurCol.setCellValueFactory(cellData -> cellData.getValue().joueurProperty());
		scoreCol.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());
		
		listeScore.setFixedCellSize(30);
		
		Scaler.updateSize(Main.width,vbox);
		try {
			populateThemeList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Récupère la liste des thèmes
	public List<String> getThemeList() throws SQLException{
		List<String> themes = new ArrayList<String>();
			
		//Connect to the database to request theme name
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM THEME");
		while(result.next()) {
			themes.add(result.getString("NOM_THEME"));
		}
		return themes;
	}
	
	public void populateThemeList() throws SQLException {
		ObservableList<String> liste = FXCollections.observableArrayList();
		for(String nom : getThemeList()) {
			liste.add(nom);
		}
		
		listeTheme.setItems(liste);
	}
	
	//Récupère la site des score pour un theme
	public ObservableList<Score> loadThemeScore(String nomTheme) throws SQLException {
		ObservableList<Score> liste = FXCollections.observableArrayList();
		int placement = 1;
		
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM JOUEUR WHERE NOM_THEME="+"'"+nomTheme+"'"+" ORDER BY SCORE_JOUEUR DESC");
		while(result.next()) {
			liste.add(new Score(placement,result.getString("NOM_JOUEUR"),result.getInt("SCORE_JOUEUR")));
			placement++;
		}
		
		return liste;
	}
	
	//Actualise la liste des scores
	@FXML
	public void showThemeScore() throws SQLException {
		String nomTheme = listeTheme.getSelectionModel().getSelectedItem().toString();
		
		listeScore.setItems(loadThemeScore(nomTheme));
	}
	
	//---- Switch scene ----
	
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
	
	@FXML
	public void goToGame() throws IOException {
		VBox root = new VBox();
		gameController.primaryStage = primaryStage;
		gameController.nomTheme = listeTheme.getSelectionModel().getSelectedItem().toString();
		
		root = FXMLLoader.load(getClass().getResource("pageDeJeu.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
}
