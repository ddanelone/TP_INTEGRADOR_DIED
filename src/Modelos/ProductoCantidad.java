package Modelos;

public class ProductoCantidad {
    private int id;
    private int productoId;
    private int cantidad;

    public ProductoCantidad() {
    }

    public ProductoCantidad(int id, int productoId, int cantidad) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public int getProductoId() {
        return productoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
