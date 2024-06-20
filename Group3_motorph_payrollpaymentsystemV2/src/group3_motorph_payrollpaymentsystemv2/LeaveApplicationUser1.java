package group3_motorph_payrollpaymentsystemv2;

import group3_motorph_payrollpaymentsystemV2.Filehandling;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danilo
 */
public class LeaveApplicationUser1 extends javax.swing.JFrame {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String FILE_NAME = "leave_applications.csv";
    private static final int MAX_LEAVE_DAYS = 30;
    public DefaultTableModel tableModel;
    private static HashMap<String, Integer> leaveBalanceMap = new HashMap<>();
    private List<LeaveDetails> employees = new ArrayList<>();

    /**
     * Creates new form LeaveApplicationUser
     */
    public LeaveApplicationUser1() throws FileNotFoundException, IOException {
        initComponents();
        String csvFile = FILE_NAME;
        csvRun(csvFile);

        showDetails();

        // Initially set the text field to not editable
        jTextFieldOthers.setEditable(false);

    }

    public String getEmployeeNumber() {

        return "4";
    }

    public String getLastName() {
        return "Garcia";
    }

    public String getFirstName() {
        return "Manuel III";
    }

    public String getuserFullName() {
        String userFullName = getLastName() + "," + getFirstName();
        return userFullName;
    }

    public void csvRun(String csvFile) throws FileNotFoundException, IOException {
        List<String[]> records = Filehandling.readCSV(csvFile);
        List<LeaveDetails> employees = parseRecords(records);
        informationTable(employees);
    }

    // Method to parse records
    public List<LeaveDetails> parseRecords(List<String[]> records) {

        for (String[] record : records) {
            String employeeNumber = record[0];
            String lastName = record[1];
            String firstName = record[2];
            String startDate = record[3];
            String endDate = record[4];
            String leaveDay = record[5];
            String leaveReason = record[6];
            String leaveStatus = record[7];

            LeaveDetails leaveDetails = new LeaveDetails(employeeNumber, lastName, firstName,
                    startDate, endDate, leaveDay,
                    leaveReason, leaveStatus);
            employees.add(leaveDetails);
        }

        return employees;
    }

    private void informationTable(List<LeaveDetails> employees) {
        DefaultTableModel tableModel = (DefaultTableModel) jTableLeaveApplications.getModel();
        tableModel.setRowCount(0); // Clear existing rows
        for (LeaveDetails employee : employees) {
            if (employee.getEmployeeNumber().equals(getEmployeeNumber())) {
                tableModel.addRow(new Object[]{
                    employee.getStartDate(),
                    employee.getEndDate(),
                    employee.getLeaveDay(),
                    employee.getLeaveReason(),
                    employee.getLeaveStatus()
                });
            }
        }
    }

    public void showDetails() {
        jTextFieldEmployeeNum.setText(getEmployeeNumber());
        jTextFieldEmployeeName.setText(getuserFullName());
    }

