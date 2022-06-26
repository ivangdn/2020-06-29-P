package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Graph<Match, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Match> idMap;
	
	private List<Match> best;
	private int pesoMax;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
		dao.listAllMatches(idMap);
	} 
	
	public void creaGrafo(int minutiGiocati, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getMatchesByMonth(mese, this.idMap));
		
		List<Coppia> coppie = dao.getCoppie(mese, minutiGiocati, idMap);
		for(Coppia c : coppie) {
			Graphs.addEdge(this.grafo, c.getM1(), c.getM2(), c.getPeso());
		}

	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> getVertici() {
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public List<Coppia> getConnessioneMax() {
		if(this.grafo==null) {
			return null;
		}
		
		int max = 0;
		List<Coppia> connessioniMax = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso = (int) this.grafo.getEdgeWeight(e);
			if(peso > max) {
				connessioniMax.clear();
				connessioniMax.add(new Coppia(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), peso));
				max = peso;
			} else if(peso == max) {
				connessioniMax.add(new Coppia(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), peso));
			}
		}
		return connessioniMax;
	}
	
	public List<Match> calcolaCollegamento(Match m1, Match m2) {
		if(this.grafo==null) {
			return null;
		}
		
		this.best = new ArrayList<>();
		this.pesoMax = 0;
		List<Match> parziale = new ArrayList<>();
		parziale.add(m1);
		cerca(parziale, m2);
		return best;
	}

	private void cerca(List<Match> parziale, Match m2) {
		//condizioni di terminazione
		if(parziale.get(parziale.size()-1).equals(m2)) {
			//controllo se è la soluzione migliore
			int pesoParziale = calcolaPeso(parziale);
			if(pesoParziale > this.pesoMax) {
				this.best = new ArrayList<>(parziale);
				pesoMax = pesoParziale;
			}
			return;
		}
		
		//ricorsione
		for(Match m : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(m) &&
					((m.getTeamHomeID()!=parziale.get(parziale.size()-1).getTeamHomeID() || m.getTeamAwayID()!=parziale.get(parziale.size()-1).getTeamAwayID()) &&
					(m.getTeamHomeID()!=parziale.get(parziale.size()-1).getTeamAwayID() || m.getTeamAwayID()!=parziale.get(parziale.size()-1).getTeamHomeID()))) {
				parziale.add(m);
				cerca(parziale, m2);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	public int calcolaPeso(List<Match> listaMatch) {
		int max = 0;
		for(int i=0; i<listaMatch.size()-1; i++) {
			Match m1 = listaMatch.get(i);
			Match m2 = listaMatch.get(i+1);
			max += this.grafo.getEdgeWeight(this.grafo.getEdge(m1, m2));
		}
		
		return max;
	}
	
}
