package application.view;

import java.io.IOException;
import java.util.Optional;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class accueilController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		System.out.println("Window size factor : "+Scaler.getFactor());
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
	public void goToThemeSelection() throws IOException {
		VBox root = new VBox();
		selectionThemeController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Selection - Theme.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML 
	public void goToCredits() throws IOException {
		VBox root = new VBox();
		creditController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Credit.fxml"));
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
