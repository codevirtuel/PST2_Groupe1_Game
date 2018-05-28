package application.view;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class selectionThemeController {

	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView listeTheme;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		checkDirectory();
		checkThemeFolder("test");
		try {
			populateThemeList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkDirectory() {
		//Check if themes directory exists, if not create
		File themes = new File("themes");
		if(!themes.exists()) {
			themes.mkdir();
			System.out.println("Theme directory was missing, create new one");
		}	
	}
	
	public boolean checkThemeFolder(String name) {
		String PATH = "themes/"+name;
		File theme = new File(PATH);
		
		if(!theme.exists()) {
			System.out.println("The theme with name : "+name+" doesn't exists"); 
			return false;
		}
			
		//Check if the theme as a background image
		File background = new File(PATH+"/background.png");
		
		if(!background.exists()) {
			System.out.println("The theme with name : "+name+" doesn't have a background image"); 
			return false;
		}
		
		//Check if the theme as a database
		File database = new File(PATH+"/database.db");
		
		if(!database.exists()) {
			System.out.println("The theme with name : "+name+" doesn't have a database"); 
			return false;
		}
		
		return true;
	}
	
	public List<String> getThemeList() throws SQLException{
		List<String> themes = new ArrayList<String>();
		
		File directory = new File("themes");
		for(File theme : directory.listFiles()) {
			
			//Connect to the database to request theme name
			Connect db = new Connect(theme.getAbsolutePath()+"\\database","root","root");
			ResultSet result = db.executeCmd("SELECT * FROM THEME");
			while(result.next()) {
				themes.add(result.getString("NOM_THEME"));
			}
			db.Disconnect();
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
	
}
