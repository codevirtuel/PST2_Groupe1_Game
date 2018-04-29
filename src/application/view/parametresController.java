package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class parametresController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	HBox hbox;
	
	@FXML
	public void initialize() {
		hbox.setSpacing(hbox.getSpacing());
		Scaler.updateSize(Main.width,vbox);
	}
}
