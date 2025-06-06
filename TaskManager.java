import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class TaskManager {
    private List<Task> tasks;
    private static final String DATA_FILE = "tasks.dat";

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    // Agregar nueva tarea
    public void addTask(String title, String description, LocalDate dueDate) {
        Task newTask = new Task(title, description, dueDate);
        tasks.add(newTask);
        saveTasks();
    }

    public void addTask(String title, String description) {
        addTask(title, description, null);
    }

    // Eliminar tarea
    public boolean removeTask(Task task) {
        boolean removed = tasks.remove(task);
        if (removed) {
            saveTasks();
        }
        return removed;
    }

    public boolean removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveTasks();
            return true;
        }
        return false;
    }

    // Marcar tarea como completada
    public void markTaskAsCompleted(Task task) {
        task.markAsCompleted();
        saveTasks();
    }

    public void markTaskAsCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
            saveTasks();
        }
    }

    // Marcar tarea como pendiente
    public void markTaskAsPending(Task task) {
        task.markAsPending();
        saveTasks();
    }

    public void markTaskAsPending(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsPending();
            saveTasks();
        }
    }

    // Obtener todas las tareas
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // Obtener tareas pendientes
    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // Obtener tareas completadas
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // Obtener tareas vencidas
    public List<Task> getOverdueTasks() {
        return tasks.stream()
                .filter(Task::isDueDateExpired)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // Buscar tareas por título
    public List<Task> searchTasksByTitle(String keyword) {
        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // Estadísticas
    public int getTotalTasksCount() {
        return tasks.size();
    }

    public int getPendingTasksCount() {
        return getPendingTasks().size();
    }

    public int getCompletedTasksCount() {
        return getCompletedTasks().size();
    }

    public int getOverdueTasksCount() {
        return getOverdueTasks().size();
    }

    // Obtener tarea por índice
    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    // Actualizar tarea
    public void updateTask(int index, String title, String description, LocalDate dueDate) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            saveTasks();
        }
    }

    // Persistencia - Guardar tareas
    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error al guardar las tareas: " + e.getMessage());
        }
    }

    // Persistencia - Cargar tareas
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                tasks = (List<Task>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar las tareas: " + e.getMessage());
                tasks = new ArrayList<>();
            }
        }
    }

    // Limpiar todas las tareas completadas
    public void clearCompletedTasks() {
        tasks.removeIf(Task::isCompleted);
        saveTasks();
    }

    // Exportar tareas a texto plano
    public void exportTasksToText(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== GESTOR DE TAREAS ===");
            writer.println("Fecha de exportación: " + LocalDate.now());
            writer.println();

            List<Task> pending = getPendingTasks();
            List<Task> completed = getCompletedTasks();

            writer.println("TAREAS PENDIENTES (" + pending.size() + "):");
            writer.println("=====================================");
            for (Task task : pending) {
                writer.println("• " + task.getTitle());
                writer.println("  Descripción: " + task.getDescription());
                writer.println("  Fecha límite: " + task.getFormattedDueDate());
                if (task.isDueDateExpired()) {
                    writer.println("  *** VENCIDA ***");
                }
                writer.println();
            }

            writer.println("TAREAS COMPLETADAS (" + completed.size() + "):");
            writer.println("=======================================");
            for (Task task : completed) {
                writer.println("✓ " + task.getTitle());
                writer.println("  Descripción: " + task.getDescription());
                writer.println("  Fecha límite: " + task.getFormattedDueDate());
                writer.println();
            }
        }
    }
}