    public void openUserProfile() {

        try {
            setVisible(false);
            EmployeeProfileUser profileUser = new EmployeeProfileUser(getEmployeeNumber());
            profileUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(LeaveApplicationUser1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String[] sendInformation() {
        String[] userInformation = new String[3];;

        userInformation[0] = getEmployeeNumber() ;
        userInformation[1] = "Giltendez";
        userInformation[2] = "Danilo";

        return userInformation;

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

    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonVacationLeave1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonLeaveApp = new javax.swing.JButton();
        jButtonProfile = new javax.swing.JButton();
        jButtonPayroll = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldEmployeeNum = new javax.swing.JTextField();
        jTextFieldEmployeeName = new javax.swing.JTextField();
        jButtonSubmit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldOthers = new javax.swing.JTextField();
        jComboBoxLeaveReason = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jDateChooserEndDate = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLeaveApplications = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();

        jRadioButtonVacationLeave1.setText("Vacation Leave");
        jRadioButtonVacationLeave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVacationLeave1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonLeaveApp.setText("Leave Application");
        jButtonLeaveApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLeaveAppActionPerformed(evt);
            }
        });

        jButtonProfile.setText("Profile");
        jButtonProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProfileActionPerformed(evt);
            }
        });

        jButtonPayroll.setText("Payroll");
        jButtonPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonPayroll)
                    .addComponent(jButtonProfile))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jButtonLeaveApp)
                .addGap(22, 22, 22))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jButtonProfile)
                .addGap(44, 44, 44)
                .addComponent(jButtonPayroll)
                .addGap(33, 33, 33)
                .addComponent(jButtonLeaveApp)
                .addContainerGap(261, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 460));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("End Date :");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        jLabel1.setText("Employee Name:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jLabel2.setText("Employee ID:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        jLabel3.setText("Start Date :");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        jTextFieldEmployeeNum.setEditable(false);
        jTextFieldEmployeeNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmployeeNumActionPerformed(evt);
            }
        });
        jPanel3.add(jTextFieldEmployeeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 395, -1));

        jTextFieldEmployeeName.setEditable(false);
        jTextFieldEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmployeeNameActionPerformed(evt);
            }
        });
        jPanel3.add(jTextFieldEmployeeName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 390, -1));

        jButtonSubmit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSubmit.setText("Submit Leave Application");
        jButtonSubmit.setToolTipText("");
        jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 400, -1));

        jLabel6.setText("Reason for Leave :");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));

        jTextFieldOthers.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jTextFieldOthers.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextFieldOthers.setText("( if Others, specify reason )");
        jTextFieldOthers.setToolTipText("");
        jTextFieldOthers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldOthersActionPerformed(evt);
            }
        });
        jPanel3.add(jTextFieldOthers, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 150, -1));

        jComboBoxLeaveReason.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Sick Leave", "Vacation Leave", "Maternity Leave", "Paternity Leave", "Others" }));
        jComboBoxLeaveReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLeaveReasonActionPerformed(evt);
            }
        });
        jComboBoxLeaveReason.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jComboBoxLeaveReasonKeyTyped(evt);
            }
        });
        jPanel3.add(jComboBoxLeaveReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 130, -1));

        jLabel7.setText("Specify Reason :");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 136, -1, 30));

        jDateChooserStartDate.setDateFormatString("yyyy-MM-dd");
        jDateChooserStartDate.setMinSelectableDate(new java.util.Date(-62135794702000L));
        jPanel3.add(jDateChooserStartDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 210, -1));

        jDateChooserEndDate.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(jDateChooserEndDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 210, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 570, 200));

        jTableLeaveApplications.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Start Date", "End Date", "Leave Days", "Reason for Leave", "Status"
            }
        ));
        jTableLeaveApplications.setCellSelectionEnabled(false);
        jTableLeaveApplications.setRowSelectionAllowed(true);
        jTableLeaveApplications.setRowSorter(null);
        jScrollPane1.setViewportView(jTableLeaveApplications);
        jTableLeaveApplications.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 560, 110));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Leave Application");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 580, 39));

        jButtonEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEdit.setText("Edit");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, -1, -1));

        jButtonDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 400, -1, -1));

        jButtonSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 400, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldEmployeeNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextFieldEmployeeNumActionPerformed

    private void jTextFieldEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmployeeNameActionPerformed

    private void jButtonPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayrollActionPerformed
        // TODO add your handling code here:
