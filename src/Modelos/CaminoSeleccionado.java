package Modelos;

public class CaminoSeleccionado {
    private int id;
    private int orden_provision_id;
    private int sucursal_origen_id;
    private int sucursal_destino_id;
    private String camino;
    private int tiempo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrden_provision_id() {
        return orden_provision_id;
    }

    public void setOrden_provision_id(int orden_provision_id) {
        this.orden_provision_id = orden_provision_id;
    }

    public int getSucursal_origen_id() {
        return sucursal_origen_id;
    }

    public void setSucursal_origen_id(int sucursal_origen_id) {
        this.sucursal_origen_id = sucursal_origen_id;
    }

    public int getSucursal_destino_id() {
        return sucursal_destino_id;
    }

    public void setSucursal_destino_id(int sucursal_destino_id) {
        this.sucursal_destino_id = sucursal_destino_id;
    }

    public String getCamino() {
        return camino;
    }

    public void setCamino(String camino) {
        this.camino = camino;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public CaminoSeleccionado(int id, int orden_provision_id, int sucursal_origen_id, int sucursal_destino_id, String camino, int tiempo) {
        this.id = id;
        this.orden_provision_id = orden_provision_id;
        this.sucursal_origen_id = sucursal_origen_id;
        this.sucursal_destino_id = sucursal_destino_id;
        this.camino = camino;
        this.tiempo = tiempo;
    }

    public CaminoSeleccionado() {
    }
    
}
