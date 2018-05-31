package application.gestionThemes;

import javafx.beans.property.SimpleStringProperty;

public class Score {
	private final SimpleStringProperty placement;
    private final SimpleStringProperty nomJoueur;
    private final SimpleStringProperty score;
    
	public Score(String placement, String nomJoueur, String score) {
		this.placement = new SimpleStringProperty(placement);
		this.nomJoueur = new SimpleStringProperty(nomJoueur);
		this.score = new SimpleStringProperty(score);
	}
	
	public String getPlacement() {
        return placement.get();
    }
    public void setPlacement(String placement) {
        this.placement.set(placement);
    }
        
    public String getNomJoueur() {
        return nomJoueur.get();
    }
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur.set(nomJoueur);
    }
    
    public String getScore() {
        return score.get();
    }
    public void setScore(String score) {
        this.score.set(score);
    }
}
