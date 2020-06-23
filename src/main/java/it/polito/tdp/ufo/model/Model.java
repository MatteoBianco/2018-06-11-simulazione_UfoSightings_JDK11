package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	private Graph<String, DefaultEdge> graph;
	
	private List<String> bestPath;
	private Integer bestSize;
	
	public Model() {
		this.dao = new SightingsDAO();
	}
	
	public void createGraph(Integer year) {
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getStates(year));
		for(CountryPair cp : this.dao.getCountryPairs(year)) {
			if(this.graph.getEdge(cp.getC1(), cp.getC2()) == null) {
				this.graph.addEdge(cp.getC1(), cp.getC2());
			}
		}
		System.out.println(this.graph.vertexSet().size());
		System.out.println(this.graph.edgeSet().size());
	}
	
	public List<String> getBestPath(String start) {
		this.bestPath = new ArrayList<>();
		this.bestSize = 0;
		
		List<String> parziale = new ArrayList<>();
		parziale.add(start);
		recursion(parziale, start);
		return this.bestPath;
	}
	
	private void recursion(List<String> parziale, String last) {
		if(parziale.size() > this.bestSize) {
			this.bestPath = new ArrayList<>(parziale);
			this.bestSize = parziale.size();
		}
		
		List<String> successors = this.getSuccessors(last);
		successors.retainAll(parziale);
		for(String successor : successors) {
			if(! parziale.contains(successor)) {
				parziale.add(successor);
				recursion(parziale, successor);
				parziale.remove(parziale.size()-1);
			}
		}
	}

	public List<String> getStates() {
		return new ArrayList<>(this.graph.vertexSet());
	}
	
	public List<String> getPredecessors(String state) {
		return Graphs.predecessorListOf(this.graph, state);
	}
	
	public List<String> getSuccessors(String state) {
		return Graphs.successorListOf(this.graph, state);
	}
	
	public List<String> getReachedStates(String state) {
		List<String> result = new ArrayList<>();
		GraphIterator<String, DefaultEdge> dfv = new DepthFirstIterator<>(this.graph, state);
		while(dfv.hasNext()) {
			result.add(dfv.next());
		}
		result.remove(0);
		return result;
	}
	
	public List<YearAndSightings> getYearsAndSightings() {
		return this.dao.getSightingsForYear();
	}

}
