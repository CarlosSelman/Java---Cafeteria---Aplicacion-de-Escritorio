package org.carlosselman.controller;
//////////////////////////////////////////////////////////////////////////
import org.carlosselman.system.MainApp;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
//////////////////////////////////////////////////////////////////////////
public class MenuPrincipalController implements Initializable {
    private MainApp escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
 //----------------------------------------------------------------------  
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
 //----------------------------------------------------------------------   
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
 //----------------------------------------------------------------------   
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
 //----------------------------------------------------------------------   
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
 //----------------------------------------------------------------------      
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
 //---------------------------------------------------------------------- 
      public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
 //----------------------------------------------------------------------  
      public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
 //---------------------------------------------------------------------- 
      public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
 //---------------------------------------------------------------------- 
      public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
 //---------------------------------------------------------------------- 
      public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
 //----------------------------------------------------------------------
      public void ventanaServiciohasPlatos(){
        escenarioPrincipal.ventanaServiciohasPlatos();
    }
 //----------------------------------------------------------------------
      public void ventanaProductohasPlatos(){
        escenarioPrincipal.ventanaProductohasPlatos();
    }
 //----------------------------------------------------------------------
     public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
 //----------------------------------------------------------------------
    public void ventanaServiciohasEmpleados(){
        escenarioPrincipal.ventanaServiciohasEmpleados();
    }
 //----------------------------------------------------------------------
}
