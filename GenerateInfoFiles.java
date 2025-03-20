import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

//Genera archivos de prueba pseudoaleatorios para el proyecto.

public class GenerateInfoFiles {
    private static final String[] NOMBRES = {"Juan", "Maria", "Carlos", "Ana", "Luis"};
    private static final String[] APELLIDOS = {"Gomez", "Lopez", "Martinez", "Rodriguez", "Perez"};
    private static final int PRODUCTS_COUNT = 10; // Número de productos a generar

//Genera archivos de ventas para cada vendedor en el archivo de información de vendedores.

    private static void generateSalesFiles() {
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String tipoDoc = parts[0];
                long numeroDoc = Long.parseLong(parts[1]);
                int randomSales = new Random().nextInt(5) + 1;
                createSalesMenFile(randomSales, tipoDoc, numeroDoc);
            }
        } catch (IOException e) {
            System.err.println("Error creating salesmen_info.txt: " + e.getMessage());
        }
    }

//Crea un archivo de productos con datos pseudoaleatorios para productsCount (Número de productos a generar).

    public static void createProductsFile(int productsCount) {
        try (PrintWriter writer = new PrintWriter("products_info.txt")) {
            for (int i = 1; i <= productsCount; i++) {
                writer.println(i + ";Producto" + i + ";" + (new Random().nextDouble() * 100 + 1));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error creating products_info.txt: " + e.getMessage());
        }
    }

//Crea un archivo de información de vendedores.param salesmanCount Número de vendedores a generar.
    public static void createSalesManInfoFile(int salesmanCount) {
        Random random = new Random();
        try (PrintWriter writer = new PrintWriter("salesmen_info.txt")) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = random.nextBoolean() ? "CC" : "TI";
                long numeroDoc = 1000 + i;
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
                writer.println(tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error creating salesmen_info.txt: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo de ventas para un vendedor.
     * @param randomSalesCount Número de ventas a generar.
     * @param tipoDocumento Tipo de documento del vendedor.
     * @param numeroDocumento Número de documento del vendedor.
     */

    public static void createSalesMenFile(int randomSalesCount, String tipoDocumento, long numeroDocumento) {
        String fileName = "ventas_" + tipoDocumento + "_" + numeroDocumento + ".txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(tipoDocumento + ";" + numeroDocumento);
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                int productId = random.nextInt(PRODUCTS_COUNT) + 1;
                int quantity = random.nextInt(10) + 1;
                writer.println(productId + ";" + quantity);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error creating " + fileName + ": " + e.getMessage());
        }
    }

//Método principal que genera los archivos de prueba.
    public static void main(String[] args) {
        createProductsFile(PRODUCTS_COUNT);
        createSalesManInfoFile(5);
        generateSalesFiles();
    }
}