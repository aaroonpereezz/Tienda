package logica;

public class Electronico extends Producto {
    final  double IVA_ELECTRONICO=0.21;
    private String marca;
    private int garantia;



    public Electronico(int id, String nombre, double precio, int cantidad, String marca, int garantia) {
        super(id, nombre, precio, cantidad);
        this.marca = marca;
        this.garantia = garantia;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    @Override
    public String mostrarDetalles() {

        return "Electronico{" +
                "id="+getId()+
                ", nombre='" + getNombre() + '\'' +
                ", precio=" + getPrecio() +
                ", cantidad=" + getStock() +
                ", marca='" + marca + '\'' +
                ", garantia=" + garantia +
                '}';
    }



    @Override
    public double calcularPrecio() {

        return getPrecio()* getStock()*IVA_ELECTRONICO;
    }


}
