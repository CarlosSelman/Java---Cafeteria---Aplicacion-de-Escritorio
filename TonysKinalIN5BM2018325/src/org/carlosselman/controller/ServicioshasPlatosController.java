package org.carlosselman.controller;
//======================================================================= CASD
//-------------------------- Importaciones  ----------------------------- CASD
//======================================================================= CASD
import org.carlosselman.system.MainApp;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Servicio;
import org.carlosselman.bean.Plato;
import org.carlosselman.bean.ServicioshasPlatos;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bd.Conexion;
//----------------------------------------------------------------------- CASD
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
//----------------------------------------------------------------------- CASD
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//----------------------------------------------------------------------- CASD
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
//----------------------------------------------------------------------- CASD
import java.util.ResourceBundle;
import java.util.ArrayList;
//----------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//----------------------------------------------------------------------- CASD
import java.net.URL;
import javafx.scene.control.TableView;
//======================================================================= CASD
public class ServicioshasPlatosController implements Initializable {
    private ObservableList<ServicioshasPlatos>listaServicioshasPlatos;
    private ObservableList<Servicio>listaServicio;
    private ObservableList<Plato>listaPlato;
    private MainApp escenarioPrincipal;
    @FXML private TableView tblServicioshasPlatos;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    //====================================================================== CASD
    //----------- Método para el sp_ListarServicios_has_Platos ------------- CASD
    //====================================================================== CASD   
    public ObservableList <ServicioshasPlatos> getServicioshasPlatos(){
    ArrayList <ServicioshasPlatos> lista = new ArrayList<ServicioshasPlatos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ServicioshasPlatos(resultado.getInt("codigoServicio"),
            resultado.getInt("codigoPlato")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaServicioshasPlatos = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //--------------------- Método para cargar datos ----------------------- CASD
    //====================================================================== CASD
    public void cargarDatos(){
        tblServicioshasPlatos.setItems(getServicioshasPlatos());
        colCodigoServicio.setCellValueFactory (new PropertyValueFactory<ServicioshasPlatos,Integer>("codigoServicio"));
        cmbCodigoServicio.setItems(getServicio());
        colCodigoPlato.setCellValueFactory (new PropertyValueFactory<ServicioshasPlatos,Integer>("codigoPlato"));
        cmbCodigoPlato.setItems(getPlato());
    }
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblServicioshasPlatos.getSelectionModel().getSelectedItem() != null){
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioshasPlatos)tblServicioshasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServicioshasPlatos)tblServicioshasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarServicios --------------------- CASD
    //====================================================================== CASD   
    public ObservableList <Servicio> getServicio(){
    ArrayList <Servicio> lista = new ArrayList<Servicio>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Servicio(resultado.getInt("codigoServicio"),
            resultado.getDate("fechaServicio"),
            resultado.getString("tipoServicio"),
            resultado.getString("horaServicio"),
            resultado.getString("lugarServicio"),
            resultado.getString("telefonoContacto"),
            resultado.getInt("codigoEmpresa")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaServicio = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //------------- Método para buscar los datos de Servicio --------------- CASD
    //====================================================================== CASD
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                        registro.getDate("fechaServicio"),
                                        registro.getString("tipoServicio"), 
                                        registro.getString("horaServicio"),
                                        registro.getString("lugarServicio"),
                                        registro.getString("telefonoContacto"),
                                        registro.getInt("codigoEmpresa")
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarServicios --------------------- CASD
    //====================================================================== CASD   
    public ObservableList <Plato> getPlato(){
    ArrayList <Plato> lista = new ArrayList<Plato>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Plato(resultado.getInt("codigoPlato"),
            resultado.getInt("cantidad"),
            resultado.getString("nombrePlato"),
            resultado.getString("descripcionPlato"),
            resultado.getDouble("precioPlato"),
            resultado.getInt("codigoTipoPlato")));                             
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaPlato = FXCollections.observableArrayList(lista);
    }
        //====================================================================== CASD
    //------------- Método para buscar los datos de Servicio --------------- CASD
    //====================================================================== CASD
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"), 
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("codigoTipoPlato")
            );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    //====================================================================== CASD
    //----------------------- Método initialize ---------------------------- CASD
    //====================================================================== CASD  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
    }
    //====================================================================== CASD
    //----------------------- Métodos de enlace ---------------------------- CASD
    //====================================================================== CASD
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    //---------------------------------------------------------------------- CASD
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //---------------------------------------------------------------------- CASD
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //---------------------------------------------------------------------- CASD
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    //---------------------------------------------------------------------- CASD
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    //---------------------------------------------------------------------- CASD
    //====================================================================== CASD
}