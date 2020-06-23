package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.CountryPair;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.YearAndSightings;

public class SightingsDAO {
	
	public List<String> getStates(Integer year) {
		
		String sql = "SELECT DISTINCT state " + 
				"FROM sighting " + 
				"WHERE country = 'us' AND YEAR(`datetime`) = ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("state")) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<CountryPair> getCountryPairs(Integer year) {
		String sql = "SELECT DISTINCT s1.state, s2.state " + 
				"FROM sighting AS s1, sighting AS s2 " + 
				"WHERE s1.country = 'us' AND s2.country = 'us' " + 
				"AND YEAR(s1.`datetime`) = ? AND YEAR(s2.`datetime`) = ? " + 
				"AND s1.`datetime` < s2.`datetime` AND s1.state <> s2.state" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<CountryPair> list = new ArrayList<>() ;
			
			st.setInt(1, year);
			
			st.setInt(2, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new CountryPair(res.getString("s1.state"), res.getString("s2.state"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting WHERE country = 'us' " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<YearAndSightings> getSightingsForYear() {
		String sql = "SELECT DISTINCT YEAR(`datetime`) AS `year`, COUNT(*) AS sightings " + 
				"FROM sighting " + 
				"WHERE country = 'us' " + 
				"GROUP BY `year` " + 
				"ORDER BY `year`DESC " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<YearAndSightings> result = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new YearAndSightings(res.getInt("year"), res.getInt("sightings")));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

}
