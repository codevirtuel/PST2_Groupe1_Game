package application.gestionThemes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Score {
	private final SimpleStringProperty placement;
    private final SimpleStringProperty nomJoueur;
    private final SimpleStringProperty score;
    
	public Score(int placement, String nomJoueur, int score) {
		this.placement = new SimpleStringProperty(String.valueOf(placement));
		this.nomJoueur = new SimpleStringProperty(nomJoueur);
		this.score = new SimpleStringProperty(String.valueOf(score));
	}

	public StringProperty placementProperty() {
		return placement;
	}
	
	public String getPlacement() {
        return placement.get();
    }
    public void setPlacement(String placement) {
        this.placement.set(placement);
    }
        
    
    public StringProperty joueurProperty() {
		return nomJoueur;
	}
    
    public String getNomJoueur() {
        return nomJoueur.get();
    }
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur.set(nomJoueur);
    }
    
    public StringProperty scoreProperty() {
		return score;
	}
    
    public String getScore() {
        return score.get();
    }
    public void setScore(String score) {
        this.score.set(score);
    }
    
    //toString
    public String toString() {
    	return "#"+placement+" "+score+" "+nomJoueur;
    }
}
