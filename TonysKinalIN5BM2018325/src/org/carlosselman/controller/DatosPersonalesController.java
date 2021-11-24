package org.carlosselman.controller;
//////////////////////////////////////////////////////////////////////////
import org.carlosselman.system.MainApp;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
//////////////////////////////////////////////////////////////////////////
public class DatosPersonalesController implements Initializable {
    private MainApp escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
//----------------------------------------------------------------------
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
//----------------------------------------------------------------------
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
//----------------------------------------------------------------------    
    public void menuPrincipal(){
     escenarioPrincipal.menuPrincipal();
    }
//----------------------------------------------------------------------    
}
