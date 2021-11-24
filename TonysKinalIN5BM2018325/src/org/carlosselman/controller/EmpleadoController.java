package org.carlosselman.controller;
//======================================================================= CASD
//-------------------------- Importaciones  ----------------------------- CASD
//======================================================================= CASD
import org.carlosselman.system.MainApp;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Empleado;
import org.carlosselman.bean.TipoEmpleado;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bd.Conexion;
//----------------------------------------------------------------------- CASD
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
//----------------------------------------------------------------------- CASD
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//----------------------------------------------------------------------- CASD
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
//----------------------------------------------------------------------- CASD
import java.util.ResourceBundle;
import java.util.ArrayList;
//----------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//----------------------------------------------------------------------- CASD
import java.net.URL;
//----------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
//======================================================================= CASD
public class EmpleadoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<TipoEmpleado>listaTipoEmpleado;
    private ObservableList<Empleado>listaEmpleado;
    private MainApp escenarioPrincipal;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private TableView tblEmpleado;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado; 
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    //====================================================================== CASD
    //--------------------- Método para desactivar controles --------------- CASD
    //====================================================================== CASD
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    //====================================================================== CASD
    //--------------------- Método para activar controles ------------------ CASD
    //====================================================================== CASD
    public void activarControles(){
        //txtCodigoEmpleado.setEditable(true);
        txtCodigoEmpleado.setDisable(true);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        //cmbCodigoTipoEmpleado.setEditable(false);
        //cmbCodigoTipoEmpleado.setDisable(false);
    }
    //====================================================================== CASD
    //--------------------- Método para limpiar los controles -------------- CASD
    //====================================================================== CASD
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombresEmpleado.setText("");
        txtDireccionEmpleado.setText("");
        txtTelefonoContacto.setText("");
        txtGradoCocinero.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoTipoEmpleado.getSelectionModel().select(null);
    }
    //====================================================================== CASD
    //--------------------- Método para el botón nuevo --------------------- CASD
    //====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                cmbCodigoTipoEmpleado.setDisable(false);
                txtCodigoEmpleado.setText("------------ Desactivado ------------");
                txtCodigoEmpleado.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR:
                if(validacion(txtNumeroEmpleado,txtApellidosEmpleado,
                   txtNombresEmpleado,txtDireccionEmpleado,
                   txtTelefonoContacto,txtGradoCocinero,cmbCodigoTipoEmpleado)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
    //====================================================================== CASD
    //--------------------- Método para eliminar Empleado ------------------ CASD
    //====================================================================== CASD
    public void eliminarEmpleado(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblEmpleado.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
            break;
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                               sp.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                               sp.execute();
                               listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblEmpleado.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Empleado eliminado con éxito");
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
    //--------------------- Método para editar Empleado -------------------- CASD
    //====================================================================== CASD   
    public void editarEmpleado(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoTipoEmpleado.setDisable(true);
                    tblEmpleado.setDisable(true);
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
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
               if(validacion(txtNumeroEmpleado,txtApellidosEmpleado,
                  txtNombresEmpleado,txtDireccionEmpleado,
                  txtTelefonoContacto,txtGradoCocinero,cmbCodigoTipoEmpleado)){
                actualizar();
                tblEmpleado.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblEmpleado.setDisable(true);
                tblEmpleado.setDisable(false);
               }else{
                tblEmpleado.setDisable(true);   
               } 
            break;
        }
    }
    
    //====================================================================== CASD
    //--------------------- Método botón Guardar --------------------------- CASD
    //====================================================================== CASD
    public void guardar(){
        Empleado empleadoNuevo = new Empleado();
        empleadoNuevo.setNumeroEmpleado(Integer.valueOf(txtNumeroEmpleado.getText()));
        empleadoNuevo.setApellidosEmpleado(txtApellidosEmpleado.getText()); 
        empleadoNuevo.setNombresEmpleado(txtNombresEmpleado.getText());
        empleadoNuevo.setDireccionEmpleado(txtDireccionEmpleado.getText());
        empleadoNuevo.setTelefonoContacto(txtTelefonoContacto.getText());
        empleadoNuevo.setGradoCocinero(txtGradoCocinero.getText()); 
        empleadoNuevo.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
           sp.setInt(1,empleadoNuevo.getNumeroEmpleado());
           sp.setString(2,empleadoNuevo.getApellidosEmpleado());
           sp.setString(3,empleadoNuevo.getNombresEmpleado());
           sp.setString(4,empleadoNuevo.getDireccionEmpleado());
           sp.setString(5,empleadoNuevo.getTelefonoContacto());
           sp.setString(6,empleadoNuevo.getGradoCocinero());
           sp.setInt(7,empleadoNuevo.getCodigoTipoEmpleado());
           sp.execute();
           listaEmpleado.add(empleadoNuevo);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarEmpleados --------------------- CASD
    //====================================================================== CASD   
    public ObservableList <Empleado> getEmpleado(){
    ArrayList <Empleado> lista = new ArrayList<Empleado>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
            resultado.getInt("numeroEmpleado"),
            resultado.getString("apellidosEmpleado"), 
            resultado.getString("nombresEmpleado"),
            resultado.getString("direccionEmpleado"), 
            resultado.getString("telefonoContacto"), 
            resultado.getString("gradoCocinero"),
            resultado.getInt("codigoTipoEmpleado")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //--------------------- Método para cargar datos ----------------------- CASD
    //====================================================================== CASD
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory (new PropertyValueFactory<Empleado,String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory (new PropertyValueFactory<Empleado,String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory (new PropertyValueFactory<Empleado,Integer>("codigoTipoEmpleado"));
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    desactivarControles();
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
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
            txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtApellidosEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtNombresEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtDireccionEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefonoContacto.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocinero.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
         desactivarControles();
        }
    }      	
    //====================================================================== CASD
    //------------- Método para buscar los datos de TipoEmpleado ----------- CASD
    //====================================================================== CASD
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
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?)}");
            Empleado empleadoActualizado = ((Empleado) tblEmpleado.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            empleadoActualizado.setNumeroEmpleado(Integer.valueOf(txtNumeroEmpleado.getText()));
            empleadoActualizado.setApellidosEmpleado(txtApellidosEmpleado.getText()); 
            empleadoActualizado.setNombresEmpleado(txtNombresEmpleado.getText());
            empleadoActualizado.setDireccionEmpleado(txtDireccionEmpleado.getText());
            empleadoActualizado.setTelefonoContacto(txtTelefonoContacto.getText());
            empleadoActualizado.setGradoCocinero(txtGradoCocinero.getText()); 
            //Enviando los datos actualizados a ejecutar en el objeto sp
            sp.setInt(1,empleadoActualizado.getNumeroEmpleado());
            sp.setString(2,empleadoActualizado.getApellidosEmpleado());
            sp.setString(3,empleadoActualizado.getNombresEmpleado());
            sp.setString(4,empleadoActualizado.getDireccionEmpleado());
            sp.setString(5,empleadoActualizado.getTelefonoContacto());
            sp.setString(6,empleadoActualizado.getGradoCocinero());
            sp.setInt(7,empleadoActualizado.getCodigoEmpleado());
            sp.execute();
            JOptionPane.showMessageDialog(null,"Empleado actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    } 
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD
    public boolean validacion
        (
         TextField txtNumeroEmpleado, 
         TextField txtApellidosEmpleado, 
         TextField txtNombresEmpleado, 
         TextField txtDireccionEmpleado, 
         TextField txtTelefonoContacto, 
         TextField txtGradoCocinero, 
         ComboBox cmbCodigoTipoEmpleado
        ){
    boolean variable = false;	
    if(!(
        //------------------------------------------------------------------ CASD
        txtNumeroEmpleado.getText()==null || 
        txtNumeroEmpleado.getText().trim().equals("")||
        txtNumeroEmpleado.getText().matches(".*[a-zA-z].*")||
        txtNumeroEmpleado.getText().length()>11||
        //------------------------------------------------------------------ CASD
        txtApellidosEmpleado.getText()==null || 
        txtApellidosEmpleado.getText().trim().equals("")||
        txtApellidosEmpleado.getText().matches(".*[0-9].*")||
        txtApellidosEmpleado.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtNombresEmpleado.getText()==null || 
        txtNombresEmpleado.getText().trim().equals("")||
        txtNombresEmpleado.getText().matches(".*[0-9].*")||
        txtNombresEmpleado.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtDireccionEmpleado.getText()==null || 
        txtDireccionEmpleado.getText().trim().equals("")||
        txtDireccionEmpleado.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtTelefonoContacto.getText()==null || 
        txtTelefonoContacto.getText().trim().equals("")||
        //txtTelefonoContacto.getText().matches(".*[a-zA-z]*")||
        txtTelefonoContacto.getText().matches("([0-9][ -]*){8}")==false|| 
        txtTelefonoContacto.getText().length()>9||
        //------------------------------------------------------------------ CASD
        txtGradoCocinero.getText()==null || 
        txtGradoCocinero.getText().trim().equals("")||
        txtGradoCocinero.getText().matches(".*[0-9].*")||
        txtGradoCocinero.getText().length()>50||
        //------------------------------------------------------------------ CASD
        cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()==null
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
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    //---------------------------------------------------------------------- CASD
    //====================================================================== CASD
}
