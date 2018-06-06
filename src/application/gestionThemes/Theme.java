package application.gestionThemes;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Theme {
	
	private List<Question> questions;
	private List<Zone> zones;
	private String nom;
	private Image imageFond;
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Theme(String nom) {
		//A changer si necessaire
		questions = new ArrayList<Question>();
		zones = new ArrayList<Zone>();
		this.nom = nom;
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	public void addZone(Zone zone) {
		zones.add(zone);
	}
	
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public List<Zone> getZones() {
		return zones;
	}
	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
	public List<Zone> getZonesWithIDs(List<Integer> liste) {
		List<Zone> zones = new ArrayList<Zone> ();
		for(Integer i : liste) {
			Zone zone = getZoneWithID(i.intValue());
			if(zone!=null)
				zones.add(zone);
		}
		return zones;
	}

	public Zone getZoneWithID(int id) {
		for(Zone zone : zones)
			if(zone.getIndex()==id)
				return zone;
		return null;
	}

	public void setImageFond(Image img) {
		imageFond=img;
	}
	public Image getImageFond() {
		return imageFond;
	}

}
