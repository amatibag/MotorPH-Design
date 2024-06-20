package group3_motorph_payrollpaymentsystemv2;

public class LeaveDetails {

    // Attributes
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String submittedDate;
    private String startDate;
    private String endDate;
    private String leaveDay;
    private String leaveReason;
    private String leaveStatus;

    // Constructor
    public LeaveDetails(String employeeNumber, String lastName, String firstName, String submittedDate,
            String startDate, String endDate, String leaveDay,
            String leaveReason, String leaveStatus) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.startDate = startDate;
        this.submittedDate = submittedDate;
        this.endDate = endDate;
        this.leaveDay = leaveDay;
        this.leaveReason = leaveReason;
        this.leaveStatus = leaveStatus;
    }

    // Getter and Setter methods
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    
    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(String leaveDay) {
        this.leaveDay = leaveDay;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}
