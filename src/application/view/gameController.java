package application.view;

import java.awt.Paint;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;

	private Theme theme = new Theme(nomTheme);
	private List<Zone> reponses;
	private Question questionEnCours;

	@FXML VBox vbox;

<<<<<<< HEAD
	@FXML AnchorPane background;
=======
	@FXML AnchorPane image;
>>>>>>> Alexis_Poupelin

	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		try {
			loadTheme();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		showZones();
	}

	//Charge le th�me via la classe Th�me
	public void loadTheme() throws SQLException {
		//Load zones
		ResultSet result = Main.bdd.executeCmd("SELECT * FROM ZONE WHERE NOM_THEME="+"'"+nomTheme+"'");
		while(result.next()) {
			int idZone = result.getInt("ID_ZONE");

			//Query points
			List<Double> points = new ArrayList<Double>();
			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM POINT WHERE ID_ZONE="+idZone);
			while(result2.next()) {
				points.add(result2.getDouble("POS_X"));
				points.add(result2.getDouble("POS_Y"));
			}
			theme.addZone(new Zone(idZone,points));
		}

		//Load questions
		result = Main.bdd.executeCmd("SELECT * FROM QUESTION WHERE NOM_THEME="+"'"+nomTheme+"'");

		while(result.next()) {
			int questionId = result.getInt("ID_QUESTION");
			String questionIntitule = result.getString("INTITULE_QUESTION");
			List<Zone> questionZone = new ArrayList<Zone>();

			ResultSet result2 = Main.bdd.executeCmd("SELECT * FROM REPONSE WHERE ID_QUESTION="+questionId);
			while(result2.next()) {
				if(theme.getZoneWithID(result2.getInt("ID_ZONE")) != null) {
					questionZone.add(theme.getZoneWithID(result2.getInt("ID_ZONE")));
				}
			}

			theme.addQuestion(new Question(questionIntitule,questionZone));
		}

		//Load background
		result = Main.bdd.executeCmd("SELECT * FROM THEME WHERE NOM_THEME="+"'"+nomTheme+"'");
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
<<<<<<< HEAD
		//background.setImage(theme.getImageFond());

=======
		BackgroundImage bgImage = new BackgroundImage(theme.getImageFond(),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(480, 270, false, false, false, true));
		image.setBackground(new Background(bgImage));
	}

	public void showZones() {
		for(Zone z : theme.getZones()) {
			Polygon poly = new Polygon();
			poly.getPoints().setAll(z.getPoints());
			image.getChildren().add(poly);
		}
		System.out.println(image.getChildren());
>>>>>>> Alexis_Poupelin
	}

	public void valider() {
		for(Zone zone : reponses)
			if(!questionEnCours.getReponses().contains(zone)) {
				zone.setStroke(Color.RED);
			}
		for(Zone zone : questionEnCours.getReponses())
			zone.setStroke(Color.GREEN);
	}
}
