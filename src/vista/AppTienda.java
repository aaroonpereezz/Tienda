package vista;


import logica.*;

import java.io.IOException;
import java.util.Scanner;

public class AppTienda {

    private static void mostrarMenu() {
        System.out.println("=========== TIENDA ONLINE ===========");
        System.out.println("1) Añadir producto al inventario");
        System.out.println("2) Vender producto");
        System.out.println("3) Reponer producto");
        System.out.println("4) Mostrar inventario y valor total");
        System.out.println("0) Salir");
        System.out.println("-------------------------------------");
    }

    private static int leerEntero(String prompt) {
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }


    private static double leerDouble(String prompt) {
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número decimal válido (usa punto como separador).");
            }
        }
    }


    private static String leerCadena(String prompt) {
        Scanner sc=new Scanner(System.in);
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static void main(String[] args) {
        Inventario inventario = null;
        try {
            inventario = new Inventario();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Selecciona una opción: ");
            System.out.println();
            try {
                switch (opcion) {
                    case 1 -> opcionAñadirProducto(inventario);
                    case 2 -> opcionVender(inventario);
                    case 3 -> opcionReponer(inventario);
                    case 4 -> System.out.println(inventario.mostrarInventario());
                    case 0 -> salir = true;
                    default -> System.out.println("Opción inválida. Intenta de nuevo.");
                }
            } catch (ProductoNoInventarioException e) {
                System.out.println("[Error Inventario] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Excepción] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("¡Hasta luego!");
    }
    private static void opcionAñadirProducto(Inventario inv) {
        System.out.println("Tipo de producto: 1) Electrónico 2) Ropa");
        int tipo = leerEntero("Elige tipo: ");
        int id = leerEntero("ID: ");
        String nombre = leerCadena("Nombre: ");
        double precio = leerDouble("Precio unitario (sin IVA): ");
        int cantidad = leerEntero("Cantidad inicial: ");


        boolean insertado = false;
        if (tipo == 1) {
            String marca = leerCadena("Marca: ");
            int garantiaMeses = leerEntero("Garantía (meses): ");
            insertado = inv.insertarProducto(new Electronico(id, nombre, precio, cantidad, marca, garantiaMeses));
        } else if (tipo == 2) {
            String talla = leerCadena("Talla: ");
            String material = leerCadena("Material: ");
            insertado = inv.insertarProducto(new Ropa(id, nombre, precio, cantidad, talla, material));
        } else {
            System.out.println("Tipo no válido.");
            return;
        }


        if (insertado) System.out.println("Producto insertado correctamente.");
        else System.out.println("Ya existe un producto con ese ID.");
    }


    private static void opcionVender(Inventario inv) throws ProductoNoInventarioException {
        int id = leerEntero("ID del producto a vender: ");
        int cant = leerEntero("Cantidad a vender: ");
        if (inv.venderProducto(id, cant))
            System.out.println("Venta realizada.");
        else
            System.out.println("No hay stock suficiente para realizar la venta");
    }


    private static void opcionReponer(Inventario inv) throws Exception {
        int id = leerEntero("ID del producto a reponer: ");
        int cant = leerEntero("Cantidad a añadir: ");
        inv.reponerProducto(id, cant);
        System.out.println("Reposición realizada.");
    }
}
