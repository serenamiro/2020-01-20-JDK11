package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	private ArtsmiaDAO dao;
	private List<Integer> best;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}

	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// PER LA CREAZIONE DEL GRAFO SI POSSONO SEGUIRE DUE STRADE DIVERSE.
		// 1. farsi dare tutti i vertici da inserire nel grafo dal db e successivamente inserire gli archi
		// 2. avendo le adiacenze, controlliamo volta per volta se i nodi sono o meno presenti nel grafo; se non ci sono
		// li aggiungiamo direttamente.
		// La differenza è che usando le adiacenze alcuni vertici potrebbero mancare (ad esempio, vertici isolati).
		// Scegliere il metodo a seconda delle richieste del problema
		
		Graphs.addAllVertices(this.grafo, this.dao.getArtisti(ruolo)); // inserisco tutti i vertici prima
		this.adiacenze = this.dao.getAdiacenze(ruolo);
		
		for(Adiacenza a : adiacenze) {
			/* questo codice andrebbe per aggiungere i vertici direttamente dalle adiacenze
			if(!this.grafo.containsVertex(a.getA1())) {
				this.grafo.addVertex(a.getA1());
			}
			if(!this.grafo.containsVertex(a.getA1())) {
				this.grafo.addVertex(a.getA1());
			}
			*/
			if(this.grafo.getEdge(a.getA1(), a.getA2()) == null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
		System.out.println("Grafo creato!");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public List<String> getRuoli(){
		return this.dao.getRuoli();
	}
	
	public int vertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int archi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getAdiacenze(){
		return this.adiacenze;
	}
	
	// TROVARE IL PERCORSO PIU' LUNGO TRA I NODI, IN CUI GLI ARCHI ABBIANO TUTTI LO STESSO PESO
	public List<Integer> trovaPercorso(Integer sorgente){
		this.best = new ArrayList<Integer>();
		List<Integer> parziale = new ArrayList<Integer>();
		parziale.add(sorgente);
		
		// metodo per lanciare la ricorsione
		// per lanciare la ricorsione, imposto un peso che non esiste nel grafo, in modo da poter isolare il caso iniziale
		ricorsione(parziale, -1);
		
		return best;	
	}
	
	private void ricorsione(List<Integer> parziale, int peso) {
		
		Integer ultimo = parziale.get(parziale.size()-1);
		// prendo tutti i vicini di questo nodo
		List<Integer> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(Integer vicino : vicini) {
			// bisogna controllare anche che il parziale non contenga il nodo, in quanto non vogliamo cicli
			if(!parziale.contains(vicino) & peso == -1) {
				// se entro in questo ciclo vuol dire che sono alla prima iterazione
				parziale.add(vicino);
				ricorsione(parziale,(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
				parziale.remove(vicino);
			} else {
				if(!parziale.contains(vicino) & this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)) == peso) {
					parziale.add(vicino);
					ricorsione(parziale, peso);
					parziale.remove(vicino);
				}
			}
		}
		
		if(parziale.size()>best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		
	}
	
	public boolean grafoContiene(Integer id) {
		if(this.grafo.containsVertex(id)) {
			return true;
		}
		return false;
	}
}

