import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class BookManagementSystemApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private JTextField searchField;

    public BookManagementSystemApp() {
        setTitle("Book Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("ISBN");

        bookTable = new JTable(tableModel);
        bookTable.setAutoCreateRowSorter(true);
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editBook();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });

        JButton saveButton = new JButton("Save Books");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBooks();
            }
        });

        JButton loadButton = new JButton("Load Books");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(searchField, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog("Enter Title:");
        String author = JOptionPane.showInputDialog("Enter Author:");
        String isbn = JOptionPane.showInputDialog("Enter ISBN:");
    
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Object[] data = {title, author, isbn};
        tableModel.addRow(data);
    }
    
    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        
        String currentTitle = (String) tableModel.getValueAt(selectedRow, 0);
        String currentAuthor = (String) tableModel.getValueAt(selectedRow, 1);
        String currentIsbn = (String) tableModel.getValueAt(selectedRow, 2);
    
        String newTitle = JOptionPane.showInputDialog("Enter new Title:", currentTitle);
        String newAuthor = JOptionPane.showInputDialog("Enter new Author:", currentAuthor);
        String newIsbn = JOptionPane.showInputDialog("Enter new ISBN:", currentIsbn);
    
        if (newTitle.isEmpty() || newAuthor.isEmpty() || newIsbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        
        tableModel.setValueAt(newTitle, selectedRow, 0);
        tableModel.setValueAt(newAuthor, selectedRow, 1);
        tableModel.setValueAt(newIsbn, selectedRow, 2);
    }
    
    private void removeBook() {
        int selectedRow = bookTable.getSelectedRow();
    
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
    
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this book?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
        
            tableModel.removeRow(selectedRow);
        }
    }
    
    private void saveBooks() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
    
        if (option == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
              
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        writer.print(tableModel.getValueAt(row, col));
                        writer.print("\t"); 
                    }
                    writer.println(); 
                }
                JOptionPane.showMessageDialog(this, "Books saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadBooks() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
    
        if (option == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                // Clear existing table data
                tableModel.setRowCount(0);
    
            
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t"); 
                    tableModel.addRow(parts);
                }
                JOptionPane.showMessageDialog(this, "Books loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchBooks() {
        String searchText = searchField.getText().toLowerCase();
    
        
        tableModel.setRowCount(0);
    
        Object originalData;
        for (int row = 0; row < originalData.size(); row++) {
            boolean found = false;
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                String cellValue = originalData.get(row)[col].toLowerCase();
                if (cellValue.contains(searchText)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                tableModel.addRow(originalData.get(row));
            }
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookManagementSystemApp().setVisible(true);
            }
        });
    }
}
