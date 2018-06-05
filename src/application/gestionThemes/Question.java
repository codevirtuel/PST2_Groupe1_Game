package application.gestionThemes;

import java.util.ArrayList;
import java.util.List;

public class Question {

	private List<Zone> reponses;
	private String intitule;
	
	public Question(String intitule, List<Zone> zones) {
		//A changer si necessaire
		//reponses = new ArrayList<Zone>();
		reponses = zones;
		this.intitule = intitule;
	}

	public Question(String intitule) {
		reponses = new ArrayList<Zone>();
		this.intitule = intitule;
	}

	public List<Zone> getReponses() {
		return reponses;
	}
	public void setReponses(List<Zone> reponses) {
		this.reponses = reponses;
	}
	public String getIntitule() {
		return intitule;
	}
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
	
}
