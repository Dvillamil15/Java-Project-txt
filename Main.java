import java.io.*;
import java.util.*;

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
            
            // Generar reportes
            generateSalesmenReport();
            generateProductsReport();
            
            System.out.println("Reportes generados exitosamente!");
            System.out.println("1. salesmen_report.csv - Vendedores por recaudación");
            System.out.println("2. products_report.csv - Productos por cantidad vendida");
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida - " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error en formato de números - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Verifica que existan los archivos necesarios para generar los reportes.
     */
    private static void checkRequiredFiles() throws FileNotFoundException {
        if (!new File("salesmen_info.txt").exists()) {
            throw new FileNotFoundException("No se encontró el archivo salesmen_info.txt");
        }
        if (!new File("products_info.txt").exists()) {
            throw new FileNotFoundException("No se encontró el archivo products_info.txt");
        }
        
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        if (salesFiles == null || salesFiles.length == 0) {
            throw new FileNotFoundException("No se encontraron archivos de ventas");
        }
    }
    
    /**
     * Genera reporte de vendedores ordenado por recaudación.
     */
    private static void generateSalesmenReport() throws IOException {
        // 1. Leer información de vendedores
        Map<String, Double> salesmanRevenue = new HashMap<>();
        Map<String, String> salesmanInfo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("salesmen_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue; // Saltar líneas inválidas
                
                String fullId = parts[0] + "_" + parts[1];
                salesmanInfo.put(fullId, line);
                salesmanRevenue.put(fullId, 0.0); // Inicializar con 0.0
            }
        }
        
        // 2. Procesar archivos de ventas
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        
        if (salesFiles != null) {
            for (File file : salesFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String header = reader.readLine();
                    if (header == null) continue; // Archivo vacío
                    
                    String[] headerParts = header.split(";");
                    if (headerParts.length < 2) continue; // Encabezado inválido
                    
                    String fullId = headerParts[0] + "_" + headerParts[1];
                    
                    // Verificar que el vendedor existe
                    if (!salesmanRevenue.containsKey(fullId)) {
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
                            
                            // Actualizar recaudación de forma segura
                            double currentRevenue = salesmanRevenue.get(fullId);
                            salesmanRevenue.put(fullId, currentRevenue + (price * quantity));
                        } catch (NumberFormatException e) {
                            System.err.println("Error en formato numérico en archivo " + file.getName() + ": " + saleLine);
                        } catch (IOException e) {
                            System.err.println("Error al obtener precio para producto en archivo " + file.getName());
                        }
                    }
                }
            }
        }
        
        // 3. Ordenar y generar reporte
        List<Map.Entry<String, Double>> sortedSalesmen = new ArrayList<>(salesmanRevenue.entrySet());
        sortedSalesmen.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        try (PrintWriter writer = new PrintWriter("salesmen_report.csv")) {
            writer.println("TipoDocumento;NumeroDocumento;NombreCompleto;TotalRecaudado");
            
            for (Map.Entry<String, Double> entry : sortedSalesmen) {
                String[] info = salesmanInfo.get(entry.getKey()).split(";");
                if (info.length >= 4) { // Verificar que tenga todos los campos
                    String fullName = info[2] + " " + info[3];
                    writer.printf("%s;%s;%s;%.2f%n", info[0], info[1], fullName, entry.getValue());
                }
            }
        }
    }
    
    /**
     * Genera reporte de productos ordenados por cantidad vendida.
     */
    private static void generateProductsReport() throws IOException {
        // 1. Leer información de productos
        Map<Integer, ProductInfo> products = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("products_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                products.put(id, new ProductInfo(parts[1], Double.parseDouble(parts[2])));
            }
        }
        
        // 2. Contar ventas por producto
        Map<Integer, Integer> productSales = new HashMap<>();
        products.keySet().forEach(id -> productSales.put(id, 0));
        
        File[] salesFiles = new File(".").listFiles((dir, name) -> name.startsWith("ventas_"));
        
        for (File file : salesFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Saltar encabezado
                
                String saleLine;
                while ((saleLine = reader.readLine()) != null) {
                    String[] sale = saleLine.split(";");
                    int productId = Integer.parseInt(sale[0]);
                    int quantity = Integer.parseInt(sale[1]);
                    
                    productSales.put(productId, productSales.get(productId) + quantity);
                }
            }
        }
        
        // 3. Ordenar y generar reporte
        List<Map.Entry<Integer, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet());
        sortedProducts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        try (PrintWriter writer = new PrintWriter("products_report.csv")) {
            writer.println("NombreProducto;Precio;TotalVendido");
            
            for (Map.Entry<Integer, Integer> entry : sortedProducts) {
                ProductInfo info = products.get(entry.getKey());
                writer.printf("%s;%.2f;%d%n", info.name, info.price, entry.getValue());
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
    
    /**
     * Clase auxiliar para almacenar información de productos.
     */
    private static class ProductInfo {
        String name;
        double price;
        
        ProductInfo(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}
