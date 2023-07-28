package Controladores;

import Modelos.Caminos;
import Modelos.CaminosDao;
import Modelos.Electrodomesticos;
import Modelos.ElectrodomesticosDao;
import Modelos.Ordenes;
import Modelos.OrdenesDao;
import Modelos.Stock;
import Modelos.StockDao;
import Modelos.Sucursales;
import Modelos.SucursalesDao;
import Vistas.SystemView;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControladorInicio implements ChangeListener {

    private SystemView vista;
     //Sucursales
    Sucursales sucursal = new Sucursales();
    SucursalesDao sucursalDao = new SucursalesDao();
    
    //Electrodomesticos
    Electrodomesticos electro = new Electrodomesticos();
    ElectrodomesticosDao electroDao = new ElectrodomesticosDao();
    
    //Caminos
    Caminos camino = new Caminos();
    CaminosDao caminoDao = new CaminosDao();
    
    //Stock
    Stock stock = new Stock();
    StockDao stockDao = new StockDao();
    
    //Ordenes
    Ordenes orden = new Ordenes();
    OrdenesDao ordenDao = new OrdenesDao();

    private SucursalesControlador sucursal_cuenta;
    private ElectrodomesticosControlador electrodomestico_cuenta;
    private CaminosControlador camino_cuenta;
    private StockControlador stock_cuenta;
    private OrdenesControlador ordenes_cuenta;
    private AdministrarControlador administrar_cuenta;
    private InformesControlador informe_cuenta;
    
    public ControladorInicio(SystemView vista) {
        this.vista = vista;
        
          //Controlador de Sucursales
        sucursal_cuenta = new SucursalesControlador(sucursal, sucursalDao, vista);
        sucursal_cuenta.listarTodasLasSucursales();
        //Controlador de electrodomesticos
        electrodomestico_cuenta = new ElectrodomesticosControlador(electro, electroDao, vista);
        electrodomestico_cuenta.listarTodosLosElectrodomesticos();
        //Controlador de Caminos
        camino_cuenta = new CaminosControlador(camino, caminoDao, vista);
        camino_cuenta.listarTodosLosCaminos();
        //Controlador de Stock
        stock_cuenta = new StockControlador(stock, stockDao, vista);
        stock_cuenta.listarTodosLosStock();
        //Controlador de órdenes
        ordenes_cuenta = new OrdenesControlador(orden, ordenDao, vista);
        ordenes_cuenta.listarTodasLasOrdenes();
        //Controlador de Administracion
        administrar_cuenta = new AdministrarControlador(vista);     
        administrar_cuenta.listarTodasLasSucursales();
        administrar_cuenta.listarTodosLosCaminos();
        //Controlador de informes
        informe_cuenta = new InformesControlador(vista);

        this.vista.jTabbedPane1.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int selectedIndex = vista.jTabbedPane1.getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                sucursal_cuenta.listarTodasLasSucursales();
                break;
            case 1:
                electrodomestico_cuenta.listarTodosLosElectrodomesticos();
                break;
            case 2:
                camino_cuenta.listarTodosLosCaminos();
                break;
            case 3:
                stock_cuenta.listarTodosLosStock();
                break;
            case 4:
                ordenes_cuenta.listarTodasLasOrdenes();
                break;
            case 5:
                administrar_cuenta.listarTodasLasSucursales();
                administrar_cuenta.listarTodosLosCaminos();
                break;
            default:
                break;
        }
    }
}
