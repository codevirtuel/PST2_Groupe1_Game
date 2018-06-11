package application;

import java.io.File;
import java.sql.SQLException;

import org.ini4j.Ini;

import application.database.Connect;
import application.view.accueilController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static Connect bdd;
	
	public static int width = 1280;
	public static int height = 720;

	@Override
	public void start(Stage primaryStage) {

		try {
			VBox root = new VBox();
			
			//Check if config file is here
			File saveFile = new File("options.ini");
			if(saveFile.exists()) {
				Ini ini = new Ini(saveFile);
				width = ini.get("resolution","width",int.class);
				height = ini.get("resolution","height",int.class);
			}
			
			accueilController.primaryStage = primaryStage;
			root = FXMLLoader.load(getClass().getResource("view/Jeu - Accueil.fxml"));
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			
			primaryStage.centerOnScreen();
			primaryStage.show();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			bdd = new Connect("./src/application/database/database","root", "root");
			System.out.println("Base de données connecté !");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		launch(args);
	}

}
