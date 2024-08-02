
package finals;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

enum UserMenu { CREATE, SAVE, UPDATE, DELETE, SEARCH, DISPLAY, EXIT };

class Menu {
    private static final String FILE_PATH = "C:\\Users\\Perdi\\Documents\\NetBeansProjects\\finals\\app\\src\\main\\java\\finals\\StorageDB.txt";
    private JTextArea textArea;

    public void create() {
        try {
            File file = new File(FILE_PATH);
            if (file.createNewFile()) {
                JOptionPane.showMessageDialog(null, "File created successfully");
            } else {
                JOptionPane.showMessageDialog(null, "File already exists");
            }
        } catch (IOException e) {
            handleIOException("Error creating file", e);
        }
    }

    public void save(String memo) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(memo + "\n");
            JOptionPane.showMessageDialog(null, "Record Saved Successfully");
        } catch (IOException e) {
            handleIOException("Error writing to file", e);
        }
    }

    public void searchFile() {
    try {
        String idSearch = JOptionPane.showInputDialog(null, "Input ID to Search:");

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "'StorageDB.txt' file does not exist.");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean found = false;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", 10);
            String id = parts[0];
            if (id.equals(idSearch)) {
                found = true;
                result.append("ID number: ").append(parts[0]).append("\n");
                result.append("Hardware Name: ").append(parts[1]).append("\n");
                result.append("Category: ").append(parts[2]).append("\n");
                result.append("Brand: ").append(parts[3]).append("\n");
                result.append("Size/Capacity: ").append(parts[4]).append("\n");
                result.append("Price: PHP ").append(parts[5]).append("\n");
                result.append("Release Year: ").append(parts[6]).append("\n");
                result.append("Date Purchased: ").append(parts[7]).append("\n");
                result.append("Supplier Name: ").append(parts[8]).append("\n");
                result.append("Supplier Contact Number: ").append(parts[9]).append("\n");
                break;
            }
        }
        reader.close();

        if (found) {
            textArea.setText(result.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Record Does not Exist!!");
        }
    } catch (IOException e) {
        handleIOException("An unexpected error has occurred", e);
    }
}
    

    public void display() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(line -> sb.append(line).append("\n"));
            textArea.setText(sb.toString());
        } catch (IOException e) {
            handleIOException("An unexpected error has occurred", e);
        }
    }

    public void update() {
        try {
            String idSearch = JOptionPane.showInputDialog(null, "Input ID to Search:");

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "'StorageDB.txt' file does not exist.");
                return;
            }

            StringBuilder updatedContent = new StringBuilder();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 10);
                String id = parts[0];
                if (id.equals(idSearch)) {
                    found = true;

                    System.out.println("Record Found:");
                    String[] fieldName = {"ID number", "Hardware Name", "Category", "Brand", "Size/Capacity", "Price", "Release Date", "Date Purchased", "Supplier Name", "Supplier Contact Number"};
                    for (int i = 0; i < parts.length; i++) {
                        String update = JOptionPane.showInputDialog(null, "Update " + fieldName[i] + " (" + parts[i] + "): ");
                        parts[i] = update.isEmpty() ? parts[i] : update;
                    }
                    updatedContent.append(String.join(",", parts) + "\n");
                    JOptionPane.showMessageDialog(null, "Record Updated Successfully");
                } else {
                    updatedContent.append(line + "\n");
                }
            }
            reader.close();

            if (!found) {
                JOptionPane.showMessageDialog(null, "Record Does not Exist!!");
                return;
            }

            FileWriter writer = new FileWriter(file);
            writer.write(updatedContent.toString());
            writer.close();

        } catch (IOException e) {
            handleIOException("An unexpected error has occurred", e);
        }
    }

    public void delete() {
        try {
            String idSearch = JOptionPane.showInputDialog(null, "Input ID to Delete:");

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "'StorageDB.txt' file does not exist.");
                return;
            }

            StringBuilder updatedContent = new StringBuilder();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 10);
                String id = parts[0];
                if (!id.equals(idSearch)) {
                    updatedContent.append(line + "\n");
                } else {
                    found = true;
                    JOptionPane.showMessageDialog(null, "Record Found and Deleted:\n" + line);
                }
            }
            reader.close();

            if (!found) {
                JOptionPane.showMessageDialog(null, "Record Does not Exist!!");
                return;
            }

            FileWriter writer = new FileWriter(file);
            writer.write(updatedContent.toString());
            writer.close();

            JOptionPane.showMessageDialog(null, "Record Deleted Successfully");

        } catch (IOException e) {
            handleIOException("An unexpected error has occurred", e);
        }
    }
    
    private void handleIOException(String message, IOException e) {
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage());
        e.printStackTrace();
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }
}

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hardware Inventory Record");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(550, 300);
            frame.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 1));

            Menu menu = new Menu();
            menu.setTextArea(textArea);

            JButton createButton = new JButton(UserMenu.CREATE.toString() + " Database");
            createButton.setBackground(new Color(204, 204, 255));
            createButton.setFont(new Font("Arial", Font.BOLD, 12));
            createButton.addActionListener(e -> menu.create());

            JButton saveButton = new JButton(UserMenu.SAVE.toString() + " New Record");
            saveButton.setBackground(new Color(204, 204, 255));
            saveButton.setFont(new Font("Arial", Font.BOLD, 12));
            saveButton.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(null, "Enter ID number:");
                String hardwareName = JOptionPane.showInputDialog(null, "Enter Hardware Name:");
                String category = JOptionPane.showInputDialog(null, "Enter Category (CPU, Monitor, Printer, Keyboard, Mouse):");
                String brand = JOptionPane.showInputDialog(null, "Enter Brand:");
                String sizeCapacity = JOptionPane.showInputDialog(null, "Enter Size/Capacity:");
                String price = JOptionPane.showInputDialog(null, "Enter Price:");
                String releaseDate = JOptionPane.showInputDialog(null, "Enter Release Date (MM/DD/YYYY):");
                String datePurchased = JOptionPane.showInputDialog(null, "Enter Date Purchased (MM/DD/YYYY):");
                String supplierName = JOptionPane.showInputDialog(null, "Enter Supplier Name:");
                String supplierContact = JOptionPane.showInputDialog(null, "Enter Supplier Contact Number:");

                String memo = id + "," + hardwareName + "," + category + "," + brand + "," + sizeCapacity + "," + price + "," + releaseDate + "," + datePurchased + "," + supplierName + "," + supplierContact;
                menu.save(memo);
            });

            JButton updateButton = new JButton(UserMenu.UPDATE.toString() + " Existing Record");
            updateButton.setBackground(new Color(204, 204, 255));
            updateButton.setFont(new Font("Arial", Font.BOLD, 12));
            updateButton.addActionListener(e -> menu.update());

            JButton deleteButton = new JButton(UserMenu.DELETE.toString() + " Existing Record");
            deleteButton.setBackground(new Color(204, 204, 255));
            deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
            deleteButton.addActionListener(e -> menu.delete());

            JButton searchButton = new JButton(UserMenu.SEARCH.toString() + " Specific Record");
            searchButton.setBackground(new Color(204, 204, 255));
            searchButton.setFont(new Font("Arial", Font.BOLD, 12));
            searchButton.addActionListener(e -> menu.searchFile());

            JButton displayButton = new JButton(UserMenu.DISPLAY.toString() + " All Record");
            displayButton.setBackground(new Color(204, 204, 255));
            displayButton.setFont(new Font("Arial", Font.BOLD, 12));
            displayButton.addActionListener(e -> menu.display());

            JButton exitButton = new JButton(UserMenu.EXIT.toString());
            exitButton.setBackground(new Color(204, 204, 255));
            exitButton.setFont(new Font("Arial", Font.BOLD, 12));
            exitButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Program successfully shutdown.");
                System.exit(0);
            });

            buttonPanel.add(createButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(searchButton);
            buttonPanel.add(displayButton);
            buttonPanel.add(exitButton);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}
