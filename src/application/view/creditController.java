package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.ini4j.Ini;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class creditController {
public static Stage primaryStage;
	
	private KeyCode[] konamiCode = {KeyCode.UP,KeyCode.UP,KeyCode.DOWN,KeyCode.DOWN,KeyCode.LEFT,KeyCode.RIGHT,KeyCode.LEFT,KeyCode.RIGHT,KeyCode.B,KeyCode.A};
	private int nbrCompleted = 0;
	private boolean codeEnabled = false;
	Media suca = new Media(new File("src/application/data/soviet.mp3").toURI().toString());
	MediaPlayer player;
	Font font;
	
	@FXML
	VBox vbox;
	
	@FXML
	Label test;
	
	@FXML
	public void initialize() throws FileNotFoundException {
		
		InputStream inputstream = new FileInputStream(new File("src/application/Russian Dollmaker.ttf").getAbsolutePath());
		System.out.println(inputstream);
		font = Font.loadFont(inputstream, 50);
		Scaler.updateSize(Main.width,vbox);
	}
	
	@FXML 
	public void goToAccueil() throws IOException {
		if(codeEnabled){
			player.stop();
		}
		
		VBox root = new VBox();
		accueilController.primaryStage = primaryStage;
		root = FXMLLoader.load(getClass().getResource("Jeu - Accueil.fxml"));
		Scene scene = new Scene(root,Main.width,Main.height);
		
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	public void konami(KeyEvent e) {
		if(!codeEnabled) {
			KeyCode newCode = konamiCode[nbrCompleted];
			if(e.getCode() == newCode) {
				nbrCompleted++;
			}else {
				nbrCompleted = 0;
			}
			
			if(nbrCompleted == konamiCode.length) {
				codeEnabled = true;
				System.out.println("KONAMI CODE ENABLE !");
				enableRussianMode();
			}
		}
	}
	
	public void enableRussianMode() {
		//play music
		player = new MediaPlayer(suca);
		
		Ini saveFile = null;
		try {
			saveFile = new Ini(new File("options.ini"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.setVolume(saveFile.get("other","volume",double.class)/100);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
		
		//Load font
		System.out.println(font.getFamily());
		test.setFont(font);
		
	}
}
