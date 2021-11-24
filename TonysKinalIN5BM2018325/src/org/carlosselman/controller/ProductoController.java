package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.Producto;
//---------------------------------------------------------------------- CASD
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
//---------------------------------------------------------------------- CASD
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//---------------------------------------------------------------------- CASD
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
//---------------------------------------------------------------------- CASD
import java.util.ResourceBundle;
import java.util.ArrayList;
//---------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//---------------------------------------------------------------------- CASD
import java.net.URL;
//---------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
//---------------------------------------------------------------------- CASD
//====================================================================== CASD
public class ProductoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<Producto>listaProducto;
    private MainApp escenarioPrincipal;
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private TableView tblProducto;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    //====================================================================== CASD
//--------------------- Método para desactivar controles --------------- CASD
//====================================================================== CASD   
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
//====================================================================== CASD
//--------------------- Método para activar controles ------------------ CASD
//====================================================================== CASD
    public void activarControles(){
        //txtCodigoProducto.setEditable(true);
        txtCodigoProducto.setDisable(true);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
//====================================================================== CASD
//--------------------- Método para limpiar los controles -------------- CASD
//====================================================================== CASD
    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }
//====================================================================== CASD
//--------------------- Método para el botón nuevo --------------------- CASD
//====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                txtCodigoProducto.setText("--- Desactivado ---");
                txtCodigoProducto.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR:
                if(validacion(txtNombreProducto,txtCantidad)){
                guardar();
                guardar();
                limpiarControles();
                desactivarControles();
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método para eliminar Productos ----------------- CASD
//====================================================================== CASD
    public void eliminarProducto(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblProducto.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
            break;
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                               sp.setInt(1, ((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                               sp.execute();
                               listaProducto.remove(tblProducto.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblProducto.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Producto eliminado con éxito");
                           }catch(Exception e){
                               e.printStackTrace();
                           }
                        }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un registro de la tabla");  
                }
        }
    }
//====================================================================== CASD
//--------------------- Método para editar Productos ------------------- CASD
//====================================================================== CASD
    public void editarProducto(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    tblProducto.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(false);
                    activarControles();
                    tipoOperacion = Operacion.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                }
            break;
            case ACTUALIZAR:
               tblProducto.setDisable(true);
               tblProducto.setDisable(false);
               if(validacion(txtNombreProducto,txtCantidad)){
               actualizar();
                tblProducto.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblProducto.setDisable(true);
                tblProducto.setDisable(false);
               }else{
                tblProducto.setDisable(true);   
               } 
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método botón Guardar --------------------------- CASD
//====================================================================== CASD
    public void guardar(){
        Producto productoNuevo = new Producto();
        productoNuevo.setNombreProducto(txtNombreProducto.getText());
        productoNuevo.setCantidad(Integer.valueOf(txtCantidad.getText()));
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
           sp.setString(1,productoNuevo.getNombreProducto());
           sp.setInt(2,productoNuevo.getCantidad());
           sp.execute();
           listaProducto.add(productoNuevo);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
//====================================================================== CASD
//-------------- Método para el sp_ListarProductos ---------------------- CASD
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
//--------------------- Método para cargar datos ----------------------- CASD
//====================================================================== CASD
    public void cargarDatos(){
        tblProducto.setItems(getProducto());
        colCodigoProducto.setCellValueFactory (new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory (new PropertyValueFactory<Producto,String>("nombreProducto"));
        colCantidad.setCellValueFactory (new PropertyValueFactory<Producto,Integer>("cantidad"));
    desactivarControles();
    }
//======================================================================================= CASD
//-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
//======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblProducto.getSelectionModel().getSelectedItem() != null){
             txtCodigoProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()));
             txtNombreProducto.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto());
             txtCantidad.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
        desactivarControles();
        }
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
//======================================================================================= CASD
//----- Método para actualizar los datos del modelo y del tableview ejecutando el sp ---- CASD
//======================================================================================= CASD
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
            Producto productoActualizada = ((Producto) tblProducto.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            productoActualizada.setNombreProducto(txtNombreProducto.getText());
            productoActualizada.setCantidad(Integer.valueOf(txtCantidad.getText()));
            //Enviando los datos actualizados a ejecutar en el objeto sp
            sp.setString(1, productoActualizada.getNombreProducto());
            sp.setInt(2, productoActualizada.getCantidad());
            sp.execute();
            JOptionPane.showMessageDialog(null,"Producto actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD 
    public boolean validacion
        (
         TextField txtNombreProducto, 
         TextField txtCantidad
        ){
    boolean variable = false;	
    if(!(
        //------------------------------------------------------------------ CASD
        txtNombreProducto.getText()==null || 
        txtNombreProducto.getText().trim().equals("")||
        txtNombreProducto.getText().matches(".*[0-9].*")||
        txtNombreProducto.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtCantidad.getText()==null || 
        txtCantidad.getText().trim().equals("")||
        txtCantidad.getText().matches("^\\d+$")==false||    
        txtCantidad.getText().matches(".*[a-zA-z].*")||
        txtCantidad.getText().length()>11
        //------------------------------------------------------------------ CASD
      )){ 
        variable = true;
      }else{
        JOptionPane.showMessageDialog(null,"LLene todos los campos de manera correcta");
      }
     return variable;
    }
    //====================================================================== CASD
    //----------------------- Método initialize ---------------------------- CASD
    //====================================================================== CASD  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
    }
    //---------------------------------------------------------------------- CASD
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
    //====================================================================== CASD  
}
