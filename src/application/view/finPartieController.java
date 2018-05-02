package application.view;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class finPartieController {
	
	public static Stage primaryStage;
	
	@FXML
	VBox vbox;
	
	@FXML
	ListView list;
	
	@FXML
	AnchorPane anchor;
	
	@FXML
	public void initialize() {
		Scaler.updateSize(Main.width,vbox);
	}
}
