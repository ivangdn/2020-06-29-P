/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Coppia;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	
    	List<Coppia> connessioniMax = this.model.getConnessioneMax();
    	if(connessioniMax==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	txtResult.appendText("Coppie con connessione massima:\n\n");
    	for(Coppia c : connessioniMax) {
    		txtResult.appendText(String.format("%s - %s (%d)\n", c.getM1(), c.getM2(), c.getPeso()));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	int minuti;
    	try {
    		minuti = Integer.parseInt(txtMinuti.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("Il campo MIN deve contenere un valore numerico");
    		return;
    	}
    	
    	Integer mese = cmbMese.getValue();
    	if(mese==null) {
    		txtResult.setText("Selezionare un mese");
    		return;
    	}
    	
    	this.model.creaGrafo(minuti, mese);
    	txtResult.appendText("Grafo creato\n");
    	txtResult.appendText("# VERTICI: "+this.model.nVertici()+"\n");
    	txtResult.appendText("# ARCHI: "+this.model.nArchi());
    	
    	cmbM1.getItems().clear();
    	cmbM2.getItems().clear();
    	cmbM1.getItems().addAll(this.model.getVertici());
    	cmbM2.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	txtResult.clear();
    	
    	Match m1 = cmbM1.getValue();
    	Match m2 = cmbM2.getValue();
    	if(m1==null || m2==null) {
    		txtResult.setText("Selezionare due match");
    	}
    	
    	List<Match> collegamenti = this.model.calcolaCollegamento(m1, m2);
    	if(collegamenti==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	int pesoMax = this.model.calcolaPeso(collegamenti);
    	txtResult.appendText("Collegamento con peso massimo:\n\n");
    	for(Match m : collegamenti) {
    		txtResult.appendText(m.toString()+"\n");
    	}
    	txtResult.appendText("\nPESO: "+pesoMax);
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        for(int i=1; i<=12; i++) {
        	cmbMese.getItems().add(i);
        }

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
    
}
