package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class gameController {

	public static Stage primaryStage;
	
	@FXML VBox vbox;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
		
	}
	
}
