package application.view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class accueilController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML 
	public void goToSettings() throws IOException {
		VBox root = new VBox();
		parametresController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("parametres.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	public void quitter() {
		primaryStage.close();
	}
}
