package application.view;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
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
	private Question questionActuelle;
	double avancement = 1.0 / nbQuestion;
	int scoreActuel = 0;

	@FXML
	VBox vbox;

	@FXML
	AnchorPane image;

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
		questionActuelle = listQuestions.get(0);
		showQuestion(questionActuelle);
		progression.setProgress(0);
		progression.setStyle("-fx-accent: green;");
		pourcentage.setText((int) (progression.getProgress() * 100) + " % effectués");
		score.setText((scoreActuel) + " / " + nbQuestion);
	}

	// Charge le thème via la classe Thème
	public void loadTheme() throws SQLException {
		// Load zones
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM ZONE WHERE NOM_THEME=" + "'" + nomTheme + "'");
		while (result.next()) {
			int idZone = result.getInt("ID_ZONE");

			// Query points
			List<Double> points = new ArrayList<Double>();
			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM POINT WHERE ID_ZONE=" + idZone);
			while (result2.next()) {
				points.add(result2.getDouble("POS_X"));
				points.add(result2.getDouble("POS_Y"));
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

		// Load background
		result = Main.bdd.executeCmd("SELECT * FROM THEME WHERE NOM_THEME=" + "'" + nomTheme + "'");
		while (result.next()) {
			String URL = "File:./src/application/data/";
			File image = new File(URL + result.getString("URL_IMAGE"));
			if (result.getString("URL_IMAGE") == null) {
				URL += "480x270.png";
			} else {
				URL += result.getString("URL_IMAGE");
			}

			theme.setImageFond(new Image(URL));
		}
		double factor = Scaler.getFactor();
		int imageWidth = (int) (480 * factor);
		int imageHeight = (int) (270 * factor);

		Image newImage = scale(theme.getImageFond(), imageWidth, imageHeight, false);

		BackgroundImage bgImage = new BackgroundImage(newImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, new BackgroundSize(480, 270, false, false, false, true));
		image.setBackground(new Background(bgImage));
	}

	public Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
		ImageView imageView = new ImageView(source);
		imageView.setPreserveRatio(preserveRatio);
		imageView.setFitWidth(targetWidth);
		imageView.setFitHeight(targetHeight);
		return imageView.snapshot(null, null);
	}

	// -- gestion zone --

	public void showZones() {
		for (Zone z : theme.getZones()) {
			double factor = Scaler.getFactor();
			for (int i = 0; i < z.getPoints().size(); i++) {
				z.getPoints().set(i, z.getPoints().get(i));
			}
			z.setId("" + z.getIndex());
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
		// intitulé
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
				pourcentage.setText((int) (progression.getProgress() * 100) + " % effectués");
				scoreActuel += 1;
				score.setText((scoreActuel) + " / " + nbQuestion);
			}

			return true;
		} else
			return false;

	}

	@FXML
	public void valider() {
		try {
			System.out.println(isAnwserCorrect(questionActuelle));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
