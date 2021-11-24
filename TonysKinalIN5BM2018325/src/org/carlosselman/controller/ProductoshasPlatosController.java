package org.carlosselman.controller;
//======================================================================= CASD
//-------------------------- Importaciones  ----------------------------- CASD
//======================================================================= CASD
import org.carlosselman.system.MainApp;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Producto;
import org.carlosselman.bean.Plato;
import org.carlosselman.bean.ProductoshasPlatos;
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
public class ProductoshasPlatosController implements Initializable {
    private ObservableList<ProductoshasPlatos>listaProductoshasPlatos;
    private ObservableList<Producto>listaProducto;
    private ObservableList<Plato>listaPlato;
    private MainApp escenarioPrincipal;
    @FXML private TableView tblProductoshasPlatos;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    //====================================================================== CASD
    //----------- Método para el sp_ListarProductos_has_Platos ------------- CASD
    //====================================================================== CASD   
    public ObservableList <ProductoshasPlatos> getProductoshasPlatos(){
    ArrayList <ProductoshasPlatos> lista = new ArrayList<ProductoshasPlatos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ProductoshasPlatos(resultado.getInt("codigoProducto"),
            resultado.getInt("codigoPlato")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaProductoshasPlatos = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //--------------------- Método para cargar datos ----------------------- CASD
    //====================================================================== CASD
    public void cargarDatos(){
        tblProductoshasPlatos.setItems(getProductoshasPlatos());
        colCodigoProducto.setCellValueFactory (new PropertyValueFactory<ProductoshasPlatos,Integer>("codigoProducto"));
        cmbCodigoProducto.setItems(getProducto());
        colCodigoPlato.setCellValueFactory (new PropertyValueFactory<ProductoshasPlatos,Integer>("codigoPlato"));
        cmbCodigoPlato.setItems(getPlato());
    }
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblProductoshasPlatos.getSelectionModel().getSelectedItem() != null){
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductoshasPlatos)tblProductoshasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductoshasPlatos)tblProductoshasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarProductos --------------------- CASD
    //====================================================================== CASD   
    public ObservableList <Producto> getProducto(){
        ArrayList <Producto> lista = new ArrayList<Producto>();
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Producto(resultado.getInt("codigoProducto"),
            resultado.getString("nombreProducto"),
            resultado.getInt("cantidad")));
        }
        }catch(Exception e){
         e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //------------- Método para buscar los datos de Producto --------------- CASD
    //====================================================================== CASD
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                        registro.getString("nombreProducto"),
                                        registro.getInt("cantidad"));
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
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    //---------------------------------------------------------------------- CASD
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    //---------------------------------------------------------------------- CASD
    //====================================================================== CASD
}
