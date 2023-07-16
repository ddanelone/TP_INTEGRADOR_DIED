package Modelos;

import java.time.LocalDate;
import java.util.List;

public class Ordenes {
    private int id;
    private int sucursalOrigenId;
    private int sucursalDestinoId;
    private LocalDate fechaOrden;
    private int tiempoMaximo;
    private String estado;
    private List<ProductoCantidad> listaProductos;    

    public Ordenes() {
    }

    public Ordenes(int id, int sucursalOrigenId, int sucursalDestinoId, LocalDate fechaOrden, int tiempoMaximo, String estado, List<ProductoCantidad> listaProductos) {
        this.id = id;
        this.sucursalOrigenId = sucursalOrigenId;
        this.sucursalDestinoId = sucursalDestinoId;
        this.fechaOrden = fechaOrden;
        this.tiempoMaximo = tiempoMaximo;
        this.estado = estado;
        this.listaProductos = listaProductos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSucursalOrigenId() {
        return sucursalOrigenId;
    }

    public void setSucursalOrigenId(int sucursalOrigenId) {
        this.sucursalOrigenId = sucursalOrigenId;
    }

    public int getSucursalDestinoId() {
        return sucursalDestinoId;
    }

    public void setSucursalDestinoId(int sucursalDestinoId) {
        this.sucursalDestinoId = sucursalDestinoId;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(int tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ProductoCantidad> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<ProductoCantidad> listaProductos) {
        this.listaProductos = listaProductos;
    }
    
    
    
}
