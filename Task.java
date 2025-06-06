import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private LocalDate createdDate;
    
    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
        this.createdDate = LocalDate.now();
    }
    
    // Constructor alternativo sin fecha límite
    public Task(String title, String description) {
        this(title, description, null);
    }
    
    // Getters y Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    // Métodos de utilidad
    public void markAsCompleted() {
        this.completed = true;
    }
    
    public void markAsPending() {
        this.completed = false;
    }
    
    public boolean isDueDateExpired() {
        if (dueDate == null) return false;
        return LocalDate.now().isAfter(dueDate) && !completed;
    }
    
    public String getFormattedDueDate() {
        if (dueDate == null) return "Sin fecha límite";
        return dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        if (dueDate != null) {
            sb.append(" (Vence: ").append(getFormattedDueDate()).append(")");
        }
        if (completed) {
            sb.append(" [COMPLETADA]");
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return title.equals(task.title) && 
               createdDate.equals(task.createdDate);
    }
    
    @Override
    public int hashCode() {
        return title.hashCode() + createdDate.hashCode();
    }
} 