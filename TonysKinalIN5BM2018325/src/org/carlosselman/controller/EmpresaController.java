package org.carlosselman.controller;
//======================================================================= CASD
//-------------------------- Importaciones  ----------------------------- CASD
//======================================================================= CASD
import org.carlosselman.system.MainApp;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bean.Empresa;
//----------------------------------------------------------------------- CASD
import org.carlosselman.bd.Conexion;
//----------------------------------------------------------------------- CASD
import org.carlosselman.report.GenerarReporte;
//----------------------------------------------------------------------- CASD
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
import java.util.HashMap;
import java.util.Map;
//---------------------------------------------------------------------- CASD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//---------------------------------------------------------------------- CASD
import java.net.URL;
//---------------------------------------------------------------------- CASD
import javax.swing.JOptionPane;
//====================================================================== CASD
public class EmpresaController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO; 
    private ObservableList<Empresa>listaEmpresa;
    private MainApp escenarioPrincipal;
    @FXML private TextField txtCodigoEmpresa, txtNombreEmpresa, txtDireccion, txtTelefono;
    @FXML private Button btnNuevo, btnEditar, btnEliminar, btnReporte,btnReporte1;
    @FXML private TableView tblEmpresa;
    @FXML private TableColumn colCodigoEmpresa, colNombreEmpresa, colDireccion, colTelefono;
//====================================================================== CASD
//--------------------- Método para desactivar controles --------------- CASD
//====================================================================== CASD   
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }
//====================================================================== CASD
//--------------------- Método para activar controles ------------------ CASD
//====================================================================== CASD
    public void activarControles(){
        //txtCodigoEmpresa.setEditable(true);
        txtCodigoEmpresa.setDisable(true);
        txtNombreEmpresa.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }
//====================================================================== CASD
//--------------------- Método para limpiar los controles -------------- CASD
//====================================================================== CASD
    public void limpiarControles(){
        txtCodigoEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
    }
