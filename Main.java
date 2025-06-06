import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal para ejecutar el Gestor de Tareas
 * 
 * Esta aplicación permite gestionar tareas de manera sencilla con las
 * siguientes características:
 * - Crear, editar y eliminar tareas
 * - Marcar tareas como completadas o pendientes
 * - Agregar fechas límite opcionales
 * - Filtrar tareas por estado (pendientes, completadas, vencidas)
 * - Buscar tareas por título
 * - Persistencia automática en archivo local
 * - Exportar tareas a archivo de texto
 * 
 * @author Gestor de Tareas Java
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        // Configurar el look and feel antes de crear la interfaz
        try {
            // Intentar usar el look and feel del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usar el look and feel por defecto de Java
            System.err.println("No se pudo cargar el look and feel del sistema: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Error al cargar look and feel: " + ex.getMessage());
            }
        }

        // Crear y mostrar la interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Crear la ventana principal
                    TaskManagerGUI taskManagerGUI = new TaskManagerGUI();

                    // Mostrar la ventana
                    taskManagerGUI.setVisible(true);

                    // Mensaje de bienvenida en consola
                    System.out.println("=== GESTOR DE TAREAS ===");
                    System.out.println("Aplicación iniciada correctamente.");
                    System.out.println("Las tareas se guardan automáticamente en: tasks.dat");
                    System.out.println("¡Comienza a gestionar tus tareas!");

                } catch (Exception e) {
                    System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Método para mostrar información de ayuda de la aplicación
     */
    public static void showHelp() {
        System.out.println("\n=== AYUDA - GESTOR DE TAREAS ===");
        System.out.println("Esta aplicación te permite:");
        System.out.println("• Crear nuevas tareas con título, descripción y fecha límite opcional");
        System.out.println("• Ver todas tus tareas en una lista organizada");
        System.out.println("• Marcar tareas como completadas o pendientes");
        System.out.println("• Eliminar tareas individuales o todas las completadas");
        System.out.println("• Filtrar tareas por estado (todas, pendientes, completadas, vencidas)");
        System.out.println("• Buscar tareas por título");
        System.out.println("• Exportar tu lista de tareas a un archivo de texto");
        System.out.println("• Persistencia automática: tus tareas se guardan automáticamente");
        System.out.println("\nLas tareas se distinguen por colores:");
        System.out.println("• Verde: Tareas completadas");
        System.out.println("• Rojo: Tareas vencidas");
        System.out.println("• Blanco: Tareas pendientes normales");
        System.out.println("\nFormato de fecha: dd/mm/yyyy (ejemplo: 25/12/2024)");
        System.out.println("================================\n");
    }

    /**
     * Método para mostrar información de la aplicación
     */
    public static void showAbout() {
        System.out.println("\n=== ACERCA DE ===");
        System.out.println("Gestor de Tareas v1.0");
        System.out.println("Aplicación desarrollada en Java puro");
        System.out.println("Características:");
        System.out.println("• Sin dependencias externas");
        System.out.println("• Interfaz gráfica con Swing");
        System.out.println("• Persistencia en archivo local");
        System.out.println("• Multiplataforma (Windows, Mac, Linux)");
        System.out.println("=================\n");
    }
}