 package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(Match m) {
		this.grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(m, idMap));
		
		//aggiungo archi
		for(Adiacenza a : this.dao.getAdiacenze(m, idMap)) {
			if(a.getPeso() >= 0) {//meglio p1
				if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			}
			else {//meglio p2
				if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (a.getPeso()*-1));
			}
		}			
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> listAllMatches(){
		List<Match> mm = this.dao.listAllMatches();
		Collections.sort(mm, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				return o1.matchID.compareTo(o2.matchID);
			}
		});
		return mm;
	}

	public GiocatoreMigliore getMigliore() {
		if(grafo == null)
			return null;
		
		Player best = null;
		double maxDelta = 0;
		for(Player p : this.grafo.vertexSet()) {
			double pesoUscente = 0.0;
			for(DefaultWeightedEdge out : this.grafo.outgoingEdgesOf(p))
				pesoUscente += this.grafo.getEdgeWeight(out);
			
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge in : this.grafo.incomingEdgesOf(p))
				pesoEntrante += this.grafo.getEdgeWeight(in);
			
			double delta = pesoUscente - pesoEntrante;
			if(delta > maxDelta) {
				maxDelta = delta;
				best = p;
			}
		}
		return new GiocatoreMigliore(best, maxDelta);
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
	
	public String getTeamFromPlayer(Player p, Match m) {
		return this.dao.getTeamFromPlayer(p, m);
	}
}
