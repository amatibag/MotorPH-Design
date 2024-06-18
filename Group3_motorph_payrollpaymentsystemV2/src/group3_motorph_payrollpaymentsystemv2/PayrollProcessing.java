/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package group3_motorph_payrollpaymentsystemv2;

import com.opencsv.CSVReader;
import static group3_motorph_payrollpaymentsystemv2.StatutoryDeductions.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import group3_motorph_payrollpaymentsystemV2.Filehandling;

/**
 *
 * @author danilo
 */
public class PayrollProcessing extends javax.swing.JFrame {

    public List<EmployeeHoursWorked> employeeData = new ArrayList<>();
 

    /**
     * Creates new form PayrollProcessing
     *
     * @throws java.io.FileNotFoundException
     */
    public PayrollProcessing() throws FileNotFoundException, IOException {

        initComponents();
        String csvWorkedHoursFile = "Employee_Hours_Worked.csv";
        List<String[]> records = Filehandling.readCSV(csvWorkedHoursFile);
        parseRecordsHoursWorked(records);
        populatecomboboxCoveredPeriods();

    }

    public void parseRecordsHoursWorked(List<String[]> records) {

        for (String[] record : records) {
            String employeeNumber = record[0];
            String lastName = record[1];
            String firstName = record[2];
            String coveredPeriod = record[3];
            String noOfHoursWorked = record[4];

            EmployeeHoursWorked employeehoursWorked = new EmployeeHoursWorked(employeeNumber, lastName, firstName, coveredPeriod, noOfHoursWorked);
            employeeData.add(employeehoursWorked);

        }

    }

    private String[] populateByMonth() {
        String[] months = {"",
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };

        return months;
    }

    private List<Integer> populateByYear() {
        List<Integer> years = new ArrayList<>();
        int currentYear = Year.now().getValue();
        for (int i = currentYear - 4; i <= currentYear; i++) {  //assuming company store data for 5years
            years.add(i);
        }
        return years;
    }

    private void populatecomboboxCoveredPeriods() {

        String[] months = populateByMonth();
        for (String month : months) {
            jComboBoxCoveredMonth.addItem(month);
        }

        List<Integer> years = populateByYear();
        for (int i = 0; i < years.size(); i++) {
            jComboBoxCoveredYear.addItem(years.get(i).toString());
        }

    }

    public Integer matchWorkedHours() {
        String searchId = jTextFieldEmployeeNum.getText();
        String searchPeriod = jComboBoxCoveredMonth.getSelectedItem().toString() + " " + jComboBoxCoveredYear.getSelectedItem().toString();

        for (int i = 0; i < employeeData.size(); i++) {
            EmployeeHoursWorked employeehoursWorked = employeeData.get(i);
            if (employeehoursWorked.getEmployeeNumber().equals(searchId) && employeehoursWorked.getCoveredPeriod().equals(searchPeriod)) {
                return i;

            }
        }
        JOptionPane.showMessageDialog(null, "No record for the selected covered period");

        return -1; // Return -1 if no match is found
    }

    public String[] createHeadertoRecords() {

        String[] headers = {
            "Employee No.",
            "Last Name",
            "First Name",
            "Worked Hours",
            "Basic Salary",
            "Hourly Rate",
            "Gross Income",
            "SSS Deduction ",
            "Philhealth Deduction ",
            "Pagibig Deduction",
            "Withholding Tax",
            "Covered Month",
            "Covered Year",
            "Benefits",
            "Total Deductions",
            "Take-Home pay"};

        return headers;
    }

