package application.view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class parametresController {
	
	public static Stage primaryStage;
	
	//Gestion volume
	@FXML
	Slider slider;
	@FXML
	Label currentVolume;
	double volume;
	
	@FXML
	VBox vbox;
	
	@FXML
	HBox hbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML 
	public void goToAccueil() throws IOException {
		VBox root = new VBox();
		accueilController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Jeu - Accueil.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	public void updateVolume() {
		volume = slider.getValue();
		currentVolume.setText(Math.round(volume)+"%");
	}
}
