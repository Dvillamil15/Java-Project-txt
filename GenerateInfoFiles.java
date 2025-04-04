// Importaciones necesarias para manejo de archivos y generación de números aleatorios
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


//Clase para la generación de archivos con los datos de prueba
public class GenerateInfoFiles {
    // Arreglos con nombres y apellidos para generar datos de vendedores
    private static final String[] NOMBRES = {"Juan", "Maria", "Carlos", "Ana", "Luis"};
    private static final String[] APELLIDOS = {"Gomez", "Lopez", "Martinez", "Rodriguez", "Perez"};
    private static final int PRODUCTS_COUNT = 10; // Número fijo de productos a generar (en este caso 10)

    /**
     * Genera archivos de ventas para cada vendedor listados en 'salesmen_info.txt'.
     * Lee el archivo de vendedores y crea un archivo de ventas por cada uno.
     */
    private static void generateSalesFiles() {
        // Intenta abrir el archivo de vendedores para lectura
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            String line;
            
            // Lee línea por línea (cada línea es un vendedor)
            while ((line = reader.readLine()) != null) {

                // Divide la línea por ';' para obtener los datos del vendedor
                String[] parts = line.split(";");
                String tipoDoc = parts[0]; // Tipo de documento (CC o TI)
                long numeroDoc = Long.parseLong(parts[1]); // Número de documento
                // Genera un número aleatorio de ventas (entre 1 y 5)
                int randomSales = new Random().nextInt(5) + 1;
                // Crea el archivo de ventas para este vendedor
                createSalesMenFile(randomSales, tipoDoc, numeroDoc);
            }
        } catch (IOException e) {
            // Manejo de errores si no se puede leer el archivo
            System.err.println("Error al leer salesmen_info.txt: " + e.getMessage());
        }
    }

    /**
     * Método que crea un archivo de productos con datos pseudoaleatorios.
     * @param productsCount - Número de productos a generar.
     */
    public static void createProductsFile(int productsCount) {
        // Intenta crear/escribir en el archivo 'products_info.txt'
        try (PrintWriter writer = new PrintWriter("products_info.txt")) {

            // Genera cada producto con ID, nombre y precio aleatorio
            for (int i = 1; i <= productsCount; i++) {
                // Formato: ID; NombreProducto; Precio
                writer.println(i + ";Producto" + i + ";" + (new Random().nextDouble() * 100 + 1));
            }
            //En caso de que haya algún error en la creación de archivos, "salta" la excepción del try-catch
        } catch (FileNotFoundException e) {
            System.err.println("Error al crear products_info.txt: " + e.getMessage());
        }
    }

    /**
     * Método que crea un archivo con información de vendedores.
     * @param salesmanCount - Número de vendedores a generar.
     */
    public static void createSalesManInfoFile(int salesmanCount) {
        Random random = new Random(); //Crea un generador de números aleatorio

        // Intenta crear/escribir en el archivo 'salesmen_info.txt'
        try (PrintWriter writer = new PrintWriter("salesmen_info.txt")) {

            // Genera cada vendedor con datos aleatorios
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = random.nextBoolean() ? "CC" : "TI"; // Tipo de documento aleatorio
                long numeroDoc = 1000 + i; // Número de documento secuencial desde 1000
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)]; // Nombre aleatorio
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)]; // Apellido aleatorio
                // Formato: TipoDocumento;NúmeroDocumento;Nombres;Apellidos
                writer.println(tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido); //Imprime con el formato adecuado
            }
            //En caso de que haya algún error en la creación de archivos, "salta" la excepción del try-catch
        } catch (FileNotFoundException e) {
            System.err.println("Error al crear salesmen_info.txt: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo de ventas para un vendedor específico.
     * @param randomSalesCount - Número de ventas a generar.
     * @param tipoDocumento - Tipo de documento del vendedor (CC/TI).
     * @param numeroDocumento - Número de documento del vendedor.
     */
    public static void createSalesMenFile(int randomSalesCount, String tipoDocumento, long numeroDocumento) {

        // Nombre del archivo: ventas_[TipoDocumento]_[NumeroDocumento].txt
        String fileName = "ventas_" + tipoDocumento + "_" + numeroDocumento + ".txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {

            // Primera línea: Tipo y número de documento del vendedor
            writer.println(tipoDocumento + ";" + numeroDocumento);
            Random random = new Random(); //Crea un nuevo generador de números aleatorio

            // Genera cada venta con producto y cantidad aleatorios
            for (int i = 0; i < randomSalesCount; i++) {
                int productId = random.nextInt(PRODUCTS_COUNT) + 1; // ID entre 1 y PRODUCTS_COUNT
                int quantity = random.nextInt(10) + 1; // Cantidad entre 1 y 10
                writer.println(productId + ";" + quantity); //Imprime con el formato adecuado
            }
            //En caso de que haya algún error en la creación de archivos, "salta" la excepción del try-catch
        } catch (FileNotFoundException e) {
            System.err.println("Error al crear " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Método principal que ejecuta la generación de archivos de prueba.
     */
    public static void main(String[] args) {
        createProductsFile(PRODUCTS_COUNT); // Genera archivo de productos
        createSalesManInfoFile(5); // Genera archivo de 5 vendedores
        generateSalesFiles(); // Genera archivos de ventas para cada vendedor
    }
}