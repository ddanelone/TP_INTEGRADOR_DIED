package Modelos;

public class Caminos {
    private int id;
    private int origenId;
    private int destinoId;
    private int capacidad;
    private int tiempo;
    private boolean operativo;
    private String observaciones;

    public Caminos() {
    }

    public Caminos(int id, int origenId, int destinoId, int capacidad, int tiempo, boolean operativo, String observaciones) {
        this.id = id;
        this.origenId = origenId;
        this.destinoId = destinoId;
        this.capacidad = capacidad;
        this.tiempo = tiempo;
        this.operativo = operativo;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigenId() {
        return origenId;
    }

    public void setOrigenId(int origenId) {
        this.origenId = origenId;
    }

    public int getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(int destinoId) {
        this.destinoId = destinoId;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isOperativo() {
        return operativo;
    }

    public void setOperativo(boolean operativo) {
        this.operativo = operativo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
}
