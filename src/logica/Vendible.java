package logica;

public interface Vendible {
    public boolean vender(int cantidad);
    public void reponer(int cantidad);
    public boolean estaDisponible(int cantidad);
}
