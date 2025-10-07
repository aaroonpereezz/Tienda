package vista;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logica.*;


import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppTiendaGrafica extends Application {
    private BorderPane root = new BorderPane();
    private Inventario inventario;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            inventario = new Inventario();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        MenuBar menuBar = crearMenu();

       root.setTop(menuBar);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventario Tienda Online");
        primaryStage.show();
    }

    private MenuBar crearMenu() {
        Menu menu = new Menu("Opciones");

        MenuItem insertar = new MenuItem("Insertar producto");
        MenuItem vender = new MenuItem("Vender producto");
        MenuItem reponer = new MenuItem("Reponer producto");
        MenuItem mostrar = new MenuItem("Mostrar inventario");
        MenuItem salir = new MenuItem("Salir");

        insertar.setOnAction(e -> root.setCenter(pantallaInsertarProducto()));
        vender.setOnAction(e -> root.setCenter(pantallaVenderReponer(true)));
        reponer.setOnAction(e -> root.setCenter(pantallaVenderReponer(false)));
        mostrar.setOnAction(e -> root.setCenter(pantallaMostrarInventario()));
        salir.setOnAction(e -> System.exit(0));

        menu.getItems().addAll(insertar, vender, reponer, mostrar, salir);

        return new MenuBar(menu);
    }

    private GridPane pantallaInsertarProducto() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> tipoBox = new ComboBox<>();
        tipoBox.getItems().addAll("Electronico", "Ropa");
        TextField id=new TextField();
        TextField nombre = new TextField();
        TextField precio = new TextField();
        TextField stock = new TextField();

        TextField campoExtra1 = new TextField(); // marca / talla
        TextField campoExtra2 = new TextField(); // garantía / material

        Label l0 = new Label("Tipo:");
        Label l1=new Label("Id");
        Label l2 = new Label("Nombre:");
        Label l3 = new Label("Precio:");
        Label l4 = new Label("Stock:");
        Label l5 = new Label("Marca / Talla:");
        Label l6 = new Label("Garantía / Material:");

        Button btn = new Button("Guardar");
        btn.setOnAction(e -> {
            try {
                String tipo = tipoBox.getValue();
                int idProducto=Integer.parseInt(id.getText());
                String nom = nombre.getText();
                double pre = Double.parseDouble(precio.getText());
                int sto = Integer.parseInt(stock.getText());
                String ex1 = campoExtra1.getText();
                String ex2 = campoExtra2.getText();

                if ("Electronico".equals(tipo)) {
                    int garantia = Integer.parseInt(ex2);
                    inventario.insertarProducto(new Electronico(idProducto, nom, pre, sto, ex1, garantia));
                } else if ("Ropa".equals(tipo)) {
                    inventario.insertarProducto(new Ropa(idProducto, nom, pre, sto, ex1, ex2));
                }
                showInfo("Producto insertado.");
            } catch (Exception ex) {
                showError("Datos inválidos o incompletos.");
            }
        });

        grid.addRow(0, l0, tipoBox);
        grid.addRow(1, l1, id);
        grid.addRow(2, l2, nombre);
        grid.addRow(3, l3, precio);
        grid.addRow(4, l4, stock);
        grid.addRow(5, l5, campoExtra1);
        grid.addRow(6, l6, campoExtra2);
        grid.add(btn, 1, 7);

        return grid;
    }
    private VBox pantallaMostrarInventario() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        TableView<Producto> table = new TableView<>();

        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<Producto, String> tipoCol = new TableColumn<>("Tipo");
        tipoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue() instanceof Electronico ? "Electronico" : "Ropa"));

        TableColumn<Producto, Number> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()));

        TableColumn<Producto, Number> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStock()));

        table.getColumns().addAll(nombreCol, tipoCol, precioCol, stockCol);

        table.getItems().addAll(inventario.getListaProductos());

        box.getChildren().add(table);
        return box;
    }

    private VBox pantallaVenderReponer(boolean esVenta) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        ComboBox<Producto> combo = new ComboBox<>();
        TextField cantidadField = new TextField();
        Button btn = new Button(esVenta ? "Vender" : "Reponer");

        ArrayList<Producto> productos = inventario.getListaProductos();
        combo.getItems().addAll(productos);


        btn.setOnAction(e -> {
            Producto p = combo.getValue();
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (esVenta){
                try {
                    if (inventario.venderProducto(p.getId(), cantidad)) {
                        showInfo("Venta realizada");
                    } else {
                        showInfo("Venta No realizada, no hay stock suficiente");
                    }
                }
                catch (ProductoNoInventarioException ex){
                    showError("El producto no está en el inventario");
                }
            }
            else{
                inventario.reponerProducto(p.getId(), cantidad);
                showInfo("Reposición realizada");
            }


        });

        box.getChildren().addAll(new Label("Producto:"), combo, new Label("Cantidad:"), cantidadField, btn);
        return box;
    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

}
