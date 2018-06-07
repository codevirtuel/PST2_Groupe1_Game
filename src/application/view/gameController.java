package application.view;

import java.awt.Paint;
import java.io.File;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	public static String nomTheme;
	
	private Theme theme = new Theme(nomTheme);
	
	private List<Zone> selectedZone = new ArrayList<Zone>();
	
	@FXML VBox vbox;
	
	@FXML AnchorPane image;
	
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
	
	//Charge le thème via la classe Thème
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
		double factor = Scaler.getFactor();
		int imageWidth = (int) (480*factor);
		int imageHeight = (int) (270*factor);
		
		Image newImage = scale(theme.getImageFond(),imageWidth,imageHeight,false);
		
		BackgroundImage bgImage = new BackgroundImage(newImage,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(480, 270, false, false, false, true));
		image.setBackground(new Background(bgImage));
	}
	
	public Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(preserveRatio);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(null, null);
	}
	
	public void showZones() {
		for(Zone z : theme.getZones()) {
			double factor = Scaler.getFactor();
			for(int i=0;i<z.getPoints().size();i++) {
				z.getPoints().set(i, z.getPoints().get(i));
			}
			z.setId(""+z.getIndex());
			z.setOpacity(2.0);
			z.setFill(Color.RED);
			z.setStroke(Color.BLACK);
			z.setStrokeWidth(1);
			image.getChildren().add(z);
		}
		System.out.println(image.getChildren());
	}
	
	@FXML
	public void clickOnZone(MouseEvent e) {
		for(Zone z : selectedZone) {
			System.out.println(z);
		}
		String index = e.getPickResult().getIntersectedNode().getId();
		if(!index.equals("image")) {
			System.out.println("Polygon detected!");
			Zone correspondingZone = theme.getZoneWithID(Integer.valueOf(index));
			if(selectedZone.contains(correspondingZone))
				selectedZone.remove(correspondingZone);
			else
				selectedZone.add(correspondingZone);
		}
	}
	
}
