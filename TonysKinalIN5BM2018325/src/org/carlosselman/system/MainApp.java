package org.carlosselman.system;
////////////////////////////////////////////////////////////////////////////////////////////////
import javafx.fxml.JavaFXBuilderFactory;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import java.io.InputStream;
////////////////////////////////////////////////////////////////////////////////////////////////
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
////////////////////////////////////////////////////////////////////////////////////////////////
import org.carlosselman.controller.MenuPrincipalController; //Menú Principal o Escena Principal
import org.carlosselman.controller.EmpresaController; //Tabla Empresa
import org.carlosselman.controller.EmpleadoController; //Tabla Empleado
import org.carlosselman.controller.TipoEmpleadoController; //Tabla TipoEmpleado
import org.carlosselman.controller.PresupuestoController; //Tabla Presupuesto
import org.carlosselman.controller.ServicioController; //Tabla Servicio
import org.carlosselman.controller.PlatoController; //Tabla Plato
import org.carlosselman.controller.TipoPlatoController; //Tabla TipoPlato
import org.carlosselman.controller.ServicioshasPlatosController; 
import org.carlosselman.controller.DatosPersonalesController; //Acerca de
import org.carlosselman.controller.ProductoshasPlatosController;
import org.carlosselman.controller.ProductoController;
import org.carlosselman.controller.ServiciohasEmpleadosController;
////////////////////////////////////////////////////////////////////////////////////////////////
public class MainApp extends Application {
    private final String PACKAGE_VIEW = "/org/carlosselman/view/"; // Dirección del paquete de Vistas (org.carlosselman.view)
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("TONY'S KINAL");
        escenarioPrincipal.getIcons().add(new Image("/org/carlosselman/img/Logo Tony's Kinal.png")); // Dirección de la imagen colocada en (org.carlosselman.img) 
        menuPrincipal();
        escenarioPrincipal.show();
    }
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void menuPrincipal(){
        try{
            MenuPrincipalController escenaPrincipalC = (MenuPrincipalController)cambiarDeEscena("MenuPrincipalView.fxml",411,447); //Dirección y tamaño de la vista del Menú Principal
            escenaPrincipalC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void ventanaProgramador(){
        try{
            DatosPersonalesController escenaDeDatosPC = (DatosPersonalesController)cambiarDeEscena("DatosPersonalesView.fxml",489,396); //Dirección y tamaño de DatosPersonales
            escenaDeDatosPC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
    public void ventanaEmpresa(){
        try{
          EmpresaController escenaEmpresaC = (EmpresaController)cambiarDeEscena("EmpresaView.fxml",655,493); //Dirección y tamaño de la vista de Empresa
          escenaEmpresaC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void ventanaEmpleado(){
        try{
          EmpleadoController escenaEmpleadoC = (EmpleadoController)cambiarDeEscena("EmpleadoView.fxml",841,575); //Dirección y tamaño de la vista de Empleado
          escenaEmpleadoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      public void ventanaTipoEmpleado(){
        try{
          TipoEmpleadoController escenaTipoEmpleadoC = (TipoEmpleadoController)cambiarDeEscena("TipoEmpleadoView.fxml",657,492); //Dirección y tamaño de la vista de TipoEmpleado
          escenaTipoEmpleadoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      public void ventanaPlato(){
        try{
          PlatoController escenaPlatoC = (PlatoController)cambiarDeEscena("PlatoView.fxml",664,400); //Dirección y tamaño de la vista de Plato
          escenaPlatoC.setEscenarioPrincipal(this);
         }catch(Exception e){
           e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      public void ventanaTipoPlato(){
        try{
          TipoPlatoController escenaTipoPlatoC = (TipoPlatoController)cambiarDeEscena("TipoPlatoView.fxml",657,492); //Dirección y tamaño de la vista de TipoPlato
          escenaTipoPlatoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      public void ventanaPresupuesto(){
        try{
          PresupuestoController escenaPresupuestoC = (PresupuestoController)cambiarDeEscena("PresupuestoView.fxml",657,492); //Dirección y tamaño de la vista de Presupuesto
          escenaPresupuestoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public void ventanaServicio(){ 
        try{
          ServicioController escenaServicioC = (ServicioController)cambiarDeEscena("ServicioView.fxml",785,486); //Dirección y tamaño de la vista de Servicio
          escenaServicioC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public void ventanaServiciohasPlatos(){ 
        try{
          ServicioshasPlatosController escenaServiciohPC = (ServicioshasPlatosController)cambiarDeEscena("ServiciohasPlatosView.fxml",653,400); //Dirección y tamaño de la vista de ServiciohasPlatos
          escenaServiciohPC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public void ventanaProductohasPlatos(){ 
        try{
          ProductoshasPlatosController escenaProductohPC = (ProductoshasPlatosController)cambiarDeEscena("ProductohasPlatos.fxml",662,400); //Dirección y tamaño de la vista de ProductohasPlatos
          escenaProductohPC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public void ventanaProducto(){ 
        try{
          ProductoController escenaProductoC = (ProductoController)cambiarDeEscena("ProductoView.fxml",550,400); //Dirección y tamaño de la vista de Servicio
          escenaProductoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public void ventanaServiciohasEmpleados(){ 
        try{
          ServiciohasEmpleadosController escenaServiciohEmpleadoC = (ServiciohasEmpleadosController)cambiarDeEscena("ServiciohasEmpleadosView.fxml",737,427); //Dirección y tamaño de la vista de ServiciohasEmpleados
          escenaServiciohEmpleadoC.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
      }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 
      public static void main(String[] args) {
        launch(args);
    }
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Initializable cambiarDeEscena(String fxml, int ancho, int alto) throws Exception{
      Initializable resultado = null;
      FXMLLoader cargadorFXML = new FXMLLoader();
      InputStream archivo = MainApp.class.getResourceAsStream(PACKAGE_VIEW+fxml);
      cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
      cargadorFXML.setLocation(MainApp.class.getResource(PACKAGE_VIEW+fxml));
      escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
      escenarioPrincipal.setScene(escena);
      escenarioPrincipal.sizeToScene();
      resultado = (Initializable)cargadorFXML.getController();
             return resultado;
    } 
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}

