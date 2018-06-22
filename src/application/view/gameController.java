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
import java.util.Random;

import javax.swing.JOptionPane;

import org.ini4j.Ini;

import com.sun.javafx.geom.Point2D;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	public static int scoreActuel = 0;
	private int idQuestion = 0;
	public static int tempsTotal = 0;

	private int timeBeforeSwitch = 3; // Temps d'attente à la fin d'une partie
										// en seconde
	private Timeline chrono = new Timeline(new KeyFrame(Duration.seconds(1), event -> chrono()));

	// preload music
	private Media MUSIC_FAIL = new Media(new File("src/application/data/musiques/fail.mp3").toURI().toString());
	private Media MUSIC_PASS = new Media(new File("src/application/data/musiques/pass.mp3").toURI().toString());
	private Media BACKGROUND_MUSIC;

	MediaPlayer player;

	@FXML
	BorderPane vbox;

	private final int TEMPS_MAX = 15;
	public int s = TEMPS_MAX;
	boolean Endgame = false;

	@FXML
	Label chronometre;

	@FXML
	ImageView background;

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

	@FXML
	Button btValider;
	@FXML
	Button btQuitter;

	// ************ FXML Functions ************

	@FXML
	public void initialize() {
		Random rnd = new Random();
		double randomNbr = rnd.nextInt(100);
		System.out.println(randomNbr);

		if (randomNbr >= 0 && randomNbr <= 90)
			BACKGROUND_MUSIC = new Media(
					new File("src/application/data/musiques/Backbay Lounge.mp3").toURI().toString());

		if (randomNbr > 90 && randomNbr <= 100)
			BACKGROUND_MUSIC = new Media(new File("src/application/data/musiques/flamingo.mp3").toURI().toString());

		tempsTotal = 0;
		Scaler.updateSize(Main.width, vbox);
		try {
			loadTheme();
			chrono();
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
		numeroQuestion.setText("" + (idQuestion + 1));

		player = new MediaPlayer(BACKGROUND_MUSIC);
		Ini saveFile = null;
		try {
			saveFile = new Ini(new File("options.ini"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.setVolume(saveFile.get("other", "volume", double.class) / 100);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();

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
		hoverMouse(e);
		updateZones();
	}

	@FXML
	public void hoverMouse(MouseEvent e) {
		String index = e.getPickResult().getIntersectedNode().getId();
		if (!index.equals("image")) {
			Zone correspondingZone = theme.getZoneWithID(Integer.valueOf(index));
			if (selectedZone.contains(correspondingZone)) {
				correspondingZone.setOnMouseEntered(e2 -> correspondingZone.setFill(Color.LIGHTGREEN));
				correspondingZone.setOnMouseExited(e2 -> correspondingZone.setFill(Color.GREEN));
			} else {
				correspondingZone.setOnMouseEntered(e2 -> correspondingZone.setFill(Color.BLACK));
				correspondingZone.setOnMouseExited(e2 -> correspondingZone.setFill(Color.WHITE));
			}
		}
	}

	@FXML
	public void valider() throws IOException, InterruptedException {
		try {
			boolean correct = isAnwserCorrect(questionActuelle);
			System.out.println("Correct ? : " + correct);
			reponseQuestions.add(correct);

			idQuestion++;

			tempsTotal += TEMPS_MAX - s;

			if (idQuestion + 1 <= listQuestions.size()) {
				numeroQuestion.setText("" + (idQuestion + 1));
				questionActuelle = listQuestions.get(idQuestion);
				showQuestion(questionActuelle);
				selectedZone.clear();
				removeZones();
				showZones();
				showIcon(correct);
				updateProgression();

				// Reset chrono
				s = TEMPS_MAX;
				chrono.stop();
				chrono();
			} else {
				updateProgression();
				showIcon(correct);
				finPartieController.nomTheme = theme;
				finPartieController.listQuestions = listQuestions;
				finPartieController.reponseQuestions = reponseQuestions;
				finPartieController.tempsTotal = tempsTotal;
				finPartieController.score = scoreActuel;
				System.out.println("Thï¿½me terminï¿½ !");
				System.out.println("Temps total : " + tempsTotal);
				System.out.println("Score total : " + scoreActuel);
				Endgame = true;
				chrono.stop();

				btValider.setDisable(true);
				btQuitter.setDisable(true);

				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(timeBeforeSwitch)));
				timeline.play();

				timeline.setOnFinished(e -> {
					try {
						goToFin();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ************ Functions ************

	public void pop() throws IOException {
		String[] Quitter = { "Revenir au jeu", "Quitter" };
		JOptionPane jop = new JOptionPane();
		jop.setBounds(50, 50, 200, 200);
		int rang = JOptionPane.showOptionDialog(null,
				"Etes-vous sûr de vouloir arrêter de jouer, si vous quitter la \n partie, la progression de votre partie sera effacé et  \n vous serez redirigé vers l'acceuil. ",
				"QUITTEZ LA PARTIE : ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Quitter,
				Quitter[1]);
		if (rang == 0) {
		}
		if (rang == 1) {
			System.out.println("Vous avez quitter la partie --> Retour au menu");
			chrono.stop();
			goToAccueil();
		}
	}

	Timeline tl = null;

	public void chrono() {
		if (s > 0) {
			chronometre.setText(s + " secs");
			s--;

			if (Endgame != true) {
				chrono = new Timeline(new KeyFrame(Duration.seconds(1), event -> chrono()));
				chrono.play();
			}

		} else {
			try {
				valider();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void showIcon(boolean success) throws InterruptedException {
		ImageView icon = null;
		Image img;
		Media media;
		double width = image.getPrefWidth() / 2;
		double height = image.getPrefHeight() / 2;

		if (success) {
			img = new Image("File:./src/application/data/pass.png", width, height, true, true);
			media = MUSIC_PASS;
		} else {
			img = new Image("File:./src/application/data/fail.png", width, height, true, true);
			media = MUSIC_FAIL;
		}

		MediaPlayer player = new MediaPlayer(media);
		Ini saveFile = null;
		try {
			saveFile = new Ini(new File("options.ini"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.setVolume(saveFile.get("other", "volume", double.class) / 100);
		icon = new ImageView(img);

		double positionX = width - img.getWidth() / 2;
		double positionY = height - img.getHeight() / 2;

		System.out.println(image.getPrefWidth() + " " + image.getPrefHeight());

		icon.setX(positionX);
		icon.setY(positionY);

		player.play();

		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(icon.imageProperty(), img)),
				new KeyFrame(Duration.seconds(1), new KeyValue(icon.imageProperty(), null)));

		timeline.play();
		image.getChildren().add(icon);

	}

	// -- gestion zone --

	public void showZones() {
		for (Zone z : theme.getZones()) {
			z.setId("" + z.getIndex());

			if (selectedZone.contains(z))
				z.setFill(Color.GREEN);
			else
				z.setFill(Color.WHITE);

			z.setOpacity(0.3);
			z.setStroke(Color.BLACK);
			z.setStrokeWidth(2);
			image.getChildren().add(z);
		}
	}

	public void removeZones() {
		for (Zone z : theme.getZones()) {
			image.getChildren().remove(z);
		}
	}

	// -- gestion questions --
	public void showQuestion(Question question) {
		// intitulï¿½
		intituleQuestion.setText(question.getIntitule());
	}

	public boolean isAnwserCorrect(Question question) throws SQLException {
		boolean result = true;

		if (selectedZone.size() == 0)
			result = false;

		for (Zone z : question.getReponses()) {
			if (!selectedZone.contains(z)) {
				result = false;
			}
		}

		for (Zone z : selectedZone) {
			if (!question.getReponses().contains(z)) {
				result = false;
			}
		}
		return result;
	}

	// Charge le thï¿½me via la classe Thï¿½me
	public void loadTheme() throws SQLException {
		// Load background
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM THEME WHERE NOM_THEME=" + "'" + nomTheme + "'");
		while (result.next()) {
			String URL = "File:./src/application/data/";
			File image = new File("src/application/data/" + result.getString("URL_IMAGE"));
			System.out.println(image.exists());
			if (result.getString("URL_IMAGE").equals("null") || !image.exists()) {
				URL += "480x270.png";
			} else {
				URL += result.getString("URL_IMAGE");
			}

			theme.setImageFond(new Image(URL));
		}

		// Load zones

		double factor = Main.width / 1280.0;
		System.out.println("Factor : " + factor);

		result = Main.bdd.executeCmd("SELECT * FROM ZONE WHERE NOM_THEME=" + "'" + nomTheme + "'");
		while (result.next()) {
			int idZone = result.getInt("ID_ZONE");

			// Query points
			List<Double> points = new ArrayList<Double>();

			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM POINT WHERE ID_ZONE=" + idZone);
			while (result2.next()) {
				points.add(result2.getDouble("POS_X") * factor);
				points.add(result2.getDouble("POS_Y") * factor);
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
		if (theme.getImageFond().getWidth() * rapport > theme.getImageFond().getHeight()) {
			height = width * theme.getImageFond().getHeight() / theme.getImageFond().getWidth();
			image.setPrefHeight(height);
		} else {
			width = height * theme.getImageFond().getWidth() / theme.getImageFond().getHeight();
			image.setPrefWidth(width);
		}

		BackgroundImage bgImage = new BackgroundImage(theme.getImageFond(), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(width, height, false, false, false, true));
		image.setBackground(new Background(bgImage));
	}

	public void updateProgression() {
		// Progress bar
		double progress = (idQuestion) * avancement;
		progression.setProgress(progress);

		// Progress text
		pourcentage.setText((int) (progression.getProgress() * 100) + " % effectués");

		// Score text
		int nbTrueQuestions = 0;
		for (Boolean b : reponseQuestions) {
			if (b == true)
				nbTrueQuestions++;
		}
		scoreActuel = nbTrueQuestions;
		score.setText(nbTrueQuestions + " / " + nbQuestion);
	}

	public void updateZones() {
		removeZones();
		showZones();
	}

	// ************ Switch stage ************

	@FXML
	public void goToFin() throws IOException {
		player.stop();
		VBox root = new VBox();
		root = FXMLLoader.load(getClass().getResource("finDePartie.fxml"));
		Scene scene = new Scene(root, Main.width, Main.height);

		finPartieController.primaryStage = primaryStage;

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	public void goToAccueil() throws IOException {
		player.stop();
		VBox root = new VBox();
		accueilController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Jeu - Accueil.fxml"));
		Scene scene = new Scene(root, Main.width, Main.height);

		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
