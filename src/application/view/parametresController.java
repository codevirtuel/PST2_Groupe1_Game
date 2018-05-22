package application.view;

import java.io.IOException;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
	
	//Gestion r�solutions
	@FXML
	ComboBox resolutions;
	
	@FXML
	VBox vbox;
	
	@FXML
	HBox hbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		populateResolutions();
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
	
	@FXML
	public void populateResolutions() {
		ObservableList<String> liste = FXCollections.observableArrayList
				("640 x 360",
				"1280 x 720",
				"1920 x 1080");
		resolutions.setItems(liste);
		
		//D�fini la r�solution par d�faut en fonction de l'actuelle
		switch(Main.height) {
		case 360:
			resolutions.getSelectionModel().select(0); break;
		case 720:
			resolutions.getSelectionModel().select(1); break;
		case 1080:
			resolutions.getSelectionModel().select(2); break;
		}
		
	}
}
