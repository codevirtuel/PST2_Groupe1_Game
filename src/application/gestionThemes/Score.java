package application.gestionThemes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Score {
	private final SimpleStringProperty placement;
    private final SimpleStringProperty nomJoueur;
    private final SimpleStringProperty score;
    private final SimpleStringProperty temps;
    
	public Score(int placement, String nomJoueur, int score, int temps) {
		this.placement = new SimpleStringProperty(String.valueOf(placement));
		this.nomJoueur = new SimpleStringProperty(nomJoueur);
		this.score = new SimpleStringProperty(String.valueOf(score));
		this.temps = new SimpleStringProperty(String.valueOf(temps));
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
    
    public StringProperty tempsProperty() {
		return temps;
	}
    public String getTemps() {
        return temps.get();
    }
    public void setTemps(String temps) {
        this.temps.set(temps);
    }
    
    //toString
    public String toString() {
    	return "#"+placement+" "+score+" "+nomJoueur;
    }
}
