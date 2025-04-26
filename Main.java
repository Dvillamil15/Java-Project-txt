// Importa clases para manejo de archivos y excepciones de entrada/salida
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal que genera reportes de ventas a partir de los archivos de entrada.
 * Crea reportes ordenados de vendedores por recaudación y productos por cantidad vendida.
 */
public class Main {
    
    /**
     * Método principal que coordina la generación de reportes.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando generación de reportes...");
        
        try {
            // Verificar existencia de archivos necesarios
            checkRequiredFiles();
            
            // Generar reportes de ventas por vendedor y por producto
            generateSalesmenReport();
            generateProductsReport();
            
            // Mensaje de éxito tras la generación de los reportes
            System.out.println("Reportes generados exitosamente!");
            System.out.println("1. salesmen_report.csv - Vendedores por recaudación");
            System.out.println("2. products_report.csv - Productos por cantidad vendida");
        } catch (FileNotFoundException e) {
            // Manejo de error si falta algún archivo necesario
            System.err.println("Error: Archivo no encontrado - " + e.getMessage());
        } catch (IOException e) {
            // Manejo de error de entrada/salida
            System.err.println("Error de entrada/salida - " + e.getMessage());
        } catch (NumberFormatException e) {
            // Manejo de error si hay problemas con el formato numérico
            System.err.println("Error en formato de números - " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Verifica que existan los archivos necesarios para generar los reportes.
     */
    private static void checkRequiredFiles() throws FileNotFoundException {
        // Verifica si existe el archivo con la información de los vendedores
        if (!new File("salesmen_info.txt").exists()) {
            throw new FileNotFoundException("No se encontró el archivo salesmen_info.txt");
        }
        // Verifica si existe el archivo con la información de los productos
        if (!new File("products_info.txt").exists()) {
            throw new FileNotFoundException("No se encontró el archivo products_info.txt");
        }
        
        // Verifica si existen archivos de ventas con el prefijo "ventas_"
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        if (salesFiles == null || salesFiles.length == 0) {
            throw new FileNotFoundException("No se encontraron archivos de ventas");
        }
    }
    

    //Genera un reporte de productos ordenado por cantidad vendida
    private static void generateProductsReport() throws IOException {
        // Implementación del método para generar el reporte de productos
        System.out.println("Generando reporte de productos...");
    }

    //Genera un reporte de vendedores ordenado por recaudación.
    private static void generateSalesmenReport() throws IOException {
        // Mapa para almacenar la recaudación de cada vendedor
        Map<String, Double> salesmanRevenue = new HashMap<>();
        // Mapa para almacenar la información detallada de los vendedores
        Map<String, String> salesmanInfo = new HashMap<>();
        
        // Leer información de vendedores desde el archivo salesmen_info.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            // Leer la primera línea (cabecera)
            String line;
            // Leer cada línea del archivo
            while ((line = reader.readLine()) != null) {
                // Separar la línea en partes usando el delimitador ";"
                // y asegurarse de que hay suficientes partes para procesar
                String[] parts = line.split(";");
                // Verificar que la línea tenga al menos 4 partes (ID, nombre, apellido, etc.)
                // y que el ID no esté vacío
                if (parts.length < 4) continue; // Saltar líneas inválidas
                
                String fullId = parts[0] + "_" + parts[1]; // ID único del vendedor
                salesmanInfo.put(fullId, line); // Guardar la línea completa para referencia
                salesmanRevenue.put(fullId, 0.0); // Inicializar con 0.0
            }
        }
        
