package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
import org.carlosselman.bd.Conexion;
import org.carlosselman.bean.Empresa;
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
import java.util.HashMap;
import java.util.Map;
//----------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
import org.carlosselman.report.GenerarReporte;
//======================================================================= CASD
public class ServicioController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList<Empresa>listaEmpresa;
    private ObservableList<Servicio>listaServicio;
    private MainApp escenarioPrincipal;
    private DatePicker fechaServicio;
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private GridPane grpFechaServicio;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicio;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    //====================================================================== CASD
    //--------------------- Método para desactivar controles --------------- CASD
    //====================================================================== CASD
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        grpFechaServicio.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    //====================================================================== CASD
    //--------------------- Método para activar controles ------------------ CASD
    //====================================================================== CASD
    public void activarControles(){
        //txtCodigoServicio.setEditable(true);
        txtCodigoServicio.setDisable(true);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        grpFechaServicio.setDisable(false);
        //cmbCodigoEmpresa.setEditable(false);
        //cmbCodigoEmpresa.setDisable(false);
    }
    //====================================================================== CASD
    //--------------------- Método para limpiar los controles -------------- CASD
    //====================================================================== CASD
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        fechaServicio.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        cmbCodigoEmpresa.getSelectionModel().select(null);
    }
    //====================================================================== CASD
    //--------------------- Método para el botón nuevo --------------------- CASD
    //====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                cmbCodigoEmpresa.setDisable(false);
                txtCodigoServicio.setText("------------ Desactivado ------------");
                txtCodigoServicio.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR: 
                if(validacion(fechaServicio,txtTipoServicio,txtHoraServicio,
                 txtLugarServicio,txtTelefonoContacto,cmbCodigoEmpresa)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
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
    public void eliminarServicio(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
                               sp.setInt(1, ((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                               sp.execute();
                               listaServicio.remove(tblServicio.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblServicio.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Servicio eliminado con éxito");
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
    public void editarServicio(){
        switch(tipoOperacion){
            case NINGUNO:
                 btnEditar.setText("Editar");
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
                if(tblServicio.getSelectionModel().getSelectedItem() != null){
                    tblServicio.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoEmpresa.setDisable(true);
                    activarControles();
                    tipoOperacion = Operacion.ACTUALIZAR;    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                }
            break;
            case ACTUALIZAR:
            tblServicio.setDisable(true);
            tblServicio.setDisable(false);
            if(validacion(fechaServicio,txtTipoServicio,txtHoraServicio,
                txtLugarServicio,txtTelefonoContacto,cmbCodigoEmpresa)){
               actualizar();
               tblServicio.setDisable(false);
               btnEditar.setText("Editar");
               btnReporte.setText("Reporte");
               btnNuevo.setDisable(false);
               btnEliminar.setDisable(false);
               tblServicio.setDisable(false);
               tipoOperacion = Operacion.NINGUNO; 
               cargarDatos();
               limpiarControles(); 
               tblServicio.setDisable(true);
               tblServicio.setDisable(false);
               }else{
                tblServicio.setDisable(true);   
               } 
            break; 
        }
    }
    //====================================================================== CASD
    //--------------------- Método botón Guardar --------------------------- CASD
    //====================================================================== CASD
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fechaServicio.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?)}");
           procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
           procedimiento.setString(2,registro.getTipoServicio());
           procedimiento.setString(3,registro.getHoraServicio());
           procedimiento.setString(4,registro.getLugarServicio());
           procedimiento.setString(5,registro.getTelefonoContacto());
           procedimiento.setInt(6, registro.getCodigoEmpresa());
           procedimiento.execute();
           listaServicio.add(registro);
        }catch(Exception e){
                e.printStackTrace();
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
    //--------------------- Método para cargar datos ----------------------- CASD
    //====================================================================== CASD
    public void cargarDatos(){
        tblServicio.setItems(getServicio());
        colCodigoServicio.setCellValueFactory (new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory (new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory (new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory (new PropertyValueFactory<Servicio,String>("HoraServicio"));
        colLugarServicio.setCellValueFactory (new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory (new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory (new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
        cmbCodigoEmpresa.setItems(getEmpresa());
    desactivarControles();
    }
    //======================================================================================= CASD
    //-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
    //======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblServicio.getSelectionModel().getSelectedItem() != null){ 
            txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fechaServicio.selectedDateProperty().set(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtTipoServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTipoServicio());
            txtHoraServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getHoraServicio());
            txtLugarServicio.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        desactivarControles();
        }
    }
    //====================================================================== CASD
    //------------- Método para buscar los datos de Empresa ---------------- CASD
    //====================================================================== CASD
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"), 
                                        registro.getString("nombreEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    //====================================================================== CASD
    //-------------- Método para el sp_ListarEmpresas ---------------------- CASD
    //====================================================================== CASD   
    public ObservableList <Empresa> getEmpresa(){
    ArrayList <Empresa> lista = new ArrayList<Empresa>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
            resultado.getString("nombreEmpresa"),
            resultado.getString("direccion"),
            resultado.getString("telefono")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    //====================================================================== CASD
    //------------- Método para buscar los datos de Servicio --------------- CASD
    //====================================================================== CASD
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
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
    //======================================================================================= CASD
    //----- Método para actualizar los datos del modelo y del tableview ejecutando el sp ---- CASD
    //======================================================================================= CASD
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios(?,?,?,?,?,?)}");
            Servicio registro = ((Servicio) tblServicio.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            registro.setFechaServicio(fechaServicio.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            //Enviando los datos actualizados a ejecutar en el objeto sp
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setString(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoServicio());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null,"Servicio actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
      //======================================================================= CASD
    //------------- Métodos para Generar el Reporte de Empresas ------------ CASD
    //====================================================================== CASD             
       public void Reporte(){
        switch(tipoOperacion){
            case NINGUNO:
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
                if(tblServicio.getSelectionModel().getSelectedItem() != null){ 
                    imprimirReporte();
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
                }else{
                     JOptionPane.showMessageDialog(null,"Seleccione un registro para generar el reporte");
                }
            break;
            case ACTUALIZAR:
                tblServicio.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblServicio.setDisable(true);
                tblServicio.setDisable(false);
            break;
        }
    }
   //====================================================================== CASD
   //------------- Métodos para Imprimir el Reporte de Empresas ------------ CASD
   //====================================================================== CASD  
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codServicio = Integer.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        parametros.put("codServicio", codServicio);
        GenerarReporte.mostrarReporte("ReporteDetalleServicioFinal.jasper", "Reporte del Detalle del Servicio", parametros);  
    }
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD
    public boolean validacion
        (
         DatePicker fechaServicio, 
         TextField txtTipoServicio, 
         TextField txtHoraServicio, 
         TextField txtLugarServicio,
         TextField txtTelefonoContacto,
         ComboBox cmbCodigoEmpresa 
        ){
    boolean variable = false;	
    if(!(
        //-------------------------------------------------------------------------------------------- CASD
        fechaServicio.getSelectedDate()==null ||
        //-------------------------------------------------------------------------------------------- CASD
        txtTipoServicio.getText()==null || 
        txtTipoServicio.getText().trim().equals("")||
        txtTipoServicio.getText().matches(".*[0-9].*")||
        txtTipoServicio.getText().length()>150||
        //-------------------------------------------------------------------------------------------- CASD
        txtHoraServicio.getText()==null || 
        txtHoraServicio.getText().trim().equals("")||
        //txtHoraServicio.getText().matches(".*[a-zA-z]*")||
        txtHoraServicio.getText().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")==false|| 
        //-------------------------------------------------------------------------------------------- CASD
        txtLugarServicio.getText()==null || 
        txtLugarServicio.getText().trim().equals("")||
        txtLugarServicio.getText().length()>150||
        //-------------------------------------------------------------------------------------------- CASD
        txtTelefonoContacto.getText()==null || 
        txtTelefonoContacto.getText().trim().equals("")||
        //txtTelefonoContacto.getText().matches(".*[a-zA-z]*")||
        txtTelefonoContacto.getText().matches("([0-9][ -]*){8}")==false|| 
        txtTelefonoContacto.getText().length()>9||
        //-------------------------------------------------------------------------------------------- CASD
        cmbCodigoEmpresa.getSelectionModel().getSelectedItem()==null
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
        fechaServicio = new DatePicker(Locale.ENGLISH);
        fechaServicio.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaServicio.getCalendarView().todayButtonTextProperty().set("Today");
        fechaServicio.getCalendarView().setShowWeeks(false);
        fechaServicio.getStylesheets().add("org/carlosselman/resources/DatePicker.css");
        grpFechaServicio.add(fechaServicio, 0, 0);
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
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    //---------------------------------------------------------------------- CASD 
    //====================================================================== CASD
}
