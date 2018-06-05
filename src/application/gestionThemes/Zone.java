package application.gestionThemes;

import java.util.List;

import javafx.scene.shape.Polygon;

public class Zone extends Polygon {
	
	private int index;
	
	public Zone(int i) {
		index=i;
	}
	
	public Zone(int i, List<Double> coordonnes) {
		//A changer si necessaire
		index=i;
		this.getPoints().addAll(coordonnes);
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		boolean paire = true;
		String points = "";
		for(int i=1; i<this.getPoints().size()/2; i++) {
			points+=getPointData(i);
		}
		return "("+index+") - "+getPointData(0)+points;
	}
	public String getPointData(int i) {
		return "("+this.getPoints().get(i*2)+" ; "+this.getPoints().get(i*2+1)+")";
	}

}
