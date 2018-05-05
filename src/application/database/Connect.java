package application.database;

import java.sql.*;

public class Connect {
	
	Statement stt;
	PreparedStatement pst;
	Connection con;
	
	public Connect(String chemin, String nom, String mdp) throws SQLException {
		con = DriverManager.getConnection("jdbc:h2:"+chemin,nom,mdp);
		stt = con.createStatement();
	}
	
	public ResultSet executeCmd(String requete) throws SQLException {
		pst = con.prepareStatement(requete);
        ResultSet res = pst.executeQuery();
        System.out.println(res);
        System.out.println(res);
        return res;
	}

}