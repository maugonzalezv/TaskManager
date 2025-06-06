import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private TaskManager taskManager;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JLabel statusLabel;
    private JComboBox<String> filterCombo;
    private JTextField searchField;

    public TaskManagerGUI() {
        taskManager = new TaskManager();
        initializeGUI();
        refreshTaskList();
        updateStatusLabel();
    }

    private void initializeGUI() {
        setTitle("Gestor de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior - Filtros y búsqueda
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central - Lista de tareas
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel derecho - Formulario de nueva tarea
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Panel inferior - Botones de acción y estado
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Configuración de la ventana
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        // Establecer colores modernos
        setAppearance();
    }

    private void setAppearance() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Usar look and feel por defecto
        }

        // Colores personalizados
        Color primaryColor = new Color(52, 152, 219);
        Color secondaryColor = new Color(236, 240, 241);
        Color accentColor = new Color(46, 204, 113);
        Color dangerColor = new Color(231, 76, 60);

        getContentPane().setBackground(secondaryColor);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new TitledBorder("Filtros y Búsqueda"));

        // Filtro por tipo
        JLabel filterLabel = new JLabel("Filtrar:");
        String[] filterOptions = { "Todas", "Pendientes", "Completadas", "Vencidas" };
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.addActionListener(e -> filterTasks());

        // Campo de búsqueda
        JLabel searchLabel = new JLabel("Buscar:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchTasks());

        // Botón limpiar filtros
        JButton clearButton = new JButton("Limpiar");
        clearButton.addActionListener(e -> clearFilters());

        topPanel.add(filterLabel);
        topPanel.add(filterCombo);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new TitledBorder("Lista de Tareas"));

        // Lista de tareas
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskCellRenderer());

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Nueva Tarea"));
        rightPanel.setPreferredSize(new Dimension(300, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo título
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Campo descripción
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);

        // Campo fecha límite
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Fecha límite (dd/mm/yyyy):"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dueDateField = new JTextField(20);
        dueDateField.setToolTipText("Formato: dd/mm/yyyy (opcional)");
        formPanel.add(dueDateField, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Agregar Tarea");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addTask());

        JButton clearFormButton = new JButton("Limpiar");
        clearFormButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(clearFormButton);
        formPanel.add(buttonPanel, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Panel de botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton completeButton = new JButton("Marcar Completada");
        completeButton.setBackground(new Color(52, 152, 219));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFocusPainted(false);
        completeButton.addActionListener(e -> markTaskCompleted());

        JButton pendingButton = new JButton("Marcar Pendiente");
        pendingButton.addActionListener(e -> markTaskPending());

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteTask());

        JButton clearCompletedButton = new JButton("Limpiar Completadas");
        clearCompletedButton.addActionListener(e -> clearCompletedTasks());

        JButton exportButton = new JButton("Exportar");
        exportButton.addActionListener(e -> exportTasks());

        actionPanel.add(completeButton);
        actionPanel.add(pendingButton);
        actionPanel.add(deleteButton);
        actionPanel.add(clearCompletedButton);
        actionPanel.add(exportButton);

        // Label de estado
        statusLabel = new JLabel();
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.EAST);

        return bottomPanel;
    }

    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String dueDateStr = dueDateField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate dueDate = null;
        if (!dueDateStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dueDate = LocalDate.parse(dueDateStr, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/mm/yyyy", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        taskManager.addTask(title, description, dueDate);
        clearForm();
        refreshTaskList();
        updateStatusLabel();

        JOptionPane.showMessageDialog(this, "Tarea agregada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        dueDateField.setText("");
    }

    private void markTaskCompleted() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = listModel.getElementAt(selectedIndex);
            taskManager.markTaskAsCompleted(selectedTask);
            refreshTaskList();
            updateStatusLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void markTaskPending() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = listModel.getElementAt(selectedIndex);
            taskManager.markTaskAsPending(selectedTask);
            refreshTaskList();
            updateStatusLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = listModel.getElementAt(selectedIndex);
            int option = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar la tarea: " + selectedTask.getTitle() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                taskManager.removeTask(selectedTask);
                refreshTaskList();
                updateStatusLabel();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearCompletedTasks() {
        int completedCount = taskManager.getCompletedTasksCount();
        if (completedCount == 0) {
            JOptionPane.showMessageDialog(this, "No hay tareas completadas para eliminar", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "¿Eliminar todas las " + completedCount + " tareas completadas?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            taskManager.clearCompletedTasks();
            refreshTaskList();
            updateStatusLabel();
        }
    }

    private void exportTasks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("tareas_" + LocalDate.now().toString() + ".txt"));

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                taskManager.exportTasksToText(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Tareas exportadas exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterTasks() {
        String filter = (String) filterCombo.getSelectedItem();
        List<Task> tasksToShow;

        switch (filter) {
            case "Pendientes":
                tasksToShow = taskManager.getPendingTasks();
                break;
            case "Completadas":
                tasksToShow = taskManager.getCompletedTasks();
                break;
            case "Vencidas":
                tasksToShow = taskManager.getOverdueTasks();
                break;
            default:
                tasksToShow = taskManager.getAllTasks();
                break;
        }

        listModel.clear();
        for (Task task : tasksToShow) {
            listModel.addElement(task);
        }
    }

    private void searchTasks() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshTaskList();
            return;
        }

        List<Task> searchResults = taskManager.searchTasksByTitle(keyword);
        listModel.clear();
        for (Task task : searchResults) {
            listModel.addElement(task);
        }
    }

    private void clearFilters() {
        filterCombo.setSelectedIndex(0);
        searchField.setText("");
        refreshTaskList();
    }

    private void refreshTaskList() {
        listModel.clear();
        List<Task> tasks = taskManager.getAllTasks();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    private void updateStatusLabel() {
        int total = taskManager.getTotalTasksCount();
        int pending = taskManager.getPendingTasksCount();
        int completed = taskManager.getCompletedTasksCount();
        int overdue = taskManager.getOverdueTasksCount();

        String statusText = String.format("Total: %d | Pendientes: %d | Completadas: %d | Vencidas: %d",
                total, pending, completed, overdue);
        statusLabel.setText(statusText);
    }

    // Renderer personalizado para la lista de tareas
    private class TaskCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Task task = (Task) value;

            // Texto HTML para mejor formato
            String html = "<html><b>" + task.getTitle() + "</b><br>" +
                    "<small>" + task.getDescription() + "</small><br>" +
                    "<small style='color: gray;'>Fecha límite: " + task.getFormattedDueDate() + "</small>";

            if (task.isDueDateExpired()) {
                html += "<br><small style='color: red;'><b>¡VENCIDA!</b></small>";
            }

            html += "</html>";
            setText(html);

            // Colores según el estado
            if (!isSelected) {
                if (task.isCompleted()) {
                    setBackground(new Color(212, 237, 218));
                    setForeground(new Color(40, 167, 69));
                } else if (task.isDueDateExpired()) {
                    setBackground(new Color(248, 215, 218));
                    setForeground(new Color(220, 53, 69));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
            }

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));

            return this;
        }
    }
}