    public static boolean isPayrollRecordsCsvEmpty(String csvFile) {

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine = csvReader.readNext();

            // If nextLine is null, the file is empty
            return nextLine == null; // check if statement is true
        } catch (IOException e) {

            return true; // return boolean true if csvFile can't be found in the directory. 
        }
    }

    public void updatePayrollRecords() throws IOException {
        String csvPayrollFile = "PayrollRecords.csv";

        boolean isEmpty = isPayrollRecordsCsvEmpty(csvPayrollFile);

        String[] entry = {
            jTextFieldEmployeeNum.getText(),
            jTextFieldLastName.getText(),
            jTextFieldFirstName.getText(),
            jTextFieldWorkedHours.getText(),
            jTextFieldBasicSalary.getText(),
            jTextFieldHourlyRate.getText(),
            jTextFieldGrossIncome.getText(),
            jTextSssDeduction.getText(),
            jTextFieldPhilHealthDeduction.getText(),
            jTextFieldPagibigDeduction.getText(),
            jTextFieldWHT.getText(),
            jComboBoxCoveredMonth.getSelectedItem().toString(),
            jComboBoxCoveredYear.getSelectedItem().toString(),
            jTextFieldBenefits.getText(),
            jTextFieldTotalDeductions.getText(),
            jTextFieldTakeHomePay.getText()
        };

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPayrollFile, true))) {

            if (isEmpty) {
                writer.writeNext(createHeadertoRecords());
            }

            writer.writeNext(entry);
            JOptionPane.showMessageDialog(null, "Entry added successfully.");
        } catch (IOException ex) {

        }

    }

    public void processPayroll() {

        double basisSalary = Double.parseDouble(jTextFieldBasicSalary.getText());
        double totalDaysMonth = 21; // used to be consistent with the hourlyrate in the MotorPH website.         
        double maxDayHours = 8;//maximum of working hours per day

        double hourlyRate = basisSalary / totalDaysMonth / maxDayHours;
        String formattedHourlyRate = String.format("%.2f", hourlyRate); // Format to 2 decimal places
        jTextFieldHourlyRate.setText(formattedHourlyRate);

        double workedHours = Double.parseDouble(jTextFieldWorkedHours.getText());

        double grossIncome = hourlyRate * workedHours;
        String formattedGrossIncome = String.format("%.2f", grossIncome); // Format to 2 decimal places
        jTextFieldGrossIncome.setText(formattedGrossIncome);
        jTextFieldGrossIncome_S.setText(formattedGrossIncome);

        double sssDeduction = calculateSSS(grossIncome);
        String formattedSssDeduction = String.format("%.2f", sssDeduction);
        jTextSssDeduction.setText(formattedSssDeduction);

        double philhealthDeduction = calculatePhilHealth(grossIncome);
        String formattedPhilhealthDeduction = String.format("%.2f", philhealthDeduction);
        jTextFieldPhilHealthDeduction.setText(formattedPhilhealthDeduction);

        double pagibigDeduction = calculatePagIbig(grossIncome);
        String formattedPagibigDeduction = String.format("%.2f", pagibigDeduction);
        jTextFieldPagibigDeduction.setText(formattedPagibigDeduction);

        double benefitDeductions;
        benefitDeductions = sssDeduction + pagibigDeduction + philhealthDeduction;

        double taxableMonthlyPay;
        taxableMonthlyPay = grossIncome - benefitDeductions;

        double whtax = calculateWHTax(taxableMonthlyPay);
        String formattedWhtax = String.format("%.2f", whtax);
        jTextFieldWHT.setText(formattedWhtax);

        double totalDeduction;
        totalDeduction = whtax + benefitDeductions;
        String formattedTotalDeduction = String.format("%.2f", totalDeduction);
        jTextFieldTotalDeductions.setText(formattedTotalDeduction);

        double totalBenefits;
        totalBenefits = Double.parseDouble(jTextFieldBenefits.getText());

        double takeHomePay;
        takeHomePay = grossIncome - totalDeduction + totalBenefits;
        String formattedTakeHomePay = String.format("%.2f", takeHomePay);
        jTextFieldTakeHomePay.setText(formattedTakeHomePay);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldEmployeeNum = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldLastName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxCoveredYear = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldWorkedHours = new javax.swing.JTextField();
        jButtonCompute = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldGrossIncome = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextSssDeduction = new javax.swing.JTextField();
        jTextFieldPhilHealthDeduction = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldPagibigDeduction = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldWHT = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldGrossIncome_S = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldBenefits = new javax.swing.JTextField();
        jTextFieldTotalDeductions = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldTakeHomePay = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldHourlyRate = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldBasicSalary = new javax.swing.JTextField();
        jComboBoxCoveredMonth = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButtonAddtoRecords = new javax.swing.JButton();

        jLabel21.setText("Month");

        jLabel22.setText("Month");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Employee No.");

        jTextFieldEmployeeNum.setEditable(false);
        jTextFieldEmployeeNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmployeeNumActionPerformed(evt);
            }
        });
        jTextFieldEmployeeNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldEmployeeNumKeyTyped(evt);
            }
        });

        jLabel4.setText("Last Name");

        jTextFieldLastName.setEditable(false);
        jTextFieldLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLastNameActionPerformed(evt);
            }
        });

        jLabel5.setText("First Name");

        jTextFieldFirstName.setEditable(false);
        jTextFieldFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFirstNameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("PAYROLL DETAILS");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel6.setText("Covered Period");

        jComboBoxCoveredYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCoveredYearActionPerformed(evt);
            }
        });

        jLabel7.setText("Worked Hours");

        jTextFieldWorkedHours.setEditable(false);
        jTextFieldWorkedHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWorkedHoursActionPerformed(evt);
            }
        });

        jButtonCompute.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCompute.setText("Compute");
        jButtonCompute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonComputeActionPerformed(evt);
            }
        });

        jLabel8.setText("Gross Income");

        jTextFieldGrossIncome.setEditable(false);
        jTextFieldGrossIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGrossIncomeActionPerformed(evt);
            }
        });

        jLabel9.setText("SSS Deduction ");

        jTextSssDeduction.setEditable(false);
        jTextSssDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextSssDeductionActionPerformed(evt);
            }
        });

        jTextFieldPhilHealthDeduction.setEditable(false);
        jTextFieldPhilHealthDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPhilHealthDeductionActionPerformed(evt);
            }
        });

        jLabel10.setText("PhilHealth Deduction ");

        jLabel11.setText("Pagibig Deduction ");

        jTextFieldPagibigDeduction.setEditable(false);
        jTextFieldPagibigDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPagibigDeductionActionPerformed(evt);
            }
        });

        jLabel12.setText("Withholding Tax");

        jTextFieldWHT.setEditable(false);
        jTextFieldWHT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWHTActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel13.setText("Summary");

        jLabel14.setText("Gross Income");

        jTextFieldGrossIncome_S.setEditable(false);
        jTextFieldGrossIncome_S.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGrossIncome_SActionPerformed(evt);
            }
        });

        jLabel15.setText("Benefits");

        jTextFieldBenefits.setEditable(false);
        jTextFieldBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBenefitsActionPerformed(evt);
            }
        });

        jTextFieldTotalDeductions.setEditable(false);
        jTextFieldTotalDeductions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTotalDeductionsActionPerformed(evt);
            }
        });

        jLabel16.setText("Total Deductions");

        jTextFieldTakeHomePay.setEditable(false);
        jTextFieldTakeHomePay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTakeHomePayActionPerformed(evt);
            }
        });

        jLabel17.setText("TAKE-HOME PAY");

        jTextFieldHourlyRate.setEditable(false);
        jTextFieldHourlyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHourlyRateActionPerformed(evt);
            }
        });

        jLabel18.setText("Basic Salary");

        jLabel19.setText("Hourly Rate");

        jTextFieldBasicSalary.setEditable(false);
        jTextFieldBasicSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBasicSalaryActionPerformed(evt);
            }
        });

        jComboBoxCoveredMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxCoveredMonthMouseClicked(evt);
            }
        });
        jComboBoxCoveredMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCoveredMonthActionPerformed(evt);
            }
        });

        jLabel20.setText("Month");

        jLabel23.setText("Year");

        jButtonAddtoRecords.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddtoRecords.setText("Add to Records");
        jButtonAddtoRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddtoRecordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldGrossIncome, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel3))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextFieldWorkedHours, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldEmployeeNum, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(27, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldPagibigDeduction, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                            .addComponent(jTextFieldWHT)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldPhilHealthDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldHourlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextSssDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel15)
                                                    .addComponent(jLabel14))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextFieldGrossIncome_S, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextFieldBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextFieldTotalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextFieldTakeHomePay, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel13))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jComboBoxCoveredMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel20)
                                            .addComponent(jLabel6))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel23)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jButtonCompute)
                                                .addComponent(jComboBoxCoveredYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(8, 8, 8))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(jButtonAddtoRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jLabel1)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel2)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxCoveredYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxCoveredMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCompute, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldGrossIncome_S, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14)))
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldTotalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldTakeHomePay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(26, 26, 26)
                        .addComponent(jButtonAddtoRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel1))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldEmployeeNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jTextFieldLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldWorkedHours, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextFieldHourlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldGrossIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextSssDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldPhilHealthDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldPagibigDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextFieldWHT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldEmployeeNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmployeeNumActionPerformed

    private void jTextFieldEmployeeNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldEmployeeNumKeyTyped

    private void jTextFieldLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLastNameActionPerformed

    private void jTextFieldFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFirstNameActionPerformed

    private void jTextFieldWorkedHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWorkedHoursActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextFieldWorkedHoursActionPerformed

    private void jButtonComputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComputeActionPerformed
        // TODO add your handling code here:

        int searchIndex = matchWorkedHours();
        String workedHours = employeeData.get(searchIndex).getNoOfHoursWorked();
        jTextFieldWorkedHours.setText(workedHours);

        processPayroll();

    }//GEN-LAST:event_jButtonComputeActionPerformed

    private void jTextFieldGrossIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGrossIncomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGrossIncomeActionPerformed

    private void jTextSssDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextSssDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextSssDeductionActionPerformed

    private void jTextFieldPhilHealthDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPhilHealthDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPhilHealthDeductionActionPerformed

    private void jTextFieldPagibigDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPagibigDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPagibigDeductionActionPerformed

    private void jTextFieldWHTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWHTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldWHTActionPerformed

    private void jTextFieldGrossIncome_SActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGrossIncome_SActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGrossIncome_SActionPerformed

    private void jTextFieldBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBenefitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBenefitsActionPerformed

    private void jTextFieldTotalDeductionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTotalDeductionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTotalDeductionsActionPerformed

    private void jTextFieldTakeHomePayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTakeHomePayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTakeHomePayActionPerformed

    private void jTextFieldHourlyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHourlyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldHourlyRateActionPerformed

    private void jTextFieldBasicSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBasicSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBasicSalaryActionPerformed

    private void jButtonAddtoRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddtoRecordsActionPerformed
        try {
            // TODO add your handling code here:

            updatePayrollRecords();
        } catch (IOException ex) {
            Logger.getLogger(PayrollProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonAddtoRecordsActionPerformed

    private void jComboBoxCoveredMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCoveredMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCoveredMonthActionPerformed

    private void jComboBoxCoveredYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCoveredYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCoveredYearActionPerformed

    private void jComboBoxCoveredMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxCoveredMonthMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxCoveredMonthMouseClicked

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
            java.util.logging.Logger.getLogger(PayrollProcessing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollProcessing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollProcessing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollProcessing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PayrollProcessing().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeProfile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddtoRecords;
    private javax.swing.JButton jButtonCompute;
    private javax.swing.JComboBox<String> jComboBoxCoveredMonth;
    private javax.swing.JComboBox<String> jComboBoxCoveredYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JTextField jTextFieldBasicSalary;
    public javax.swing.JTextField jTextFieldBenefits;
    public javax.swing.JTextField jTextFieldEmployeeNum;
    public javax.swing.JTextField jTextFieldFirstName;
    public javax.swing.JTextField jTextFieldGrossIncome;
    public javax.swing.JTextField jTextFieldGrossIncome_S;
    public javax.swing.JTextField jTextFieldHourlyRate;
    public javax.swing.JTextField jTextFieldLastName;
    public javax.swing.JTextField jTextFieldPagibigDeduction;
    public javax.swing.JTextField jTextFieldPhilHealthDeduction;
    public javax.swing.JTextField jTextFieldTakeHomePay;
    public javax.swing.JTextField jTextFieldTotalDeductions;
    public javax.swing.JTextField jTextFieldWHT;
    public javax.swing.JTextField jTextFieldWorkedHours;
    public javax.swing.JTextField jTextSssDeduction;
    // End of variables declaration//GEN-END:variables
}
