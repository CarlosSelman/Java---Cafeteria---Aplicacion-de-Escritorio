package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.TipoEmpleado;
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
public class TipoEmpleadoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<TipoEmpleado>listaTipoEmpleado;
    private MainApp escenarioPrincipal;
    @FXML 
    private TextField txtCodigoTipoEmpleado, txtDescripcion;
    @FXML 
    private Button btnNuevo, btnEditar, btnEliminar;
    @FXML
    private TableView tblTipoEmpleado;
    @FXML 
    private TableColumn colCodigoTipoEmpleado, colDescripcion;
//====================================================================== CASD
//--------------------- Método para desactivar controles --------------- CASD
//====================================================================== CASD   
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }
//====================================================================== CASD
//--------------------- Método para activar controles ------------------ CASD
//====================================================================== CASD
    public void activarControles(){
        //txtCodigoTipoEmpleado.setEditable(true);
        txtDescripcion.setEditable(true);
    }
//====================================================================== CASD
//--------------------- Método para limpiar los controles -------------- CASD
//====================================================================== CASD
    public void limpiarControles(){
        txtCodigoTipoEmpleado.setText("");
        txtDescripcion.setText("");
    }
//====================================================================== CASD
//--------------------- Método para el botón nuevo --------------------- CASD
//====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                txtCodigoTipoEmpleado.setText("-- Desactivado --");
                txtCodigoTipoEmpleado.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
                limpiarControles();
            break;
            case GUARDAR:
                if(validacion(txtCodigoTipoEmpleado,txtDescripcion)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método para eliminar Tipo Empleado ------------- CASD
//====================================================================== CASD
    public void eliminarTipoEmpleado(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblTipoEmpleado.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
            break;
            case GUARDAR:
               desactivarControles(); 
                limpiarControles();
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Tipo Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                               sp.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                               sp.execute();
                               listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblTipoEmpleado.getSelectionModel().clearSelection();
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
//--------------------- Método para editar Tipo Empleado --------------- CASD
//====================================================================== CASD
    public void editarTipoEmpleado(){
        switch(tipoOperacion){
             case NINGUNO:
                btnEditar.setText("Editar");
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                    tblTipoEmpleado.setDisable(true);
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
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
               if(validacion(txtCodigoTipoEmpleado,txtDescripcion)){
                actualizar();
                tblTipoEmpleado.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblTipoEmpleado.setDisable(true);
                tblTipoEmpleado.setDisable(false);
               }else{
                tblTipoEmpleado.setDisable(true);   
               } 
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método botón Guardar --------------------------- CASD
//====================================================================== CASD
    public void guardar(){
        TipoEmpleado tipoEmpleadoNuevo = new TipoEmpleado();
        tipoEmpleadoNuevo.setDescripcion(txtDescripcion.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
           sp.setString(1,tipoEmpleadoNuevo.getDescripcion());
           sp.execute();
           listaTipoEmpleado.add(tipoEmpleadoNuevo);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
//====================================================================== CASD
//-------------- Método para el sp_ListarTipoEmpleado ------------------ CASD
//====================================================================== CASD  
    public ObservableList <TipoEmpleado> getTipoEmpleado(){
        ArrayList <TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
            resultado.getString("descripcion")));
            }
        }catch(Exception e){
         e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
//====================================================================== CASD
//--------------------- Método para cargar datos ----------------------- CASD
//====================================================================== CASD
    public void cargarDatos(){
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory (new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory (new PropertyValueFactory<TipoEmpleado,String>("descripcion"));
    desactivarControles();
    }
//======================================================================================= CASD
//-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
//======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){ 
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
        desactivarControles();
        }
    }
//======================================================================= CASD
//------------- Método para buscar los datos de Tipo Empleado ----------- CASD
//======================================================================= CASD
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                        registro.getString("descripcion"));
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
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
            TipoEmpleado tipoEmpleadoActualizado = ((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            tipoEmpleadoActualizado.setDescripcion(txtDescripcion.getText());
            //Enviando los datos actualizados a ejecutar en el objeto sp
            sp.setString(1, tipoEmpleadoActualizado.getDescripcion());
            sp.setInt(2, tipoEmpleadoActualizado.getCodigoTipoEmpleado());
            sp.execute();
            JOptionPane.showMessageDialog(null,"TipoEmpleado actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
//====================================================================== CASD
//---------------------- Método de Validación -------------------------- CASD
//====================================================================== CASD 
    public boolean validacion(TextField txtCodigoTipoEmpleado,TextField txtDescripcion){
    boolean variable = false;
        if(!txtCodigoTipoEmpleado.getText().equals("") && !txtDescripcion.getText().equals("")){
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
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
//---------------------------------------------------------------------- CASD
//====================================================================== CASD 
}
