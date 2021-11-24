package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.Plato;
import org.carlosselman.bean.TipoPlato;
//---------------------------------------------------------------------- CASD
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
//---------------------------------------------------------------------- CASD
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//---------------------------------------------------------------------- CASD
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class PlatoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<TipoPlato>listaTipoPlato;
    private ObservableList<Plato>listaPlato;
    private MainApp escenarioPrincipal;
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private TableView tblPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    //====================================================================== CASD
    //--------------------- Método para desactivar controles --------------- CASD
    //====================================================================== CASD
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    //====================================================================== CASD
    //--------------------- Método para activar controles ------------------ CASD
    //====================================================================== CASD
    public void activarControles(){
        //txtCodigoPlato.setEditable(true);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        //cmbCodigoTipoPlato.setEditable(false);
        //cmbCodigoTipoPlato.setDisable(false);
    }
    //====================================================================== CASD
    //--------------------- Método para limpiar los controles -------------- CASD
    //====================================================================== CASD
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcionPlato.setText("");
        txtPrecioPlato.setText("");
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
        cmbCodigoTipoPlato.getSelectionModel().select(null);
    }
    //====================================================================== CASD
    //--------------------- Método para el botón nuevo --------------------- CASD
    //====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                cmbCodigoTipoPlato.setDisable(false);
                txtCodigoPlato.setText("-- Desactivado --");
                txtCodigoPlato.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR:
                if(validacion(txtCantidad,txtDescripcionPlato,
                   txtNombrePlato,txtPrecioPlato,cmbCodigoTipoPlato)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
    //====================================================================== CASD
    //--------------------- Método para eliminar Plato  -------------------- CASD
    //====================================================================== CASD
    public void eliminarPlato(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblPlato.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
            break;
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
                               sp.setInt(1, ((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                               sp.execute();
                               listaPlato.remove(tblPlato.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblPlato.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Plato eliminado con éxito");
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
    //--------------------- Método para editar Plato ----------------------- CASD
    //====================================================================== CASD 
    public void editarPlato(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
                if(tblPlato.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoTipoPlato.setDisable(true);
                    tblPlato.setDisable(true);
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
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
               if(validacion(txtCantidad,txtDescripcionPlato,
                   txtNombrePlato,txtPrecioPlato,cmbCodigoTipoPlato)){
                actualizar();
                tblPlato.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblPlato.setDisable(true);
                tblPlato.setDisable(false);
               }else{
                tblPlato.setDisable(true);   
               } 
            break;
        }
    }
    //====================================================================== CASD
    //--------------------- Método botón Guardar --------------------------- CASD
    //====================================================================== CASD
    public void guardar(){
        Plato platoNuevo = new Plato();
        platoNuevo.setCantidad(Integer.valueOf(txtCantidad.getText()));
        platoNuevo.setNombrePlato(txtNombrePlato.getText()); 
        platoNuevo.setDescripcionPlato(txtDescripcionPlato.getText());
        platoNuevo.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        platoNuevo.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
           sp.setInt(1,platoNuevo.getCantidad());
           sp.setString(2,platoNuevo.getNombrePlato());
           sp.setString(3,platoNuevo.getDescripcionPlato());
           sp.setDouble(4,platoNuevo.getPrecioPlato());
           sp.setInt(5,platoNuevo.getCodigoTipoPlato());
           sp.execute();
           listaPlato.add(platoNuevo);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarPlatos ------------------------ CASD
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
    //--------------------- Método para cargar datos ----------------------- CASD
    //====================================================================== CASD
    public void cargarDatos(){
        tblPlato.setItems(getPlato());
        colCodigoPlato.setCellValueFactory (new PropertyValueFactory<Plato,Integer>("codigoPlato"));
        colCantidad.setCellValueFactory (new PropertyValueFactory<Plato,Integer>("cantidad"));
        colNombrePlato.setCellValueFactory (new PropertyValueFactory<Plato,String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory (new PropertyValueFactory<Plato,String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory (new PropertyValueFactory<Plato,Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory (new PropertyValueFactory<Plato,Integer>("codigoTipoPlato"));
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    desactivarControles();
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarTipoPlato --------------------- CASD
    //====================================================================== CASD
    public ObservableList <TipoPlato> getTipoPlato(){
        ArrayList <TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
            resultado.getString("descripcionTipo")));
            }
        }catch(Exception e){
         e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblPlato.getSelectionModel().getSelectedItem() != null){   
            txtCodigoPlato.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidad.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCantidad())); 
            txtNombrePlato.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcionPlato.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        desactivarControles();
        }
    }    
    //====================================================================== CASD
    //------------- Método para buscar los datos de TipoPlato -------------- CASD
    //====================================================================== CASD
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                        registro.getString("descripcionTipo"));
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
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlatos(?,?,?,?,?)}");
                Plato platoActualizado = ((Plato) tblPlato.getSelectionModel().getSelectedItem());
                //Obteniendo los datos de la vista al modelo en java
                platoActualizado.setCantidad(Integer.valueOf(txtCantidad.getText()));
                platoActualizado.setNombrePlato(txtNombrePlato.getText()); 
                platoActualizado.setDescripcionPlato(txtDescripcionPlato.getText());
                platoActualizado.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
                //Enviando los datos actualizados a ejecutar en el objeto sp
               sp.setInt(1,platoActualizado.getCantidad());
               sp.setString(2,platoActualizado.getNombrePlato());
               sp.setString(3,platoActualizado.getDescripcionPlato());
               sp.setDouble(4,platoActualizado.getPrecioPlato());
               sp.setInt(5,platoActualizado.getCodigoPlato());
               sp.execute();
                JOptionPane.showMessageDialog(null,"Plato actualizado con éxito");
            }catch(Exception e){
                e.printStackTrace(); 
            }
        } 
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD
    public boolean validacion
        (
         TextField txtCantidad, 
         TextField txtDescripcionPlato, 
         TextField txtNombrePlato, 
         TextField txtPrecioPlato, 
         ComboBox cmbCodigoTipoPlato
        ){
    boolean variable = false;	
    if(!(
        //------------------------------------------------------------------ CASD
        txtCantidad.getText()==null || 
        txtCantidad.getText().trim().equals("")||
        txtCantidad.getText().matches("^\\d+$")==false||
        txtCantidad.getText().matches(".*[a-zA-z].*")||
        txtCantidad.getText().length()>11||
        //------------------------------------------------------------------ CASD
        txtNombrePlato.getText()==null || 
        txtNombrePlato.getText().trim().equals("")||
        txtNombrePlato.getText().matches(".*[0-9].*")||
        txtNombrePlato.getText().length()>50||
        //------------------------------------------------------------------ CASD
        txtPrecioPlato.getText()==null || 
        txtPrecioPlato.getText().trim().equals("")||
        txtPrecioPlato.getText().matches("\\d+(\\.\\d{1,2})?")==false|| 
        txtPrecioPlato.getText().matches(".*[a-zA-z].*")||
        txtPrecioPlato.getText().length()>11||
        //------------------------------------------------------------------ CASD
        txtDescripcionPlato.getText()==null || 
        txtDescripcionPlato.getText().trim().equals("")||
        txtDescripcionPlato.getText().length()>150||
        //------------------------------------------------------------------ CASD
        cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()==null
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
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    //---------------------------------------------------------------------- CASD
    //====================================================================== CASD
}
