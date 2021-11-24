package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.TipoPlato;
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
//====================================================================== CASD
public class TipoPlatoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<TipoPlato>listaTipoPlato;
    private MainApp escenarioPrincipal;
    @FXML 
    private TextField txtCodigoTipoPlato, txtDescripcionTipo;
    @FXML 
    private Button btnNuevo, btnEditar, btnEliminar;
    @FXML
    private TableView tblTipoPlato;
    @FXML 
    private TableColumn colCodigoTipoPlato, colDescripcionTipo;
//====================================================================== CASD
//--------------------- Método para desactivar controles --------------- CASD
//====================================================================== CASD   
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipo.setEditable(false);
    }
//====================================================================== CASD
//--------------------- Método para activar controles ------------------ CASD
//====================================================================== CASD
    public void activarControles(){
        //txtCodigoTipoPlato.setEditable(true);
        txtDescripcionTipo.setEditable(true);
    }
//====================================================================== CASD
//--------------------- Método para limpiar los controles -------------- CASD
//====================================================================== CASD
    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipo.setText("");
    }
//====================================================================== CASD
//--------------------- Método para el botón nuevo --------------------- CASD
//====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                txtCodigoTipoPlato.setText("-- Desactivado --");
                txtCodigoTipoPlato.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
                limpiarControles();
            break;
            case GUARDAR:
                if(validacion(txtDescripcionTipo)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método para eliminar Tipo Plato ---------------- CASD
//====================================================================== CASD
    public void eliminarTipoPlato(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblTipoPlato.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
            break;
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Tipo Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                               sp.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                               sp.execute();
                               listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblTipoPlato.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"TipoEmpleado eliminada con éxito");
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
//--------------------- Método para editar Tipo Plato ------------------ CASD
//====================================================================== CASD
    public void editarTipoPlato(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    tblTipoPlato.setDisable(true);
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
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
               if(validacion(txtDescripcionTipo)){
                actualizar();
                tblTipoPlato.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblTipoPlato.setDisable(true);
                tblTipoPlato.setDisable(false);
               }else{
                tblTipoPlato.setDisable(true);   
               } 
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método botón Guardar --------------------------- CASD
//====================================================================== CASD
    public void guardar(){
        TipoPlato tipoEmpleadoNuevo = new TipoPlato();
        tipoEmpleadoNuevo.setDescripcionTipo(txtDescripcionTipo.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
           sp.setString(1,tipoEmpleadoNuevo.getDescripcionTipo());
           sp.execute();
           listaTipoPlato.add(tipoEmpleadoNuevo);
        }catch(Exception e){
                e.printStackTrace();
        }
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
//====================================================================== CASD
//--------------------- Método para cargar datos ----------------------- CASD
//====================================================================== CASD
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory (new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcionTipo.setCellValueFactory (new PropertyValueFactory<TipoPlato,String>("descripcionTipo"));
    desactivarControles();
    }
//======================================================================================= CASD
//-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
//======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){ 
            txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionTipo.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
        desactivarControles();
        }
    }
//======================================================================= CASD
//------------- Método para buscar los datos de Tipo Plato -------------- CASD
//======================================================================= CASD
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
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
            TipoPlato tipoPlatoActualizado = ((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            tipoPlatoActualizado.setDescripcionTipo(txtDescripcionTipo.getText());
            //Enviando los datos actualizados a ejecutar en el objeto sp
            sp.setString(1, tipoPlatoActualizado.getDescripcionTipo());
            sp.setInt(2, tipoPlatoActualizado.getCodigoTipoPlato());
            sp.execute();
            JOptionPane.showMessageDialog(null,"TipoPlato actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
//====================================================================== CASD
//---------------------- Método de Validación -------------------------- CASD
//====================================================================== CASD 
    public boolean validacion(TextField txtDescripcionTipo){
    boolean variable = false;
        if(!txtDescripcionTipo.getText().equals("")){
           variable = true;
        }else{
              JOptionPane.showMessageDialog(null,"LLene todos los campos");
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
//====================================================================== CASD
//------------------------------ Métodos de enlace  -------------------- CASD
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
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
   }
//---------------------------------------------------------------------- CASD
//====================================================================== CASD 
}
