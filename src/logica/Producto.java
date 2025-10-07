package logica;

public abstract class  Producto implements Vendible{
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }



    public abstract String mostrarDetalles();

    public abstract double calcularPrecio();
    @Override
    public boolean vender(int cantidad) {
        if (estaDisponible(cantidad)){
            stock=stock-cantidad;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void reponer(int cantidad) {
        stock=stock+cantidad;

    }

    @Override
    public boolean estaDisponible(int cantidad) {
        if (cantidad>stock)
            return false;
        else
            return true;
    }
}
