package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	FormulaOneDAO dao ;
	SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo ;
	private List<Driver> ottimo;
	private boolean primo;

	public Model() {
		this.dao = new FormulaOneDAO() ;
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class) ;
		this.ottimo = new ArrayList<>() ;
		this.primo = true ;
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
		int max = 0 ;
		Driver pilota = null ;
		for (Driver d : grafo.vertexSet()) {
			int dentro = 0 ;
			int fuori = 0 ;
			
			for(DefaultWeightedEdge dwe : grafo.incomingEdgesOf(d)) {
				dentro = dentro + (int)grafo.getEdgeWeight(dwe) ; 
			}
			
			for(DefaultWeightedEdge dwe : grafo.outgoingEdgesOf(d)) {
				fuori = fuori + (int)grafo.getEdgeWeight(dwe) ; 
			}
			
			int punteggio = fuori-dentro ;
			if (punteggio>max) {
				max=punteggio ;
				pilota = d ;
			}
		}
		return new DriverPunteggio(pilota, max);
	}



	public List<Driver> calcolaDreamTeam(int k) {
		List<Driver> parziale = new ArrayList<>() ;
		this.ottimo = new ArrayList<>() ;
		this.primo = true ;
		
		this.trovaSoluzione(parziale, k) ;
		return this.ottimo;
	}



	private void trovaSoluzione(List<Driver> parziale, int k) {
		// caso terminale: ho k elementi in parziale
		if (parziale.size()==k) {
			if((this.punteggio(parziale)<this.punteggio(ottimo)) || this.primo==true) {
				this.primo = false ;
				ottimo = new ArrayList<>(parziale) ;
			}
			return ;
		}
		
		//caso intermedio: provo ad aggiungere un driver
		Set<Driver> daProvare = grafo.vertexSet() ;
		for (Driver d : daProvare) {
			if (!parziale.contains(d)) {
				parziale.add(d) ;
				this.trovaSoluzione(parziale, k) ;
				parziale.remove(parziale.size()-1) ;
			}
		}
		
	}



	private int punteggio(List<Driver> lista) {
		Set<Driver> esterni = new HashSet<Driver>(grafo.vertexSet()) ;
		esterni.removeAll(lista) ;
		int punteggio = 0 ;
		for(Driver d : esterni) {
			Set<DefaultWeightedEdge> colpiti = grafo.outgoingEdgesOf(d) ;
			for (DefaultWeightedEdge dwe : colpiti) {
				if (lista.contains(grafo.getEdgeTarget(dwe))) {
					punteggio+=grafo.getEdgeWeight(dwe) ;
				}
			}
		}
		return punteggio;
	}


}
