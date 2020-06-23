package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.YearAndSightings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Button btnAvvistamenti;
    
    @FXML
    private Button btnAnalizza;

    @FXML
    private Button btnSequenza;

    @FXML
    private ComboBox<YearAndSightings> boxAnno;

    @FXML
    private ComboBox<String> boxStato;

    @FXML
    private TextArea txtResult;
    
    @FXML
    void handleAnalizza(ActionEvent event) {
    	txtResult.clear();
    	String state = this.boxStato.getValue();
    	if(state == null) {
    		txtResult.setText("Selezionare uno stato tra quelli presenti nella lista.\n");
    		return;
    	}
    	
    	List<String> predecessors = this.model.getPredecessors(state);
    	List<String> successors = this.model.getSuccessors(state);
    	if(predecessors.isEmpty()) {
    		txtResult.appendText("Nessuno stato è immediatamente precedente allo stato selezionato.\n\n");
    	}
    	else {
    		txtResult.appendText("Stati immediatamente precedenti a " + state + ":\n");
    		for(String s : predecessors)
    			txtResult.appendText(s + "\n");
    		txtResult.appendText("\n");
    	}
    	if(successors.isEmpty()) {
    		txtResult.appendText("Nessuno stato è immediatamente successivo allo stato selezionato.\n\n");
    	}
    	else {
    		txtResult.appendText("Stati immediatamente successivi a " + state + ":\n");
    		for(String s : successors)
    			txtResult.appendText(s + "\n");
    		txtResult.appendText("\n");
    	}
    	
    	List<String> reached = this.model.getReachedStates(state);
    	if(reached.isEmpty()) {
    		txtResult.appendText("Nessun percorso trovato a partire dallo stato selezionato.\n");
    	}
    	else {
    		txtResult.appendText("Stati raggiungibili a partire da " + state + ":\n");
    		for(String s : reached) 
    			txtResult.appendText(s + "\n");
    		txtResult.appendText("Sono stati trovati " + reached.size() + " stati raggiungibili da quello selezionato.\n");
    	}

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	txtResult.clear();
    	YearAndSightings ys = this.boxAnno.getValue();
    	if(ys == null) {
    		txtResult.setText("Errore: selezionare un anno per procedere all'analisi degli avvistamenti.\n");
    		return;
    	}
    	Integer year = ys.getYear();
    	this.model.createGraph(year);
    	
    	this.boxStato.getItems().clear();
    	this.boxStato.getItems().addAll(this.model.getStates());
    	this.btnAnalizza.setDisable(false);
    	this.btnSequenza.setDisable(false);
    	txtResult.setText("Grafo creato!");
      }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	String start = this.boxStato.getValue();
    	if(start == null) {
    		txtResult.setText("Selezionare uno stato tra quelli presenti nella lista.\n");
    		return;
    	}
    	List<String> bestPath = this.model.getBestPath(start);
    	txtResult.appendText("Percorso più lungo trovato (senza ripetizioni di nessun vertice): \n\n");
    	for(String s : bestPath) {
    		txtResult.appendText(s + "\n");
    	}
    }
    
    @FXML
    void doBloccaAnalisi(ActionEvent event) {
		this.btnAnalizza.setDisable(true);
		this.btnSequenza.setDisable(true);
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getYearsAndSightings());
		this.btnAnalizza.setDisable(true);
		this.btnSequenza.setDisable(true);
	}
}