/*
        try {
            // TODO add your handling code here:
            String[] employeeInformation = sendInformation();
            PayrollUser payrollUser = new PayrollUser(employeeInformation);
            setVisible(false);
            payrollUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeProfileUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         */
    }//GEN-LAST:event_jButtonPayrollActionPerformed

    private void jButtonProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfileActionPerformed
        openUserProfile();
    }//GEN-LAST:event_jButtonProfileActionPerformed

    private void jButtonLeaveAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveAppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLeaveAppActionPerformed

    private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitActionPerformed
        // TODO add your handling code here:
        String id = jTextFieldEmployeeNum.getText();
        String reasonLeave = jComboBoxLeaveReason.getSelectedItem().toString();

        if (reasonLeave.equals("Others")) {
            reasonLeave = reasonLeave + ", " + jTextFieldOthers.getText();
        }

        // Get and format dates
        String startDate = formatDate(jDateChooserStartDate.getDate());
        String endDate = formatDate(jDateChooserEndDate.getDate());

        tableModel = (DefaultTableModel) jTableLeaveApplications.getModel();

        // Validate input fields
        if (startDate.isEmpty() || endDate.isEmpty() || reasonLeave.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int leaveDays = calculateLeaveDays(startDate, endDate);
        if (leaveDays < 0) {
            JOptionPane.showMessageDialog(null, "End date must be after start date", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int remainingLeave = leaveBalanceMap.getOrDefault(id, MAX_LEAVE_DAYS) - leaveDays;
        if (remainingLeave < 0) {
            JOptionPane.showMessageDialog(null, "Insufficient leave balance", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        leaveBalanceMap.put(id, remainingLeave);

        tableModel.addRow(new Object[]{startDate, endDate, leaveDays, reasonLeave, "Pending"});

        // Clear input fields
        jDateChooserStartDate.setDate(null);
        jDateChooserEndDate.setDate(null);
        jComboBoxLeaveReason.setSelectedIndex(0);

    }//GEN-LAST:event_jButtonSubmitActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTableLeaveApplications.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 1);
        String startDate = (String) tableModel.getValueAt(selectedRow, 2);
        String endDate = (String) tableModel.getValueAt(selectedRow, 3);
        int leaveBalance = (int) tableModel.getValueAt(selectedRow, 4);

        leaveBalanceMap.put(id, leaveBalance + calculateLeaveDays(startDate, endDate));
        tableModel.removeRow(selectedRow);

    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
  // Check if a row is selected
    int selectedRow = jTableLeaveApplications.getSelectedRow();
    if (selectedRow != -1) {
        // Get the leave status of the selected row
        String leaveStatus = (String) tableModel.getValueAt(selectedRow, 5); 

        if ("Pending".equals(leaveStatus)) {
            // Show confirmation dialog
            int response = JOptionPane.showConfirmDialog(null, "Do you want to proceed with deleting the entry?",
                    "Delete Entry Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Check the user's response
            if (response == JOptionPane.YES_OPTION) {
                // Get employee number from the selected row
                String employeeNumber = (String) tableModel.getValueAt(selectedRow, 0); // Assuming employee number is in column 0

                // Get start and end date from the selected row
                String startDate = (String) tableModel.getValueAt(selectedRow, 3); // Assuming start date is in column 3
                String endDate = (String) tableModel.getValueAt(selectedRow, 4); // Assuming end date is in column 4

                // Calculate leave days (you need to implement this method)
                int leaveDays = calculateLeaveDays(startDate, endDate);

                // Update leave balance (assuming leaveBalanceMap is a predefined map)
                leaveBalanceMap.put(employeeNumber, leaveBalanceMap.get(employeeNumber) + leaveDays);

                // Remove the selected row from the table
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Only entries with status 'Pending' can be deleted.",
                    "Delete Entry Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Please select a row to delete.",
                "No Row Selected",
                JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:

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
            JOptionPane.showMessageDialog(null, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jRadioButtonVacationLeave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonVacationLeave1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonVacationLeave1ActionPerformed

    private void jTextFieldOthersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldOthersActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldOthersActionPerformed

    private void jComboBoxLeaveReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLeaveReasonActionPerformed
        // TODO add your handling code here:

        if ("Others".equals(jComboBoxLeaveReason.getSelectedItem().toString())) {
            jTextFieldOthers.setEditable(true);
        } else {
            jTextFieldOthers.setEditable(false);
            jTextFieldOthers.setText(""); // Clear the text field when not editable
        }
    }//GEN-LAST:event_jComboBoxLeaveReasonActionPerformed

    private void jComboBoxLeaveReasonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxLeaveReasonKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxLeaveReasonKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LeaveApplicationUser1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaveApplicationUser1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaveApplicationUser1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaveApplicationUser1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LeaveApplicationUser1().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(LeaveApplicationUser1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonLeaveApp;
    private javax.swing.JButton jButtonPayroll;
    private javax.swing.JButton jButtonProfile;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSubmit;
    private javax.swing.JComboBox<String> jComboBoxLeaveReason;
    private com.toedter.calendar.JDateChooser jDateChooserEndDate;
    private com.toedter.calendar.JDateChooser jDateChooserStartDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButtonVacationLeave1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLeaveApplications;
    private javax.swing.JTextField jTextFieldEmployeeName;
    private javax.swing.JTextField jTextFieldEmployeeNum;
    private javax.swing.JTextField jTextFieldOthers;
    // End of variables declaration//GEN-END:variables
}
