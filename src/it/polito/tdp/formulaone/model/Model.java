package it.polito.tdp.formulaone.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	FormulaOneDAO dao ;
	SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo ;
	
	

	public Model() {
		this.dao = new FormulaOneDAO() ;
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class) ;
	}



	public List<Season> getAllSeasons() {
		return dao.getAllSeasons();
	}



	public void creaGrafo(Season s) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class) ;
		
		//aggiungi i vertici
		List<Driver> drivers = dao.getPiloti(s) ;
		Graphs.addAllVertices(grafo, drivers) ;		
		
		//aggiungi gli archi
		List<PilotiVittorie> pvs = dao.getPilotiVittorie(s) ;
		for (PilotiVittorie pv : pvs) {
			Graphs.addEdge(grafo, pv.getD1(), pv.getD2(), pv.getVittorie()) ;
		}
	}



	public DriverPunteggio getPilotaMigliore() {
		// TODO Auto-generated method stub
		return null;
	}


}
