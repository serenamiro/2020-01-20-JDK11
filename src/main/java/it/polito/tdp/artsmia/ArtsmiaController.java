package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenza> adiacenze = this.model.getAdiacenze();
    	if(adiacenze == null) {
    		txtResult.appendText("Devi creare prima il grafo.");
    		return;
    	}
    	Collections.sort(adiacenze);
    	for(Adiacenza a : adiacenze) {
    		txtResult.appendText(String.format("(%d, %d) = %d\n", a.getA1(), a.getA2(), a.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Integer id;
    	try {
    		id = Integer.parseInt(txtArtista.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("INSERIRE UN ID NEL FORMATO CORRETTO.");
    		return;
    	}
    	// l'id deve essere corretto e deve far parte del grafo
    	if(!this.model.grafoContiene(id)) {
    		txtResult.appendText("L'artista non è nel grafo.");
    		return;
    	}
    	
    	List<Integer> percorso = this.model.trovaPercorso(id);
    	txtResult.appendText("Percorso più lungo: "+percorso.size()+"\n");
    	for(Integer v : percorso) {
    		txtResult.appendText(v+" ");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo == null) {
    		txtResult.appendText("Seleziona un ruolo");
    		return;
    	}
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.vertici(), this.model.archi()));
    	btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	// disattivo il bottone calcola percorso e lo attivo solo quando il grafo è creato
    	btnCalcolaPercorso.setDisable(true);
    	boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
