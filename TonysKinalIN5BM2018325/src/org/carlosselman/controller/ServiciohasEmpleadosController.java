package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.Empleado;
import org.carlosselman.bean.ServicioshasEmpleados;
import org.carlosselman.bean.Servicio;
//----------------------------------------------------------------------- CASD
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
//----------------------------------------------------------------------- CASD
import eu.schudt.javafx.controls.calendar.DatePicker;
//----------------------------------------------------------------------- CASD
import java.text.SimpleDateFormat;
//----------------------------------------------------------------------- CASD
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//----------------------------------------------------------------------- CASD
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
//----------------------------------------------------------------------- CASD
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//----------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//----------------------------------------------------------------------- CASD
import java.net.URL;
//----------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
//======================================================================= CASD
public class ServiciohasEmpleadosController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList<ServicioshasEmpleados>listaServicioshasEmpleados;
    private ObservableList<Servicio>listaServicio;
    private ObservableList<Empleado>listaEmpleado;
    private MainApp escenarioPrincipal;
    private DatePicker fechaEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private GridPane grpFechaEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TableView tblServicioshasEmpleados;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    //====================================================================== CASD
    //--------------------- Método para desactivar controles --------------- CASD
    //====================================================================== CASD
    public void desactivarControles(){
        txtLugarEvento.setEditable(false);
        txtHoraEvento.setEditable(false);
        grpFechaEvento.setDisable(true);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setEditable(false);
        cmbCodigoEmpleado.setDisable(true);
    }
    //====================================================================== CASD
    //--------------------- Método para activar controles ------------------ CASD
    //====================================================================== CASD
    public void activarControles(){
        txtLugarEvento.setEditable(true);
        txtHoraEvento.setEditable(true);
        grpFechaEvento.setDisable(false);
        //cmbCodigoServicio.setEditable(false);
        //cmbCodigoServicio.setDisable(true);
        //cmbCodigoEmpleado.setEditable(false);
        //cmbCodigoEmpleado.setDisable(true);
    }
    //====================================================================== CASD
    //--------------------- Método para limpiar los controles -------------- CASD
    //====================================================================== CASD
    public void limpiarControles(){
        txtLugarEvento.setText("");
        txtHoraEvento.setText("");
        fechaEvento.selectedDateProperty().set(null);
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoServicio.getSelectionModel().select(null);
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().select(null);
    }
    //====================================================================== CASD
    //--------------------- Método para el botón nuevo --------------------- CASD
    //====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                cmbCodigoServicio.setDisable(false);
                cmbCodigoEmpleado.setDisable(false);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR: 
                if(validacion(fechaEvento,txtHoraEvento,txtLugarEvento,
                cmbCodigoServicio,cmbCodigoEmpleado)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
              }
            break;
        }
    }
    //====================================================================== CASD
    //--------------------- Método para eliminar Servicio ------------------ CASD
    //====================================================================== CASD
    public void eliminarServicioHasEmpleados(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                tblServicioshasEmpleados.setDisable(false);
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
            break;
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblServicioshasEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Servicio has Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                               sp.setInt(1, ((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio());
                               sp.execute();
                               listaServicioshasEmpleados.remove(tblServicioshasEmpleados.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblServicioshasEmpleados.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Registro eliminado con éxito");
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
    //--------------------- Método para editar Servicio -------------------- CASD
    //====================================================================== CASD
    public void editarServicioHasEmpleados(){
        switch(tipoOperacion){
            case NINGUNO:
                 btnEditar.setText("Editar");
                tblServicioshasEmpleados.setDisable(true);
                tblServicioshasEmpleados.setDisable(false);
                if(validacion(fechaEvento,txtHoraEvento,txtLugarEvento,
                cmbCodigoServicio,cmbCodigoEmpleado)){
                    tblServicioshasEmpleados.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(false);
                    cmbCodigoServicio.setDisable(true);
                    cmbCodigoEmpleado.setDisable(true);
                    activarControles();
                    tipoOperacion = Operacion.ACTUALIZAR;    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                }
            break;
            case ACTUALIZAR:
            tblServicioshasEmpleados.setDisable(true);
            tblServicioshasEmpleados.setDisable(false);
            if(validacion(fechaEvento,txtHoraEvento,txtLugarEvento,
                cmbCodigoServicio,cmbCodigoEmpleado)){
               actualizar();
               tblServicioshasEmpleados.setDisable(false);
               btnEditar.setText("Editar");
               btnNuevo.setDisable(false);
               btnEliminar.setDisable(false);
               btnEliminar.setText("Eliminar");
               tipoOperacion = Operacion.NINGUNO; 
               cargarDatos();
               limpiarControles(); 
               tblServicioshasEmpleados.setDisable(true);
               tblServicioshasEmpleados.setDisable(false);
               }else{
                tblServicioshasEmpleados.setDisable(true);   
               } 
            break; 
        }
    }
    //====================================================================== CASD
    //--------------------- Método botón Guardar --------------------------- CASD
    //====================================================================== CASD
    public void guardar(){
        ServicioshasEmpleados registro = new ServicioshasEmpleados();
        registro.setFechaEvento(fechaEvento.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
           procedimiento.setInt(1, registro.getCodigoServicio());
           procedimiento.setInt(2, registro.getCodigoEmpleado());
           procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
           procedimiento.setString(4,registro.getHoraEvento());
           procedimiento.setString(5,registro.getLugarEvento());
           procedimiento.execute();
           listaServicioshasEmpleados.add(registro);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    //====================================================================== CASD
    //--------- Método para el sp_ListarServicios_has_Empleados ------------ CASD
    //====================================================================== CASD   
    public ObservableList <ServicioshasEmpleados> getServicioshasEmpleados(){
    ArrayList <ServicioshasEmpleados> lista = new ArrayList<ServicioshasEmpleados>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ServicioshasEmpleados(resultado.getInt("codigoServicio"),
            resultado.getInt("codigoEmpleado"),
            resultado.getDate("fechaEvento"),
            resultado.getString("horaEvento"),
            resultado.getString("lugarEvento")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaServicioshasEmpleados = FXCollections.observableArrayList(lista);
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
        tblServicioshasEmpleados.setItems(getServicioshasEmpleados());
        colFechaEvento.setCellValueFactory (new PropertyValueFactory<ServicioshasEmpleados,Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory (new PropertyValueFactory<ServicioshasEmpleados,String>("horaEvento"));
        colLugarEvento.setCellValueFactory (new PropertyValueFactory<ServicioshasEmpleados,String>("lugarEvento"));
        colCodigoServicio.setCellValueFactory (new PropertyValueFactory<ServicioshasEmpleados,Integer>("codigoServicio"));
        cmbCodigoServicio.setItems(getServicio());
        colCodigoEmpleado.setCellValueFactory (new PropertyValueFactory<ServicioshasEmpleados,Integer>("codigoEmpleado"));
        cmbCodigoEmpleado.setItems(getEmpleado());
    desactivarControles();
    }
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblServicioshasEmpleados.getSelectionModel().getSelectedItem() != null){ 
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            fechaEvento.selectedDateProperty().set(((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            txtHoraEvento.setText(((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
            txtLugarEvento.setText(((ServicioshasEmpleados)tblServicioshasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()); 
        desactivarControles();
        }
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
    //------------- Método para buscar los datos de Empleado --------------- CASD
    //====================================================================== CASD
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                        registro.getInt("numeroEmpleado"),
                                        registro.getString("apellidosEmpleado"), 
                                        registro.getString("nombresEmpleado"),
                                        registro.getString("direccionEmpleado"),
                                        registro.getString("telefonoContacto"),
                                        registro.getString("gradoCocinero"),
                                        registro.getInt("codigoTipoEmpleado")
            );
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?,?)}");
            ServicioshasEmpleados registro = ((ServicioshasEmpleados) tblServicioshasEmpleados.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            registro.setFechaEvento(fechaEvento.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            //Enviando los datos actualizados a ejecutar en el objeto sp
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(2, registro.getHoraEvento());
            procedimiento.setString(3, registro.getLugarEvento());
            procedimiento.setInt(4, registro.getCodigoServicio());
            procedimiento.setInt(5, registro.getCodigoEmpleado());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null,"Registro actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD
    public boolean validacion
        (
         DatePicker fechaEvento, 
         TextField txtHoraEvento,
         TextField txtLugarEvento,
         ComboBox cmbCodigoServicio,
         ComboBox cmbCodigoEmpleado 
        ){
    boolean variable = false;	
    if(!(
        //-------------------------------------------------------------------------------------------- CASD
        fechaEvento.getSelectedDate()==null ||
        //-------------------------------------------------------------------------------------------- CASD
        txtHoraEvento.getText()==null || 
        txtHoraEvento.getText().trim().equals("")||
        //txtHoraEvento.getText().matches(".*[a-zA-z]*")||
        txtHoraEvento.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")==false|| 
        //-------------------------------------------------------------------------------------------- CASD
        txtLugarEvento.getText()==null || 
        txtLugarEvento.getText().trim().equals("")||
        txtLugarEvento.getText().length()>150||
        //-------------------------------------------------------------------------------------------- CASD
        cmbCodigoServicio.getSelectionModel().getSelectedItem()==null||
        //-------------------------------------------------------------------------------------------- CASD
        cmbCodigoEmpleado.getSelectionModel().getSelectedItem()==null
        //-------------------------------------------------------------------------------------------- CASD
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
        fechaEvento = new DatePicker(Locale.ENGLISH);
        fechaEvento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaEvento.getCalendarView().todayButtonTextProperty().set("Today");
        fechaEvento.getCalendarView().setShowWeeks(false);
        fechaEvento.getStylesheets().add("org/carlosselman/resources/DatePicker.css");
        grpFechaEvento.add(fechaEvento, 0, 0);
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
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    //---------------------------------------------------------------------- CASD
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    //---------------------------------------------------------------------- CASD 
    //====================================================================== CASD
}
