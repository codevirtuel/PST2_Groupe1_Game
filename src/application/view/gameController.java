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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;

	private Theme theme = new Theme(nomTheme);

	private List<Zone> selectedZone = new ArrayList<Zone>();
	public int nbQuestion = theme.getQuestions().size();
	//public int nbQuestion = 5;
	private List<Question> listQuestions = new ArrayList<Question>();
	private List<Boolean> reponseQuestions = new ArrayList<Boolean>();
	private Question questionActuelle;
	double avancement = 1.0 / nbQuestion;
	int scoreActuel = 0;
	private int idQuestion = 0;

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
		progression.setProgress(0);
		progression.setStyle("-fx-accent: green;");
		pourcentage.setText((int) (progression.getProgress() * 100) + " % effectu�s");
		score.setText((scoreActuel) + " / " + nbQuestion);
	}

	//Charge le th�me via la classe Th�me
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
		// intitul�
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
			if (progression.getProgress() < 1) {
				progression.setProgress(progression.getProgress() + avancement);
				pourcentage.setText((int) (progression.getProgress() * 100) + " % effectu�s");
				scoreActuel += 1;
				score.setText((scoreActuel) + " / " + nbQuestion);
			}

			return true;
		} else
			return false;

	}

	@FXML

	public void valider() throws IOException {
		try {
			System.out.println(isAnwserCorrect(questionActuelle));
			reponseQuestions.add(isAnwserCorrect(questionActuelle));
			idQuestion++;
			System.out.println("Next id : "+idQuestion);
			if(idQuestion+1 <= listQuestions.size()) {
				questionActuelle = listQuestions.get(idQuestion);
				showQuestion(questionActuelle);
				selectedZone.clear();
				removeZones();
				showZones();
			}else {
				System.out.println("Th�me termin� !");
				finPartieController.listQuestions = listQuestions;
				finPartieController.reponseQuestions = reponseQuestions;
				goToFin();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	public void pop( ) throws IOException {
	    String[] Quitter = {"Revenir au jeu", "Quitter"};
	    JOptionPane jop = new JOptionPane();
	    jop.setBounds(50, 50, 200, 200);
	    int rang = JOptionPane.showOptionDialog(null, "Etes-vous s�r de vouloir arr�ter de jouer, si vous quitter la \n partie, la progression de votre partie sera effac� et  \n vous serez redirig� vers l'acceuil. ",
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


}
