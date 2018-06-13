package application.view;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;
	public int s = 20;

	private Theme theme = new Theme(nomTheme);
	boolean Endgame = false;

	@FXML
	Label chronometre;

	@FXML
	VBox vbox;

	@FXML
	ImageView background;

	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width, vbox);
		try {
			loadTheme();
			chrono();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void chrono() {
		if (s != 0) {
			s--;
			chronometre.setText(s + " secs");
			if (Endgame == false) {
				new Timeline(new KeyFrame(Duration.seconds(1), event -> chrono())).play();
			}
		}
		if (s == 0) {
			// questionSuivante();
		}

	}

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
		background.setImage(theme.getImageFond());

	}

	@FXML
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
			goToAccueil();
		}
	}

	public void goToAccueil() throws IOException {
		VBox root = new VBox();
		accueilController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Jeu - Accueil.fxml"));
		Scene scene = new Scene(root, Main.width, Main.height);

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
