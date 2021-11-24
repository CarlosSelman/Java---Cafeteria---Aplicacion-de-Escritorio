package org.carlosselman.controller;
//====================================================================== CASD
//-------------------------- Importaciones  ---------------------------- CASD
//====================================================================== CASD
import org.carlosselman.system.MainApp;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bd.Conexion;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Empresa;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Presupuesto;
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
import java.util.HashMap;
import java.util.Map;
//----------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//----------------------------------------------------------------------- CASD
import java.net.URL;
//----------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
import org.carlosselman.report.GenerarReporte;
//======================================================================= CASD
public class PresupuestoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList<Empresa>listaEmpresa;
    private ObservableList<Presupuesto>listaPresupuesto;
    private MainApp escenarioPrincipal;
    private DatePicker fechaSolicitud;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFechaSolicitud;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
//====================================================================== CASD
//--------------------- Método para desactivar controles --------------- CASD
//====================================================================== CASD
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
//====================================================================== CASD
//--------------------- Método para activar controles ------------------ CASD
//====================================================================== CASD
    public void activarControles(){
        //txtCodigoPresupuesto.setEditable(true);
        txtCodigoPresupuesto.setDisable(true);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
    }
//====================================================================== CASD
//--------------------- Método para limpiar los controles -------------- CASD
//====================================================================== CASD
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
        fechaSolicitud.selectedDateProperty().set(null);
        //cmbCodigoEmpresa.getSelectionModel().clearSelection();
        //cmbCodigoEmpresa.getSelectionModel().select(null);
    }
//====================================================================== CASD
//--------------------- Método para el botón nuevo --------------------- CASD
//====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                cmbCodigoEmpresa.setDisable(false);
                txtCodigoPresupuesto.setText("--- Desactivado ---");
                txtCodigoPresupuesto.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR:
                if(validacion(fechaSolicitud,txtCantidadPresupuesto,cmbCodigoEmpresa)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    } 
//====================================================================== CASD
//--------------------- Método para eliminar Presupuesto --------------- CASD
//====================================================================== CASD
    public void eliminarPresupuesto(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                               sp.setInt(1, ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                               sp.execute();
                               listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblPresupuesto.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Presupuesto eliminado con éxito");
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
//--------------------- Método para editar Presupuesto ----------------- CASD
//====================================================================== CASD
    public void editarPresupuesto(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    cmbCodigoEmpresa.setDisable(true);
                    tblPresupuesto.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = Operacion.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                }
            break;
            case ACTUALIZAR:
               tblPresupuesto.setDisable(true);
               tblPresupuesto.setDisable(false);
               if(validacion(fechaSolicitud,txtCantidadPresupuesto,cmbCodigoEmpresa)){
                actualizar();
                tblPresupuesto.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte Por Empresa");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
               }else{
                tblPresupuesto.setDisable(true);   
               } 
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método botón Guardar --------------------------- CASD
//====================================================================== CASD 
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fechaSolicitud.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
           procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
           procedimiento.setDouble(2, registro.getCantidadPresupuesto());
           procedimiento.setInt(3, registro.getCodigoEmpresa());
           procedimiento.execute();
           listaPresupuesto.add(registro);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
//====================================================================== CASD
//-------------- Método para el sp_ListarPresupuesto --------------------- CASD
//====================================================================== CASD    
    public ObservableList <Presupuesto> getPresupuesto(){
    ArrayList <Presupuesto> lista = new ArrayList<Presupuesto>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
            resultado.getDate("fechaSolicitud"),
            resultado.getDouble("cantidadPresupuesto"),
            resultado.getInt("codigoEmpresa")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaPresupuesto = FXCollections.observableArrayList(lista);
}
//====================================================================== CASD
//--------------------- Método para cargar datos ----------------------- CASD
//====================================================================== CASD   
    public void cargarDatos(){
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory (new PropertyValueFactory<Presupuesto,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory (new PropertyValueFactory<Presupuesto,Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory (new PropertyValueFactory<Presupuesto,Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory (new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
        cmbCodigoEmpresa.setItems(getEmpresa());
    desactivarControles();
}
//======================================================================================= CASD
//-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
//======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){   
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fechaSolicitud.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
//======================================================================================= CASD
//----- Método para actualizar los datos del modelo y del tableview ejecutando el sp ---- CASD
//======================================================================================= CASD
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");
            Presupuesto registro = ((Presupuesto) tblPresupuesto.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            registro.setFechaSolicitud(fechaSolicitud.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            //Enviando los datos actualizados a ejecutar en el objeto sp
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoPresupuesto());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null,"Presupuesto actualizado con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
    //====================================================================== CASD
    //------------- Métodos para Generar el Reporte de Empresas ------------ CASD
    //====================================================================== CASD             
    public void generarReportePorEmpresa(){
        switch(tipoOperacion){
            case NINGUNO:
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){ 
                    imprimirReporte();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
                }else{
                     JOptionPane.showMessageDialog(null,"Seleccione un registro para generar el reporte");
                }
            break;
            case ACTUALIZAR:
                tblPresupuesto.setDisable(false);    
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte Por Empresa");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblPresupuesto.setDisable(true);
                tblPresupuesto.setDisable(false);
            break;
        }
    }
    //====================================================================== CASD
    //------------- Métodos para Imprimir el Reporte de Empleados ---------- CASD
    //====================================================================== CASD  
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte del Presupuesto", parametros);  
    }
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD
    public boolean validacion
        (
         DatePicker fechaSolicitud, 
         TextField txtCantidadPresupuesto, 
         ComboBox cmbCodigoEmpresa 
        ){
    boolean variable = false;	
    if(!(
        //------------------------------------------------------------------ CASD
        fechaSolicitud.getSelectedDate()==null ||
        //------------------------------------------------------------------ CASD
        txtCantidadPresupuesto.getText()==null ||
        txtCantidadPresupuesto.getText().trim().equals("")||
        txtCantidadPresupuesto.getText().matches("\\d+(\\.\\d{1,2})?")==false|| 
        txtCantidadPresupuesto.getText().matches(".*[a-zA-z].*")||
        txtCantidadPresupuesto.getText().length()>11||
        //------------------------------------------------------------------ CASD
        cmbCodigoEmpresa.getSelectionModel().getSelectedItem()==null
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
        fechaSolicitud = new DatePicker(Locale.ENGLISH);
        fechaSolicitud.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaSolicitud.getCalendarView().todayButtonTextProperty().set("Today");
        fechaSolicitud.getCalendarView().setShowWeeks(false);
        fechaSolicitud.getStylesheets().add("org/carlosselman/resources/DatePicker.css");
        grpFechaSolicitud.add(fechaSolicitud, 0, 0);
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
