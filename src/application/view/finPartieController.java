package application.view;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import application.Main;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.gestionThemes.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class finPartieController {

	public static Stage primaryStage;

	public static List<Question> listQuestions = new ArrayList<Question>();
	public static List<Boolean> reponseQuestions = new ArrayList<Boolean>();

	public static Theme nomTheme;
	public static int tempsTotal;
	public static int score;
	
	private Image IMAGE_PASS;
	private Image IMAGE_FAIL;
	
	@FXML
	VBox vbox;

	@FXML
	ListView list;

	@FXML
	AnchorPane anchor;

	@FXML
	Label nbBonnesQuestions;

	@FXML
	Label temps;
	@FXML
	Label scorePartie;
	
	//-- FXML functions --
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		double factor = Scaler.getFactor();
		IMAGE_PASS = new Image("File:./src/application/data/pass.png",20*factor,20*factor,true,false);
		IMAGE_FAIL = new Image("File:./src/application/data/fail.png",20*factor,20*factor,true,false);
		
		afficherNbBonnesReponces();
		afficherListe();
		afficherScoreTemps();
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

	@SuppressWarnings("unchecked")
	@FXML
	public void afficherListe() {
		ObservableList<String> liste = FXCollections.observableArrayList();
		for(int i=0;i<listQuestions.size();i++) {
			String text = listQuestions.get(i).getIntitule();
			liste.add(text);
		}
		list.setItems(liste);
		
		//Ajouter les images
		list.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                	int getId = 0;
                	for(Question q : listQuestions) {
                		if(q.getIntitule().equals(name)) {
                			getId = listQuestions.indexOf(q);
                			break;
                		}
                	}
                	
                	if(reponseQuestions.get(getId)) {
                		imageView.setImage(IMAGE_PASS);
                	}else {
                		imageView.setImage(IMAGE_FAIL);
                	}
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
		
	}
	
	//-- Functions --

	public void afficherScoreTemps() {
		temps.setText(tempsTotal+" s");
		scorePartie.setText(score+" pts");
	}
	
	public void insertScore(String j1) throws SQLException{
		boolean isHere = false;
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM JOUEUR WHERE "
				+ "NOM_THEME = " + "'" +nomTheme.getNom()+ "'");
		while(result.next()){
			if(j1.equals(result.getString("NOM_JOUEUR"))){
				isHere = true;
			}
		}
		if(isHere){
			result = Main.bdd.executeCmd("SELECT SCORE_JOUEUR FROM JOUEUR WHERE "
					+ "NOM_THEME = " + "'" +nomTheme.getNom()+ "'" );
			while(result.next()){
				if(score > result.getInt("SCORE_JOUEUR")){
					ResultSet result2 = Main.bdd.executeCmd("UPDATE JOUEUR SET SCORE_JOUEUR = " + score + 
							" WHERE NOM_THEME = " + "'" +nomTheme.getNom()+ "'" + 
							" AND NOM_JOUEUR = " + "'" + j1 + "'"); 
				}
			}
		}else{
			int lastID = 0;
			result = Main.bdd.executeCmd("SELECT MAX(ID_JOUEUR) AS ID_JOUEUR FROM JOUEUR");
			while(result.next()){
				lastID = result.getInt("ID_JOUEUR");
			}
			
			lastID++;
			boolean result3 = Main.bdd.executeUpdate("INSERT INTO JOUEUR(ID_JOUEUR,SCORE_JOUEUR, TEMPS_JOUEUR, NOM_THEME, NOM_JOUEUR)"
					+ "VALUES("+lastID+"," + score + "," + tempsTotal + "," + 
					"'" + nomTheme.getNom() + "'" + "," + "'" +j1+ "'"
					+ ")");
		}
		
	}
	
	//-- Switch stage --
	
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
	public void pop( ) throws SQLException, IOException {

		TextInputDialog nomJoueur = new TextInputDialog("");
		nomJoueur.setTitle("Enregistrez le score");
		nomJoueur.setHeaderText("Entrez votre nom ");
		nomJoueur.setGraphic(null);
		nomJoueur.setContentText("Pseudo :");

		Optional<String> textIn = nomJoueur.showAndWait();
		if (textIn.isPresent()) {
		insertScore(textIn.get());
		goToAccueil();
		}
	}
	
}