//====================================================================== CASD
//--------------------- Método para el botón nuevo --------------------- CASD
//====================================================================== CASD
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                txtCodigoEmpresa.setText("--------------------------------- Desactivado ------------------------------------");
                txtCodigoEmpresa.setDisable(true);
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                btnReporte1.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
            break;
            case GUARDAR:
                if(validacion(txtNombreEmpresa,txtDireccion,txtTelefono)){
                guardar();
                limpiarControles();
                desactivarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnReporte1.setDisable(false);
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                cargarDatos();
                }
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método para eliminar Empresas ------------------ CASD
//====================================================================== CASD
    public void eliminarEmpresa(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnReporte1.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                limpiarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
            break;
            default:
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblEmpresa.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¡Esta acción puede repercutir en los datos de otras entidades! "
                        + "\n                     ¿Está seguro de eliminar el registro?","Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                           try{
                               PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
                               sp.setInt(1, ((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                               sp.execute();
                               listaEmpresa.remove(tblEmpresa.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               tblEmpresa.getSelectionModel().clearSelection();
                               JOptionPane.showMessageDialog(null,"Empresa eliminada con éxito");
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
//--------------------- Método para editar Empresas -------------------- CASD
//====================================================================== CASD
    public void editarEmpresa(){
        switch(tipoOperacion){
            case NINGUNO:
                btnEditar.setText("Editar");
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                if(tblEmpresa.getSelectionModel().getSelectedItem() != null){
                    tblEmpresa.setDisable(true);
                    btnReporte1.setDisable(true);
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
               tblEmpresa.setDisable(true);
               tblEmpresa.setDisable(false);
               if(validacion(txtNombreEmpresa,txtDireccion,txtTelefono)){
               actualizar();
                tblEmpresa.setDisable(false);
                btnReporte1.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte Por Empresa");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
               }else{
                tblEmpresa.setDisable(true);   
               } 
            break;
        }
    }
//====================================================================== CASD
//--------------------- Método botón Guardar --------------------------- CASD
//====================================================================== CASD
    public void guardar(){
        Empresa empresaNueva = new Empresa();
        empresaNueva.setNombreEmpresa(txtNombreEmpresa.getText());
        empresaNueva.setDireccion(txtDireccion.getText());
        empresaNueva.setTelefono(txtTelefono.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?)}");
           sp.setString(1,empresaNueva.getNombreEmpresa());
           sp.setString(2,empresaNueva.getDireccion());
           sp.setString(3,empresaNueva.getTelefono());
           sp.execute();
           listaEmpresa.add(empresaNueva);
        }catch(Exception e){
                e.printStackTrace();
        }
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
//--------------------- Método para cargar datos ----------------------- CASD
//====================================================================== CASD
    public void cargarDatos(){
        tblEmpresa.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory (new PropertyValueFactory<Empresa,Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory (new PropertyValueFactory<Empresa,String>("nombreEmpresa"));
        colDireccion.setCellValueFactory (new PropertyValueFactory<Empresa,String>("direccion"));
        colTelefono.setCellValueFactory (new PropertyValueFactory<Empresa,String>("telefono"));
    desactivarControles();
    }
//======================================================================================= CASD
//-- Método para seleccionar elementos de la tabla y mostrarlos en los campos de texto -- CASD
//======================================================================================= CASD
    public void seleccionarElementos(){
        if(tblEmpresa.getSelectionModel().getSelectedItem() != null){
             txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
             txtNombreEmpresa.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getNombreEmpresa());
             txtDireccion.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getDireccion());
             txtTelefono.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getTelefono());
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
//======================================================================================= CASD
//----- Método para actualizar los datos del modelo y del tableview ejecutando el sp ---- CASD
//======================================================================================= CASD
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresas(?,?,?,?)}");
            Empresa empresaActualizada = ((Empresa) tblEmpresa.getSelectionModel().getSelectedItem());
            //Obteniendo los datos de la vista al modelo en java
            empresaActualizada.setNombreEmpresa(txtNombreEmpresa.getText());
            empresaActualizada.setDireccion(txtDireccion.getText());
            empresaActualizada.setTelefono(txtTelefono.getText());
            //Enviando los datos actualizados a ejecutar en el objeto sp
            sp.setString(1, empresaActualizada.getNombreEmpresa());
            sp.setString(2, empresaActualizada.getDireccion());
            sp.setString(3, empresaActualizada.getTelefono());
            sp.setInt(4, empresaActualizada.getCodigoEmpresa());
            sp.execute();
            JOptionPane.showMessageDialog(null,"Empresa actualizada con éxito");
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
   //======================================================================= CASD
    //------------- Métodos para Generar el Reporte de Empresas ------------ CASD
    //====================================================================== CASD             
    public void generarReporteGeneral(){
        switch(tipoOperacion){
            case NINGUNO:
                imprimirReporteGeneral();       
            break; 
            case ACTUALIZAR:
                tblEmpresa.setDisable(false);
                btnReporte1.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte Por Empresa");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
            break;
        }
    }
       public void generarReportePorEmpresa(){
        switch(tipoOperacion){
            case NINGUNO:
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                if(tblEmpresa.getSelectionModel().getSelectedItem() != null){ 
                    imprimirReportePorEmpresa();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
                }else{
                     JOptionPane.showMessageDialog(null,"Seleccione un registro para generar el reporte");
                }
            break;
            case ACTUALIZAR:
                tblEmpresa.setDisable(false);
                btnReporte1.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte Por Empresa");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                limpiarControles();
                tblEmpresa.setDisable(true);
                tblEmpresa.setDisable(false);
            break;
        }
    }
   //====================================================================== CASD
   //------------- Métodos para Imprimir el Reporte de Empresas ------------ CASD
   //====================================================================== CASD  
    public void imprimirReporteGeneral(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpleado",null);
        GenerarReporte.mostrarReporte
        ("ReporteEmpresaGeneral.jasper", "Reporte General de las Empresas ", parametros);
    }
    public void imprimirReportePorEmpresa(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReporteEmpresaPorEmpresa.jasper", "Reporte Por Empresa", parametros);  
    }
    //====================================================================== CASD
    //---------------------- Método de Validación -------------------------- CASD
    //====================================================================== CASD 
    public boolean validacion
        (
         TextField txtNombreEmpresa, 
         TextField txtDireccion, 
         TextField txtTelefono
        ){
    boolean variable = false;	
    if(!(
        //------------------------------------------------------------------ CASD
        txtNombreEmpresa.getText()==null || 
        txtNombreEmpresa.getText().trim().equals("")||
        txtNombreEmpresa.getText().matches(".*[0-9].*")||
        txtNombreEmpresa.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtDireccion.getText()==null || 
        txtDireccion.getText().trim().equals("")||
        txtDireccion.getText().length()>150||
        //------------------------------------------------------------------ CASD
        txtTelefono.getText()==null || 
        txtTelefono.getText().trim().equals("")||
        //txtTelefono.getText().matches(".*[a-zA-z]*")||
        txtTelefono.getText().matches("([0-9][ -]*){8}")==false|| 
        txtTelefono.getText().length()>9
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
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    //---------------------------------------------------------------------- CASD
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    //---------------------------------------------------------------------- CASD 
    //====================================================================== CASD
}
