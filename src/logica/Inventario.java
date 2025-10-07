package logica;

import java.io.*;
import java.util.ArrayList;

public class Inventario {
    private ArrayList<Producto> listaProductos;
    public final String RUTA_FICHERO_DATOS = "productos.txt";

    public Inventario() throws IOException {
        listaProductos=new ArrayList<>();
        leerDatosFicheroTexto();
    }

    public void leerDatosFicheroTexto() throws IOException {
        FileReader fr = new FileReader(RUTA_FICHERO_DATOS);
        BufferedReader br = new BufferedReader(fr);
        String linea = br.readLine();
        while (linea != null) {
            String[] datos = linea.split(",");
            if(datos[1].equals("Electronico")){
                Electronico productoElec = new Electronico(Integer.parseInt(datos[0]),datos[2],Double.parseDouble(datos[3]),
                        Integer.parseInt(datos[4]),datos[5],Integer.parseInt(datos[6]));
                listaProductos.add(productoElec);

            }else{
                Ropa productoRop = new Ropa(Integer.parseInt(datos[0]),datos[2],Double.parseDouble(datos[3]),
                        Integer.parseInt(datos[4]),datos[5],datos[6]);
                listaProductos.add(productoRop);
            }
            linea = br.readLine();
        }
        br.close();
        fr.close();
    }

    public void guardarDatosFicheroTexto() throws IOException {
        FileWriter fw = new FileWriter(RUTA_FICHERO_DATOS);
        BufferedWriter bw = new BufferedWriter(fw);
        for (Producto p : listaProductos) {
            if (p instanceof Electronico){
                Electronico productoElec = (Electronico) p;
                bw.write(productoElec.getId()+ ",Electronico," + productoElec.getNombre()+ "," + productoElec.getPrecio()+ ","+
                        productoElec.getStock()+ ","+ productoElec.getMarca() +"," + productoElec.getGarantia());
            }else{
                Ropa productoRop = (Ropa) p;
                bw.write(productoRop.getId()+ ",Ropa," + productoRop.getNombre()+ "," + productoRop.getPrecio()+ ","+
                        productoRop.getStock()+ ","+ productoRop.getTalla() +"," + productoRop.getMaterial());
            }
        }
        bw.close();
        fw.close();
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public boolean insertarProducto(Producto producto){
        for (Producto p:listaProductos) {
            if (p.getId() == producto.getId()) {
                return false;
            }
        }
        listaProductos.add(producto);
        return true;
    }
    public String mostrarInventario(){
        String resultado="Listado de productos."+System.lineSeparator();
        for(Producto p:listaProductos){
            resultado= resultado+ p.mostrarDetalles()+System.lineSeparator();
        }
        return resultado;
    }

    /**
     *
     * @param idProducto, a vender
     * @param cantidadVender,
     * @return true si se vende el producto correctamente, false si no hay stock suficiente
     * @throws ProductoNoInventarioException, el producto no está en el inventario.
     */
    public boolean venderProducto(int idProducto, int cantidadVender) throws ProductoNoInventarioException{
        for (Producto p:listaProductos){
            if (p.getId()==idProducto){
                if (p.estaDisponible(cantidadVender)){
                    p.vender(cantidadVender);
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        throw new ProductoNoInventarioException("El producto no esta en el inventario");

       }

    /**
     *
     * @param idProducto
     * @param cantidadReponer
     * @return true si se repone el producto, si no está el producto devuelve ProductoNoInventarioException
     * @throws ProductoNoInventarioException
     */
       public boolean reponerProducto(int idProducto, int cantidadReponer) throws ProductoNoInventarioException{
           for (Producto p:listaProductos) {
               if (p.getId() == idProducto) {
                   p.reponer(cantidadReponer);
                   return true;
               }
           }
           throw  new ProductoNoInventarioException("El producto no esta en el inventario");

       }
}
