// Importaciones necesarias para manejo de archivos y generación de números aleatorios
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


//Genera archivos de prueba pseudoaleatorios para el proyecto.
public class GenerateInfoFiles {
    //Arreglos de nombres y apellidos para generar datos aleatorios
    private static final String[] NOMBRES = {"Juan", "Maria", "Carlos", "Ana", "Luis"};
    //Arreglo de apellidos para generar datos aleatorios
    private static final String[] APELLIDOS = {"Gomez", "Lopez", "Martinez", "Rodriguez", "Perez"};
    //Número de productos a generar
    private static final int PRODUCTS_COUNT = 10; // Número de productos a generar


//Genera archivos de ventas para cada vendedor en el archivo de información de vendedores.
    private static void generateSalesFiles() {
        //Lee el archivo de información de vendedores y genera archivos de ventas para cada uno.
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            //Lee cada línea del archivo de información de vendedores
            String line;
            //Mientras haya líneas por leer
            //Lee cada línea del archivo de información de vendedores
            while ((line = reader.readLine()) != null) {
                //Divide la línea en partes usando el delimitador ";"
                String[] parts = line.split(";");
                //Verifica que la línea tenga al menos 2 partes (tipo de documento y número de documento)
                String tipoDoc = parts[0];
                long numeroDoc = Long.parseLong(parts[1]);
                //Genera un número aleatorio de ventas entre 1 y 5 para cada vendedor
                int randomSales = new Random().nextInt(5) + 1;
                //Crea un archivo de ventas para el vendedor con el número aleatorio de ventas generado
                createSalesMenFile(randomSales, tipoDoc, numeroDoc);
            }
            //Cierra el BufferedReader después de usarlo
        } catch (IOException e) {
            //Imprime un mensaje de error si ocurre una excepción al leer el archivo
            System.err.println("Error creating salesmen_info.txt: " + e.getMessage());
        }
    }


//Crea un archivo de productos con datos pseudoaleatorios para productsCount (Número de productos a generar).
    public static void createProductsFile(int productsCount) {
        //Genera un archivo de productos con datos pseudoaleatorios para productsCount (Número de productos a generar).
        try (PrintWriter writer = new PrintWriter("products_info.txt")) {
            //Escribe la cabecera del archivo de productos
            for (int i = 1; i <= productsCount; i++) {
                //Genera un ID de producto, nombre y precio aleatorio
                writer.println(i + ";Producto" + i + ";" + (new Random().nextDouble() * 100 + 1));
            }
            //Cierra el PrintWriter después de usarlo
        } catch (FileNotFoundException e) {
            //Imprime un mensaje de error si ocurre una excepción al crear el archivo
            System.err.println("Error creating products_info.txt: " + e.getMessage());
        }
    }


//Crea un archivo de información de vendedores.param salesmanCount Número de vendedores a generar.
    public static void createSalesManInfoFile(int salesmanCount) {
        Random random = new Random(); //Genera un objeto Random para generar números aleatorios
        try (PrintWriter writer = new PrintWriter("salesmen_info.txt")) {
            //Escribe la cabecera del archivo de información de vendedores
            for (int i = 0; i < salesmanCount; i++) {
                //Genera un tipo de documento aleatorio (CC o TI) y un número de documento aleatorio
                String tipoDoc = random.nextBoolean() ? "CC" : "TI";
                //Genera un número de documento aleatorio entre 1000 y 9999
                long numeroDoc = 1000 + i;
                //Genera un nombre y apellido aleatorio usando los arreglos de nombres y apellidos
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                //Genera un apellido aleatorio usando el arreglo de apellidos
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
                //Escribe la información del vendedor en el archivo de información de vendedores
                writer.println(tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido);
            }
            //Cierra el PrintWriter después de usarlo
        } catch (FileNotFoundException e) {
            //Imprime un mensaje de error si ocurre una excepción al crear el archivo
            System.err.println("Error creating salesmen_info.txt: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo de ventas para un vendedor.
     * @param randomSalesCount Número de ventas a generar.
     * @param tipoDocumento Tipo de documento del vendedor.
     * @param numeroDocumento Número de documento del vendedor.
     */

    //Crea un archivo de ventas para un vendedor.
    // @param randomSalesCount Número de ventas a generar.
    public static void createSalesMenFile(int randomSalesCount, String tipoDocumento, long numeroDocumento) {
        //Genera un archivo de ventas para un vendedor con el número aleatorio de ventas generado
        String fileName = "ventas_" + tipoDocumento + "_" + numeroDocumento + ".txt";
        //Crea un PrintWriter para escribir en el archivo de ventas
        try (PrintWriter writer = new PrintWriter(fileName)) {
            //Escribe la cabecera del archivo de ventas
            writer.println(tipoDocumento + ";" + numeroDocumento);
            //Escribe la cabecera del archivo de ventas
            Random random = new Random(); //Genera un objeto Random para generar números aleatorios
            //Genera un número aleatorio de ventas entre 1 y 5 para cada vendedor
            for (int i = 0; i < randomSalesCount; i++) {
                //Genera un ID de producto aleatorio entre 1 y PRODUCTS_COUNT (Número de productos a generar)
                int productId = random.nextInt(PRODUCTS_COUNT) + 1;
                //Genera una cantidad aleatoria entre 1 y 10
                int quantity = random.nextInt(10) + 1;
                //Escribe la información de la venta en el archivo de ventas
                writer.println(productId + ";" + quantity);
            }
            //Cierra el PrintWriter después de usarlo
        } catch (FileNotFoundException e) {
            //Imprime un mensaje de error si ocurre una excepción al crear el archivo
            System.err.println("Error creating " + fileName + ": " + e.getMessage());
        }
    }

//Método principal que genera los archivos de prueba.
    public static void main(String[] args) { //Genera los archivos de prueba.
        createProductsFile(PRODUCTS_COUNT); //Genera el archivo de productos
        createSalesManInfoFile(5); //Genera el archivo de información de vendedores
        generateSalesFiles(); //Genera los archivos de ventas para cada vendedor
    }
}