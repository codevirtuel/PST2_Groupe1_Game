package application.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.gestionThemes.Question;
import application.gestionThemes.Theme;
import application.gestionThemes.Zone;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;
	
	private Theme theme = new Theme(nomTheme);
	
	@FXML VBox vbox;
	
	@FXML ImageView background;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		try {
			loadTheme();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
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
			String URL = "./src/application/data/";
			if(result.getString("URL_IMAGE") != null) {
				URL += result.getString("URL_IMAGE");
			}else {
				URL += "480x270.png";
			}
			theme.setImageFond(new Image("./src/application/data/"+result.getString(URL)));
		}
		
		
	}
	
}
