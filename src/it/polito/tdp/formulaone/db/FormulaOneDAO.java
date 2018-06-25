package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.PilotiVittorie;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Driver> getPiloti(Season s) {
		String sql = "SELECT Distinct drivers.driverId, drivers.driverRef, drivers.number, drivers.code, drivers.forename, drivers.surname, drivers.dob, drivers.nationality, drivers.url " + 
					"FROM results, races, drivers " + 
					"WHERE position <> 'null' " + 
					"AND results.raceId = races.raceId " + 
					"AND races.year = ? " + 
					"AND results.driverId = drivers.driverId ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s.getYear().getValue());

			ResultSet rs = st.executeQuery();

			List<Driver> drivers = new ArrayList<>();
			while (rs.next()) {
				Date data = rs.getDate("drivers.dob") ;
				LocalDate ldata = null ;
				if (data!=null) {
					ldata = rs.getDate("drivers.dob").toLocalDate() ;
				}
				Driver d = new Driver(rs.getInt("drivers.driverId"), rs.getString("drivers.driverRef"), rs.getInt("drivers.number"), rs.getString("drivers.code"), rs.getString("drivers.forename"), rs.getString("drivers.surname"), ldata, rs.getString("drivers.nationality"), rs.getString("drivers.url")) ;
				drivers.add(d) ;
			}

			conn.close();
			return drivers;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<PilotiVittorie> getPilotiVittorie(Season s) {
//		String sql = "SELECT Distinct drivers.driverId, drivers.driverRef, drivers.number, drivers.code, drivers.forename, drivers.surname, drivers.dob, drivers.nationality, drivers.url " + 
//					"FROM results, races, drivers " + 
//					"WHERE position <> 'null' " + 
//					"AND results.raceId = races.raceId " + 
//					"AND races.year = ? " + 
//					"AND results.driverId = drivers.driverId ";
//	
//		try {//tutte le gare della stagione, vedere tutti i risultati, fare un from sui risultati,
//			//vincolo posizione minore, raggruppare per pilota, contare i records
//			
//			Connection conn = DBConnect.getConnection();
//	
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setInt(1, s.getYear().getValue());
//	
//			ResultSet rs = st.executeQuery();
//	
//			List<PilotiVittorie> pilotiVittorie = new ArrayList<>();
//			while (rs.next()) {
//				Date data = rs.getDate("drivers.dob") ;
//				LocalDate ldata = null ;
//				if (data!=null) {
//					ldata = rs.getDate("drivers.dob").toLocalDate() ;
//				}
//				Driver d = new Driver(rs.getInt("drivers.driverId"), rs.getString("drivers.driverRef"), rs.getInt("drivers.number"), rs.getString("drivers.code"), rs.getString("drivers.forename"), rs.getString("drivers.surname"), ldata, rs.getString("drivers.nationality"), rs.getString("drivers.url")) ;
//				drivers.add(d) ;
//			}
//	
//			conn.close();
//			return drivers;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException("SQL Query Error");
//		}
		return null ;
	}
	
}
