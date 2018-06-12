package application.view;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;

	private Theme theme = new Theme(nomTheme);

	private List<Zone> selectedZone = new ArrayList<Zone>();
	private int nbQuestion;
	private List<Question> listQuestions = new ArrayList<Question>();
	private List<Boolean> reponseQuestions = new ArrayList<Boolean>();
	private Question questionActuelle;
	double avancement;
	int scoreActuel = 0;
	private int idQuestion = 0;

	@FXML 
	VBox vbox;
	@FXML
	Label intituleQuestion;
	@FXML
	Label numeroQuestion;
	@FXML
	ProgressBar progression;
	@FXML
	Label pourcentage;
	@FXML
	Label score;

	@FXML 
	AnchorPane image;
	
	public void initialize() {
		Scaler.updateSize(Main.width, vbox);
		try {
			loadTheme();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		showZones();
		listQuestions = theme.getQuestions();
		Collections.shuffle(listQuestions);
		questionActuelle = listQuestions.get(idQuestion);
		showQuestion(questionActuelle);
		
		nbQuestion = theme.getQuestions().size();
		avancement = 1.0 / nbQuestion;
		System.out.println(theme.getQuestions().size());
		progression.setProgress(0);
		progression.setStyle("-fx-accent: green;");
		pourcentage.setText((int) (progression.getProgress() * 100) + " % effectués");
		score.setText((scoreActuel) + " / " + nbQuestion);
		numeroQuestion.setText(""+(idQuestion+1));
	}

	//Charge le thème via la classe Thème
	public void loadTheme() throws SQLException {
		//Load background
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM THEME WHERE NOM_THEME="+"'"+nomTheme+"'");
				while(result.next()) {
					String URL = "File:./src/application/data/";
					File image = new File(URL + result.getString("URL_IMAGE"));
					if(result.getString("URL_IMAGE") == null) {
						URL += "480x270.png";
					}else {
						URL += result.getString("URL_IMAGE");
					}

					theme.setImageFond(new Image(URL));
				}

		//Load zones

		double factor = Main.width/1280.0;
		System.out.println("Factor : "+factor);

		result = Main.bdd.executeCmd("SELECT * FROM ZONE WHERE NOM_THEME="+"'"+nomTheme+"'");
		while(result.next()) {
			int idZone = result.getInt("ID_ZONE");

			// Query points
			List<Double> points = new ArrayList<Double>();
			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM POINT WHERE ID_ZONE="+idZone);
			while(result2.next()) {
				points.add(result2.getDouble("POS_X")*factor);
				points.add(result2.getDouble("POS_Y")*factor);
			}
			theme.addZone(new Zone(idZone, points));
		}

		// Load questions
		result = Main.bdd.executeCmd("SELECT * FROM QUESTION WHERE NOM_THEME=" + "'" + nomTheme + "'");

		while (result.next()) {
			int questionId = result.getInt("ID_QUESTION");
			String questionIntitule = result.getString("INTITULE_QUESTION");
			List<Zone> questionZone = new ArrayList<Zone>();

			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM REPONSE WHERE ID_QUESTION=" + questionId);
			while (result2.next()) {
				if (theme.getZoneWithID(result2.getInt("ID_ZONE")) != null) {
					questionZone.add(theme.getZoneWithID(result2.getInt("ID_ZONE")));
				}
			}

			theme.addQuestion(new Question(questionIntitule, questionZone));
		}


		double height = image.getPrefHeight(), width = image.getPrefWidth(), rapport = height / width;
		if (theme.getImageFond().getWidth()
				* rapport > theme.getImageFond().getHeight()) {
			height = width * theme.getImageFond().getHeight()
					/ theme.getImageFond().getWidth();
			image.setPrefHeight(height);
		} else {
			width = height * theme.getImageFond().getWidth()
					/ theme.getImageFond().getHeight();
			image.setPrefWidth(width);
		}

		BackgroundImage bgImage = new BackgroundImage(theme.getImageFond(),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(width, height, false, false, false, true));
		image.setBackground(new Background(bgImage));
	}

	//-- gestion zone --

	public void showZones() {
		for(Zone z : theme.getZones()) {
			z.setId(""+z.getIndex());
			z.setOpacity(2.0);

			if (selectedZone.contains(z))
				z.setFill(Color.GREEN);
			else
				z.setFill(Color.RED);

			z.setStroke(Color.BLACK);
			z.setStrokeWidth(1);
			image.getChildren().add(z);
		}
	}

	public void removeZones() {
		for (Zone z : theme.getZones()) {
			image.getChildren().remove(z);
		}
	}

	public void updateZones() {
		removeZones();
		showZones();
	}

	@FXML
	public void clickOnZone(MouseEvent e) {
		String index = e.getPickResult().getIntersectedNode().getId();
		if (!index.equals("image")) {
			Zone correspondingZone = theme.getZoneWithID(Integer.valueOf(index));
			if (selectedZone.contains(correspondingZone))
				selectedZone.remove(correspondingZone);
			else
				selectedZone.add(correspondingZone);

			if (selectedZone.size() > theme.getQuestions().size()) {
				selectedZone.remove(0);
			}
		}
		updateZones();
	}

	// -- gestion questions --
	public void showQuestion(Question question) {
		// intitulï¿½
		intituleQuestion.setText(question.getIntitule());
	}

	public List<Zone> getAnwsers(Question question) throws SQLException {
		List<Zone> retour = new ArrayList<Zone>();

		ResultSet result = Main.bdd.executeCmd("SELECT * FROM QUESTION WHERE NOM_THEME=" + "'" + nomTheme + "'");
		int questionId = 0;
		while (result.next()) {
			questionId = result.getInt("ID_QUESTION");
		}

		result = Main.bdd.executeCmd("SELECT * FROM REPONSE WHERE ID_QUESTION=" + questionId);
		while (result.next()) {
			retour.add(theme.getZoneWithID(result.getInt("ID_ZONE")));
		}

		return retour;
	}

	public boolean isAnwserCorrect(Question question) throws SQLException {
		if (getAnwsers(question).equals(selectedZone)) {
			return true;
		} else
			return false;
	}

	@FXML

	public void valider() throws IOException, InterruptedException {
		try {
			System.out.println(isAnwserCorrect(questionActuelle));
			reponseQuestions.add(isAnwserCorrect(questionActuelle));
			try {
				showIcon(isAnwserCorrect(questionActuelle));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			idQuestion++;
			
			if(idQuestion+1 <= listQuestions.size()) {
				numeroQuestion.setText(""+(idQuestion+1));
				questionActuelle = listQuestions.get(idQuestion);
				showQuestion(questionActuelle);
				selectedZone.clear();
				removeZones();
				showZones();
				updateProgression();
			}else {
				System.out.println("Thème terminé !");
				finPartieController.listQuestions = listQuestions;
				finPartieController.reponseQuestions = reponseQuestions;
				goToFin();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void pop( ) throws IOException {
	    String[] Quitter = {"Revenir au jeu", "Quitter"};
	    JOptionPane jop = new JOptionPane();
	    jop.setBounds(50, 50, 200, 200);
	    int rang = JOptionPane.showOptionDialog(null, "Etes-vous sï¿½r de vouloir arrï¿½ter de jouer, si vous quitter la \n partie, la progression de votre partie sera effacï¿½ et  \n vous serez redirigï¿½ vers l'acceuil. ",
	      "QUITTEZ LA PARTIE : ",
	      JOptionPane.YES_NO_OPTION,
	      JOptionPane.QUESTION_MESSAGE,
	      null,
	      Quitter,
	      Quitter[1]);
	    if(rang == 0){}
	    if(rang==1){
	    	goToAccueil();
	    }
	}
	
	public void updateProgression() {
		//Progress bar
		double progress = (idQuestion)*avancement;
		progression.setProgress(progress);
		
		//Progress text
		pourcentage.setText((int) (progression.getProgress() * 100) + " % effectués");
		
		//Score text
		int nbTrueQuestions = 0;
		for(Boolean b : reponseQuestions) {
			if(b == true) nbTrueQuestions++;
		}
		score.setText(nbTrueQuestions + " / " + nbQuestion);
	}
	
	public void showIcon(boolean success) throws InterruptedException {
		ImageView icon;
		Media media;
		double width = image.getPrefWidth()/2;
		double height = image.getPrefHeight()/2;

		if(success) { 
			icon = new ImageView(new Image("File:./src/application/data/pass.png",width,height,false,true));
			media = new Media(new File("src/application/data/pass.mp3").toURI().toString());
		}
		else { 
			icon = new ImageView(new Image("File:./src/application/data/fail.png",width,height,false,true)); 
			media = new Media(new File("src/application/data/fail.mp3").toURI().toString());
		}
		
		icon.setX(width/2);
		icon.setY(height/2);
		MediaPlayer player = new MediaPlayer(media);
		System.out.println(player.getMedia().getSource());
		player.play();
		image.getChildren().add(icon);
		image.getChildren().remove(icon);
	}

	@FXML
	public void goToFin() throws IOException {
		VBox root = new VBox();
		finPartieController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("finDePartie.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
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
