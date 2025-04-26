## ✨Conclusiones:

### ✅ Generación de datos de pruebas
Se implementó la clase GenerateInfoFiles para crear archivos pseudoaleatorios de:

- Vendedores (con nombres, apellidos y documentos).
- Productos (con IDs, nombres y precios).
- Ventas (asociando vendedores con productos vendidos).

### ✅ Procesamiento de Archivos
- Se leyeron y procesaron múltiples archivos .txt usando BufferedReader y FileReader.
- Se validaron formatos y se manejaron errores con try-catch.

### ✅ Estructuras de Datos Eficientes
- Se utilizaron HashMap para almacenar y buscar rápidamente vendedores y productos.
- Se acumularon ventas usando merge() para sumar totales sin perder rendimiento.

### ✅ Generación de Reportes
- Reporte de Vendedores: Ordenado por monto total vendido (de mayor a menor).
- Reporte de Productos: Ordenado por cantidad vendida (de mayor a menor).
- Se guardaron en archivos .csv usando PrintWriter.

### ✅ Buenas Prácticas
- Documentación clara con comentarios Javadoc.
- Código modular separando responsabilidades en métodos bien definidos.
- Manejo de excepciones para evitar crashes inesperados.

## 📚 Aprendizajes Obtenidos

### ✅ Manejo de Archivos en Java:
- Aprendimos a leer y escribir archivos .txt y .csv eficientemente con BufferedReader y PrintWriter.
- Entendímos la importancia del try-with-resources para evitar memory leaks.

### ✅ Uso Avanzado de Mapas (HashMap)
- Aprendímos a usar Map.merge() para acumular valores sin necesidad de validaciones manuales.
- Descubrímos cómo ordenar mapas convirtiéndolos en listas con Comparator.

### ✅ Programación Orientada a Objetos (POO)
- Aplicamos el principio de encapsulamiento con clases Salesman y Product.
- Practicamos la inmutabilidad usando campos final y solo getters.

### ✅ Manejo de Errores
- Mejoramos en el tratamiento de excepciones (IOException, FileNotFoundException).
- Aprendímos a dar mensajes de error claros con System.err.println().

### ✅ Generación de Datos Aleatorios
- Usamos "Random" para simular datos realistas y probar el programa exhaustivamente.

## 📙 Conclusión final
Este proyecto no solo cumplió con los objetivos técnicos, sino que también fortaleció nuestras habilidades como programadores.
Ahora tenemos unas bases más solidas para trabajar con:
- 🔸 Procesamiento de archivos
- 🔸 Estructuras de datos eficientes
- 🔸 Generación de reportes
- 🔸 Buenas prácticas de código
