package application.database;

import java.sql.*;

public class Connect {

	Statement stt;
	PreparedStatement pst;
	Connection con;

	public Connect(String chemin, String nom, String mdp) throws SQLException {
		con = DriverManager.getConnection("jdbc:h2:" + chemin, nom, mdp);
		stt = con.createStatement();
	}

	public ResultSet executeCmd(String requete) throws SQLException {
		pst = con.prepareStatement(requete);
		ResultSet res = pst.executeQuery();
		return res;
	}
	
	public boolean executeUpdate(String requete) throws SQLException {
		stt = con.createStatement();
		boolean res = stt.execute(requete);
		return res;
	}

	public void Affichage(ResultSet res, String nom) throws SQLException {
		while (res.next()) {
			System.out.println(res.getString(nom));
		}

	}

	public void Disconnect() throws SQLException {
		con.close();

	}

}