import java.io.*; // Importa clases para manejo de archivos y excepciones de entrada/salida
import java.util.*; // Importa clases para estructuras de datos como listas y mapas

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
    
    /**
     * Genera un reporte de vendedores ordenado por recaudación.
     */
    private static void generateSalesmenReport() throws IOException {
        // Mapa para almacenar la recaudación de cada vendedor
        Map<String, Double> salesmanRevenue = new HashMap<>();
        // Mapa para almacenar la información detallada de los vendedores
        Map<String, String> salesmanInfo = new HashMap<>();
        
        // Leer información de vendedores desde el archivo salesmen_info.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue; // Saltar líneas inválidas
                
                String fullId = parts[0] + "_" + parts[1]; // ID único del vendedor
                salesmanInfo.put(fullId, line);
                salesmanRevenue.put(fullId, 0.0); // Inicializar con 0.0
            }
        }
        
        // Procesar archivos de ventas
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        
        if (salesFiles != null) {
            for (File file : salesFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String header = reader.readLine(); // Leer la primera línea (cabecera)
                    if (header == null) continue; // Archivo vacío
                    
                    String[] headerParts = header.split(";");
                    if (headerParts.length < 2) continue; // Encabezado inválido
                    
                    String fullId = headerParts[0] + "_" + headerParts[1]; // ID del vendedor
                    
                    if (!salesmanRevenue.containsKey(fullId)) {
                        // Advertencia si el vendedor no está en la lista
                        System.err.println("Advertencia: Vendedor no encontrado en " + file.getName());
                        continue;
                    }
                    
                    String saleLine;
                    while ((saleLine = reader.readLine()) != null) {
                        String[] sale = saleLine.split(";");
                        if (sale.length < 2) continue; // Línea inválida
                        
                        try {
                            int productId = Integer.parseInt(sale[0]);
                            int quantity = Integer.parseInt(sale[1]);
                            double price = getProductPrice(productId);
                            
                            // Actualizar recaudación sumando ventas
                            double currentRevenue = salesmanRevenue.get(fullId);
                            salesmanRevenue.put(fullId, currentRevenue + (price * quantity));
                        } catch (NumberFormatException e) {
                            System.err.println("Error en formato numérico en archivo " + file.getName() + ": " + saleLine);
                        }
                    }
                }
            }
        }
        
        // Ordenar vendedores por recaudación descendente
        List<Map.Entry<String, Double>> sortedSalesmen = new ArrayList<>(salesmanRevenue.entrySet());
        sortedSalesmen.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        // Generar el archivo de reporte de vendedores
        try (PrintWriter writer = new PrintWriter("salesmen_report.csv")) {
            writer.println("TipoDocumento;NumeroDocumento;NombreCompleto;TotalRecaudado");
            
            for (Map.Entry<String, Double> entry : sortedSalesmen) {
                String[] info = salesmanInfo.get(entry.getKey()).split(";");
                if (info.length >= 4) {
                    String fullName = info[2] + " " + info[3];
                    writer.printf("%s;%s;%s;%.2f%n", info[0], info[1], fullName, entry.getValue());
                }
            }
        }
    }
    
    /**
     * Obtiene el precio de un producto desde el archivo de productos.
     */
    private static double getProductPrice(int productId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("products_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (Integer.parseInt(parts[0]) == productId) {
                    return Double.parseDouble(parts[2]);
                }
            }
        }
        throw new IOException("Producto con ID " + productId + " no encontrado");
    }
}
