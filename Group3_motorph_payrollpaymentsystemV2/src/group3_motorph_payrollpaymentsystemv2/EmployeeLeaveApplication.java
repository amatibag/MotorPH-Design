package group3_motorph_payrollpaymentsystemv2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Pattern;
import group3_motorph_payrollpaymentsystemv2.LoginManager;

public class EmployeeLeaveApplication {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final String FILE_NAME = "leave_applications.txt";
    private static final int MAX_LEAVE_DAYS = 5;
    private static HashMap<String, Integer> leaveBalanceMap = new HashMap<>();

    // Declare GUI components as instance variables
    public JFrame frame;
    public JTextField nameField;
    public JTextField idField;
    public JTextField startDateField;
    public JTextField endDateField;
    public DefaultTableModel tableModel;
    public JTable leaveApplicationsTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeLeaveApplication::new);

    }

    public EmployeeLeaveApplication() {
        // Create the main frame
        JFrame frame = new JFrame("Employee Leave Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Create the leave form panel
        JPanel leaveFormPanel = new JPanel();
        leaveFormPanel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Employee Name:");
        nameField = new JTextField();
        JLabel idLabel = new JLabel("Employee ID:");
        idField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateField = new JTextField();
        JButton submitButton = new JButton("Submit Leave Application");

//        String id = "1";
//        idField.setText(id);
        leaveFormPanel.add(nameLabel);
        leaveFormPanel.add(nameField);
        leaveFormPanel.add(idLabel);
        leaveFormPanel.add(idField);
        leaveFormPanel.add(startDateLabel);
        leaveFormPanel.add(startDateField);
        leaveFormPanel.add(endDateLabel);
        leaveFormPanel.add(endDateField);
        leaveFormPanel.add(new JLabel("")); // Placeholder for grid layout
        leaveFormPanel.add(submitButton);

        // Create the display panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());

        // Define column names for the table
        String[] columnNames = {"Employee Name", "Employee ID", "Start Date", "End Date", "Leave Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable leaveApplicationsTable = new JTable(tableModel);
        leaveApplicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(leaveApplicationsTable);

        // Create the edit/delete buttons
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        displayPanel.add(scrollPane, BorderLayout.CENTER);
        displayPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to frame
        frame.add(leaveFormPanel, BorderLayout.NORTH);
        frame.add(displayPanel, BorderLayout.CENTER);

        // Handle submit button click
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();

                // Validate input fields
                if (name.isEmpty() || id.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!isValidDate(startDate) || !isValidDate(endDate)) {
                    JOptionPane.showMessageDialog(frame, "Dates must be in the format YYYY-MM-DD and valid", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int leaveDays = calculateLeaveDays(startDate, endDate);
                if (leaveDays < 0) {
                    JOptionPane.showMessageDialog(frame, "End date must be after start date", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int remainingLeave = leaveBalanceMap.getOrDefault(id, MAX_LEAVE_DAYS) - leaveDays;
                if (remainingLeave < 0) {
                    JOptionPane.showMessageDialog(frame, "Insufficient leave balance", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                leaveBalanceMap.put(id, remainingLeave);

                // Add data to the table
                tableModel.addRow(new Object[]{name, id, startDate, endDate, remainingLeave});

                // Clear input fields
                nameField.setText("");
                idField.setText("");
                startDateField.setText("");
                endDateField.setText("");
            }
        });

        // Handle edit button click
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = leaveApplicationsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = (String) tableModel.getValueAt(selectedRow, 0);
                String id = (String) tableModel.getValueAt(selectedRow, 1);
                String startDate = (String) tableModel.getValueAt(selectedRow, 2);
                String endDate = (String) tableModel.getValueAt(selectedRow, 3);
                int leaveBalance = (int) tableModel.getValueAt(selectedRow, 4);

                nameField.setText(name);
                idField.setText(id);
                startDateField.setText(startDate);
                endDateField.setText(endDate);

                leaveBalanceMap.put(id, leaveBalance + calculateLeaveDays(startDate, endDate));
                tableModel.removeRow(selectedRow);
            }
        });

        // Handle delete button click
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = leaveApplicationsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String id = (String) tableModel.getValueAt(selectedRow, 1);
                    String startDate = (String) tableModel.getValueAt(selectedRow, 2);
                    String endDate = (String) tableModel.getValueAt(selectedRow, 3);
                    int leaveDays = calculateLeaveDays(startDate, endDate);
                    leaveBalanceMap.put(id, leaveBalanceMap.get(id) + leaveDays);
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        // Handle save button click
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String name = (String) tableModel.getValueAt(i, 0);
                        String id = (String) tableModel.getValueAt(i, 1);
                        String startDate = (String) tableModel.getValueAt(i, 2);
                        String endDate = (String) tableModel.getValueAt(i, 3);
                        int leaveBalance = (int) tableModel.getValueAt(i, 4);
                        writer.write(name + "," + id + "," + startDate + "," + endDate + "," + leaveBalance);
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(frame, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Handle load button click
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length == 5) {
                            String name = data[0];
                            String id = data[1];
                            String startDate = data[2];
                            String endDate = data[3];
                            int leaveBalance = Integer.parseInt(data[4]);
                            tableModel.addRow(new Object[]{name, id, startDate, endDate, leaveBalance});
                            leaveBalanceMap.put(id, leaveBalance);
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Data loaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    private boolean isValidDate(String dateStr) {
        if (!DATE_PATTERN.matcher(dateStr).matches()) {
            return false;
        }
        try {
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private int calculateLeaveDays(String startDateStr, String endDateStr) {
        try {
            long startDate = DATE_FORMAT.parse(startDateStr).getTime();
            long endDate = DATE_FORMAT.parse(endDateStr).getTime();
            return (int) ((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
        } catch (ParseException e) {
            return -1;
        }
    }

}