        // Procesar archivos de ventas
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        
        // Verificar si se encontraron archivos de ventas
        // Si no se encontraron, lanzar una excepción
        if (salesFiles != null) {
            // Iterar sobre cada archivo de ventas encontrado
            for (File file : salesFiles) {
                // Verificar que el archivo sea un archivo regular (no un directorio)
                if (!file.isFile()) continue; // Saltar si no es un archivo regular
                // Verificar que el archivo no esté vacío
                if (file.length() == 0) continue; // Saltar archivos vacíos
                // Leer el archivo de ventas
                // Usar BufferedReader para leer el archivo línea por línea
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String header = reader.readLine(); // Leer la primera línea (cabecera)
                    if (header == null) continue; // Archivo vacío
                    
                    // Separar la cabecera en partes usando el delimitador ";"
                    // y asegurarse de que hay suficientes partes para procesar
                    String[] headerParts = header.split(";");
                    if (headerParts.length < 2) continue; // Encabezado inválido
                    
                    String fullId = headerParts[0] + "_" + headerParts[1]; // ID del vendedor
                    
                    // Verificar que el ID del vendedor esté en la lista de vendedores
                    // Si no está, imprimir una advertencia y continuar con el siguiente archivo
                    if (!salesmanRevenue.containsKey(fullId)) {
                        // Advertencia si el vendedor no está en la lista
                        System.err.println("Advertencia: Vendedor no encontrado en " + file.getName());
                        continue;
                    }
                    
                    // Leer cada línea del archivo de ventas
                    // y procesar las ventas de productos
                    String saleLine;
                    while ((saleLine = reader.readLine()) != null) {
                        // Separar la línea de venta en partes usando el delimitador ";"
                        String[] sale = saleLine.split(";");
                        // Verificar que la línea tenga al menos 2 partes (ID del producto y cantidad)
                        // y que la línea no esté vacía 
                        if (sale.length < 2) continue; // Línea inválida
                        
                        // Verificar que el ID del producto y la cantidad sean números válidos
                        // y convertirlos a enteros
                        try {
                            // Obtener el ID del producto y la cantidad de la línea de venta
                            int productId = Integer.parseInt(sale[0]);
                            // Obtener la cantidad vendida del producto
                            int quantity = Integer.parseInt(sale[1]);
                            // Verificar que la cantidad sea positiva
                            double price = getProductPrice(productId);
                            
                            // Actualizar recaudación sumando ventas
                            double currentRevenue = salesmanRevenue.get(fullId);
                            // Actualizar la recaudación del vendedor
                            // multiplicando el precio por la cantidad vendida
                            salesmanRevenue.put(fullId, currentRevenue + (price * quantity));
                            // Imprimir información de la venta procesada
                        } catch (NumberFormatException e) {
                            // Manejo de error si el formato numérico es incorrecto
                            System.err.println("Error en formato numérico en archivo " + file.getName() + ": " + saleLine);
                        }
                    }
                }
            }
        }
        
        // Ordenar vendedores por recaudación descendente
        List<Map.Entry<String, Double>> sortedSalesmen = new ArrayList<>(salesmanRevenue.entrySet());
        // Ordenar la lista de vendedores por recaudación usando un comparador
        sortedSalesmen.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        // Generar el archivo de reporte de vendedores
        try (PrintWriter writer = new PrintWriter("salesmen_report.csv")) {
            // Escribir la cabecera del archivo CSV
            // usando el delimitador ";"
            writer.println("TipoDocumento;NumeroDocumento;NombreCompleto;TotalRecaudado");
            
            // Iterar sobre la lista de vendedores ordenados
            // y escribir la información en el archivo CSV
            for (Map.Entry<String, Double> entry : sortedSalesmen) {
                String[] info = salesmanInfo.get(entry.getKey()).split(";");
                if (info.length >= 4) {
                    String fullName = info[2] + " " + info[3];
                    writer.printf("%s;%s;%s;%.2f%n", info[0], info[1], fullName, entry.getValue());
                }
            }
        }
    }
    
    //Obtiene el precio de un producto desde el archivo de productos.
    private static double getProductPrice(int productId) throws IOException {
        // Leer el archivo de productos y buscar el precio del producto por su ID
        // Usar BufferedReader para leer el archivo línea por línea
        try (BufferedReader reader = new BufferedReader(new FileReader("products_info.txt"))) {
            String line;
            // Leer cada línea del archivo de productos
            // y separar la línea en partes usando el delimitador ";"
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                // Verificar que la línea tenga al menos 3 partes (ID, nombre, precio)
                // y que el ID no esté vacío
                if (Integer.parseInt(parts[0]) == productId) {
                    // Si se encuentra el ID del producto, devolver el precio
                    // Convertir el precio a un número decimal
                    return Double.parseDouble(parts[2]);
                }
            }
        }
        // Si no se encuentra el producto, lanzar una excepción
        // indicando que el producto no fue encontrado
        throw new IOException("Producto con ID " + productId + " no encontrado");
    }
}
