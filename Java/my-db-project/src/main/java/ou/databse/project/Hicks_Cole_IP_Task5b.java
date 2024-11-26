package ou.databse.project;

// Cole Hicks
// CS 4513

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.math.BigDecimal;

public class Hicks_Cole_IP_Task5b {

    // Helper method to validate date format
    private static boolean isInvalidDate(String dateString) {
        try {
            LocalDate.parse(dateString); // Try to parse the date
            return false;
        } catch (DateTimeParseException e) { // Catch any parsing exceptions
            System.err.println("Invalid date format: " + dateString);
            return true;
        }
    }

    // Helper method to get user input
    private static String getInput(Scanner keyboard, String prompt) {
        System.out.println(prompt);
        return keyboard.nextLine();
    }

    // Helper method to insert emergency contacts
    private static void insertEmergencyContact(Connection dbConnection, Scanner keyboard, String ssn, String personType) throws SQLException {
        String tableName;
        switch (personType.toLowerCase()) {
            case "client" -> tableName = "EmergencyContactClient"; // Table name for client emergency contacts
            case "volunteer" -> tableName = "EmergencyContactVolunteer"; // Table name for volunteer emergency contacts
            case "employee" -> tableName = "EmergencyContactEmployee"; // Table name for employee emergency contacts
            case "donor" -> tableName = "EmergencyContactDonor"; // Table name for donor emergency contacts
            default -> throw new IllegalArgumentException("Invalid person type: " + personType); // Invalid person type
        }

        String emergencyContactSql = String.format("INSERT INTO %s (ssn_%s, ec_name, ec_phone_number, ec_relationship) VALUES (?, ?, ?, ?)",
                tableName, personType.toLowerCase());

        // Get the number of emergency contacts
        int numEmergencyContacts;
        while (true) {
            try {
                System.out.println("How many emergency contacts does the " + personType + " have?");
                numEmergencyContacts = Integer.parseInt(keyboard.nextLine()); 
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Insert emergency contacts
        for (int i = 0; i < numEmergencyContacts; i++) {
            String ecName = getInput(keyboard, "Please enter the name of emergency contact " + (i + 1) + ":");
            String ecPhoneNumber = getInput(keyboard, "Please enter the phone number of emergency contact " + (i + 1) + ":");
            String ecRelationship = getInput(keyboard, "Please enter the relationship of emergency contact " + (i + 1) + ":");

            // Prepare the SQL statement
            try (PreparedStatement ecStmt = dbConnection.prepareStatement(emergencyContactSql)) {
                ecStmt.setString(1, ssn); // Set the SSN
                ecStmt.setString(2, ecName); // Set the emergency contact name
                ecStmt.setString(3, ecPhoneNumber); // Set the emergency contact phone number
                ecStmt.setString(4, ecRelationship); // Set the emergency contact relationship
                ecStmt.executeUpdate(); // Execute the SQL statement
            }
        }
    }

    // Query 1: Insert a new team into the database
    private static void insertTeam(Connection dbConnection, Scanner keyboard) throws SQLException {
        String teamName = getInput(keyboard, "Please enter the team name that will be inserted:");
        String teamType = getInput(keyboard, "Please enter the team type that will be inserted:");
        String dateOfCreation = getInput(keyboard, "Please enter the date when the team was created. Use the format yyyy-MM-dd:");

        if (isInvalidDate(dateOfCreation)) {
            System.err.println("The date format you entered is not accepted. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        String sqlQuery = "{call insertTeam(?, ?, ?)}";
        try (CallableStatement callableStmt = dbConnection.prepareCall(sqlQuery)) {
            callableStmt.setString(1, teamName); // Set the team name
            callableStmt.setString(2, teamType); // Set the team type
            callableStmt.setString(3, dateOfCreation); // Set the date of creation

            callableStmt.execute(); // Execute the SQL statement
            System.out.println("The team was inserted successfully into the database!");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting the team: " + e.getMessage());
            throw e;
        }
    }

    // Query 2: Insert a new client into the database
    private static void insertClientIntoDB(Connection dbConnection, Scanner keyboard) throws SQLException {
        String clientSSN = getInput(keyboard, "Please enter the client's social security number:");
        String clientName = getInput(keyboard, "Please enter the client's name:");
        String clientGender = getInput(keyboard, "Please enter the client's gender:");
        String clientProfession = getInput(keyboard, "Please enter the client's profession:");
        String clientMailingAddress = getInput(keyboard, "Please enter the client's mailing address:");
        String clientEmail = getInput(keyboard, "Please enter the client's email address:");
        String clientPhoneNumber = getInput(keyboard, "Please enter the client's phone number:");

        // Validate mailing list status
        int clientMailingListStatus;
        while (true) {
            try {
                clientMailingListStatus = Integer.parseInt(getInput(keyboard, "Is the client on the mailing list? (0 for no, 1 for yes):"));
                if (clientMailingListStatus == 0 || clientMailingListStatus == 1) {
                    break;
                } else {
                    System.err.println("Please enter 0 for no or 1 for yes.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter 0 or 1.");
            }
        }

        String doctorName = getInput(keyboard, "Please enter the name of the client's doctor:");
        String doctorPhoneNumber = getInput(keyboard, "Please enter the phone number of the client's doctor:");
        String clientAssignmentDate = getInput(keyboard, "Please enter the client's assignment date in the format yyyy-MM-dd:");

        // Validate assignment date
        if (isInvalidDate(clientAssignmentDate)) {
            System.err.println("The date format you entered is not accepted. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        String clientNeedType = getInput(keyboard, "Please enter the client's need type:");

        // Validate client need importance
        int clientNeedImportance;
        while (true) {
            try {
                clientNeedImportance = Integer.parseInt(getInput(keyboard, "Please rate the importance of the client's need on a scale of 1 to 10:"));
                if (clientNeedImportance >= 1 && clientNeedImportance <= 10) {
                    break;
                } else {
                    System.err.println("Please enter a number between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter an integer between 1 and 10.");
            }
        }

        String clientInsurancePolicyId = getInput(keyboard, "Please enter the client's insurance policy ID:");
        String clientInsuranceProvider = getInput(keyboard, "Please enter the name of the client's insurance provider:");
        String clientInsuranceProviderAddress = getInput(keyboard, "Please enter the address of the client's insurance provider:");
        String clientInsuranceType = getInput(keyboard, "Please enter the type of insurance the client has:");

        String sqlQuery = "{call insertClient(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try (CallableStatement callableStmt = dbConnection.prepareCall(sqlQuery)) {
            callableStmt.setString(1, clientSSN); // Set the client's SSN
            callableStmt.setString(2, clientName); // Set the client's name
            callableStmt.setString(3, clientGender); // Set the client's gender
            callableStmt.setString(4, clientProfession); // Set the client's profession
            callableStmt.setString(5, clientMailingAddress); // Set the client's mailing address
            callableStmt.setString(6, clientEmail); // Set the client's email
            callableStmt.setString(7, clientPhoneNumber); // Set the client's phone number
            callableStmt.setInt(8, clientMailingListStatus); // Set the mailing list status
            callableStmt.setString(9, doctorName); // Set the doctor's name
            callableStmt.setString(10, doctorPhoneNumber); // Set the doctor's phone number
            callableStmt.setString(11, clientAssignmentDate); // Set the assignment date
            callableStmt.setString(12, clientNeedType); // Set the client's need type
            callableStmt.setInt(13, clientNeedImportance); // Set the client's need importance
            callableStmt.setString(14, clientInsurancePolicyId); // Set the client's insurance policy ID
            callableStmt.setString(15, clientInsuranceProvider); // Set the client's insurance provider
            callableStmt.setString(16, clientInsuranceProviderAddress); // Set the client's insurance provider address
            callableStmt.setString(17, clientInsuranceType); // Set the client's insurance type

            callableStmt.execute(); // Execute the SQL statement
            System.out.println("Client information has been successfully inserted!");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting client data: " + e.getMessage());
            throw e;
        }

        // Insert emergency contacts for the client
        insertEmergencyContact(dbConnection, keyboard, clientSSN, "client");

        // Insert teams into CaresFor table
        String caresForSql = "INSERT INTO CaresFor (social_sec_number, team_name, is_client_active) VALUES (?, ?, ?)";
        int numberOfTeams;
        while (true) {
            try {
                numberOfTeams = Integer.parseInt(getInput(keyboard, "How many teams are responsible for the client's care?"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Get the names of the teams
        List<String> nameOfTeams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            nameOfTeams.add(getInput(keyboard, "Please enter the name of team " + (i + 1) + ":"));
        }

        // Insert into CaresFor table
        for (String teamName : nameOfTeams) {
            try (PreparedStatement caresForStmt = dbConnection.prepareStatement(caresForSql)) {
                caresForStmt.setString(1, clientSSN);
                caresForStmt.setString(2, teamName);

                int isActive;
                while (true) {
                    try {
                        isActive = Integer.parseInt(getInput(keyboard, "Is the client currently active for team " + teamName + "? (Enter 1 for yes, 0 for no):"));
                        if (isActive == 0 || isActive == 1) {
                            break;
                        } else {
                            System.err.println("Please enter 0 for no or 1 for yes.");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid input. Please enter 0 or 1.");
                    }
                }
                caresForStmt.setInt(3, isActive);
                caresForStmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("An error occurred while inserting into CaresFor: " + e.getMessage());
                throw e;
            }
        }

        // Insert into Has table
        String hasSql = "INSERT INTO Has (social_sec_number, insurance_policy_id) VALUES (?, ?)";
        try (PreparedStatement hasStmt = dbConnection.prepareStatement(hasSql)) {
            hasStmt.setString(1, clientSSN);
            hasStmt.setString(2, clientInsurancePolicyId);
            hasStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting into Has: " + e.getMessage());
            throw e;
        }
    }

    // Query 3: Insert a new volunteer into the database
    private static void insertVolunteerIntoDB(Connection dbConnection, Scanner keyboard) throws SQLException {
        String volunteerSSN = getInput(keyboard, "Please enter the volunteer's SSN (Social Security Number):");
        String volunteerName = getInput(keyboard, "Please enter the volunteer's full name:");
        String volunteerGender = getInput(keyboard, "Please enter the volunteer's gender (e.g., Male, Female, Other):");
        String volunteerProfession = getInput(keyboard, "Please enter the volunteer's profession (e.g., Engineer, Doctor):");
        String volunteerMailingAddress = getInput(keyboard, "Please enter the volunteer's mailing address (e.g., 123 Main St, City, State):");
        String volunteerEmail = getInput(keyboard, "Please enter the volunteer's email address (e.g., example@example.com):");
        String volunteerPhoneNumber = getInput(keyboard, "Please enter the volunteer's phone number (e.g., 123-456-7890):");

        // Validate mailing list status
        int volunteerMailingListStatus;
        while (true) {
            try {
                volunteerMailingListStatus = Integer.parseInt(getInput(keyboard, "Is the volunteer on the mailing list? (Enter 1 for yes, 0 for no):"));
                if (volunteerMailingListStatus == 0 || volunteerMailingListStatus == 1) {
                    break;
                } else {
                    System.err.println("Please enter 0 for no or 1 for yes.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter 0 or 1.");
            }
        }

        String volunteerDateJoined = getInput(keyboard, "Please enter the date the volunteer joined - in the format yyyy-MM-dd:");
        if (isInvalidDate(volunteerDateJoined)) {
            System.err.println("Invalid date format for date joined. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        String volunteerTrainingDate = getInput(keyboard, "Please enter the date that the volunteer completed training - in the format yyyy-MM-dd:");
        if (isInvalidDate(volunteerTrainingDate)) {
            System.err.println("Invalid date format for training date. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        String volunteerTrainingLocation = getInput(keyboard, "Please enter the location that the volunteer completed training:");

        String sql = "{call insertVolunteer(?,?,?,?,?,?,?,?,?,?,?)}";
        try (CallableStatement callableStatement = dbConnection.prepareCall(sql)) {
            callableStatement.setString(1, volunteerSSN); // Set the volunteer's SSN
            callableStatement.setString(2, volunteerName); // Set the volunteer's name
            callableStatement.setString(3, volunteerGender); // Set the volunteer's gender
            callableStatement.setString(4, volunteerProfession); // Set the volunteer's profession
            callableStatement.setString(5, volunteerMailingAddress); // Set the volunteer's mailing address
            callableStatement.setString(6, volunteerEmail); // Set the volunteer's email
            callableStatement.setString(7, volunteerPhoneNumber); // Set the volunteer's phone number
            callableStatement.setInt(8, volunteerMailingListStatus); // Set the mailing list status
            callableStatement.setString(9, volunteerDateJoined); // Set the date joined
            callableStatement.setString(10, volunteerTrainingDate); // Set the training date
            callableStatement.setString(11, volunteerTrainingLocation); // Set the training location

            callableStatement.execute(); // Execute the SQL statement
            System.out.println("Volunteer information has been successfully inserted into the database!");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting volunteer data: " + e.getMessage());
            throw e;
        }

        // Insert emergency contacts for the volunteer
        insertEmergencyContact(dbConnection, keyboard, volunteerSSN, "volunteer");

        // Insert team information
        List<String> teamNames = new ArrayList<>();
        int numberOfTeams;
        while (true) {
            try {
                numberOfTeams = Integer.parseInt(getInput(keyboard, "How many teams does the volunteer serve on?"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Get the names of the teams
        for (int i = 0; i < numberOfTeams; i++) {
            teamNames.add(getInput(keyboard, "Please enter the name of team " + (i + 1) + ":"));
        }

        // Insert into ServesOn table
        for (String teamName : teamNames) {
            String servesOnSql = "INSERT INTO ServesOn (social_sec_number, team_name, month_served, hours_served, is_active) VALUES (?, ?, ?, ?, ?)";

            // Get the number of months served
            int monthsServed;
            while (true) {
                try {
                    monthsServed = Integer.parseInt(getInput(keyboard, "Please enter the number of months the volunteer has served on team " + teamName + ":"));
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number. Please enter a valid integer.");
                }
            }

            // Get the active status
            int isActive;
            while (true) {
                try {
                    isActive = Integer.parseInt(getInput(keyboard, "Is the volunteer currently active for team " + teamName + "? (Enter 1 for yes, 0 for no):"));
                    if (isActive == 0 || isActive == 1) {
                        break;
                    } else {
                        System.err.println("Please enter 0 for no or 1 for yes.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input. Please enter 0 or 1.");
                }
            }

            // Insert into ServesOn table
            for (int j = 0; j < monthsServed; j++) {
                String servingMonth = getInput(keyboard, "Please enter the serving month for volunteer on team " + teamName + ":");
                String servedHoursStr = getInput(keyboard, "Please enter the number of hours served by volunteer on team " + teamName + ":");

                // Validate the number of hours served
                int servedHours;
                try {
                    servedHours = Integer.parseInt(servedHoursStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number of hours. Please enter a valid integer.");
                    continue;
                }

                // Prepare the SQL statement
                try (PreparedStatement servesOnStmt = dbConnection.prepareStatement(servesOnSql)) {
                    servesOnStmt.setString(1, volunteerSSN); // Set the volunteer's SSN
                    servesOnStmt.setString(2, teamName); // Set the team name
                    servesOnStmt.setString(3, servingMonth); // Set the serving month
                    servesOnStmt.setInt(4, servedHours); // Set the number of hours served
                    servesOnStmt.setInt(5, isActive); // Set the active status
                    servesOnStmt.executeUpdate(); // Execute the SQL statement
                } catch (SQLException e) {
                    System.err.println("An error occurred while inserting into ServesOn: " + e.getMessage());
                    throw e;
                }
            }
        }

        // Insert into Leads table
        String leadsSql = "INSERT INTO Leads (social_sec_number, team_name, is_leader) VALUES (?, ?, ?)";
        for (String teamName : teamNames) {
            int isLeader;
            while (true) {
                try {
                    isLeader = Integer.parseInt(getInput(keyboard, "Is the volunteer the team leader for: '" + teamName + "'? (Enter 1 for yes, 0 for no):"));
                    if (isLeader == 0 || isLeader == 1) {
                        break;
                    } else {
                        System.err.println("Please enter 0 for no or 1 for yes.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input. Please enter 0 or 1.");
                }
            }

            // Prepare the SQL statement
            try (PreparedStatement leadsStmt = dbConnection.prepareStatement(leadsSql)) {
                leadsStmt.setString(1, volunteerSSN); // Set the volunteer's SSN
                leadsStmt.setString(2, teamName); // Set the team name
                leadsStmt.setInt(3, isLeader); // Set the leader status
                leadsStmt.executeUpdate();
                System.out.println("Assigned volunteer as team leader for: '" + teamName + "'.");
            } catch (SQLException e) {
                System.err.println("An error occurred while inserting into Leads: " + e.getMessage());
                throw e;
            }
        }
    }

    // Query 4: Insert the number of hours a volunteer worked this month
    private static void insertVolunteerHours(Connection dbConnection, Scanner keyboard) throws SQLException {
        String volunteerSSN = getInput(keyboard, "Please enter the volunteer's Social Security Number (SSN):");
        String volunteerTeamName = getInput(keyboard, "Please enter the volunteer's team name:");
        String monthServed = getInput(keyboard, "Please enter the month the volunteer served (e.g., January):");

        // Validate the number of hours served
        int volunteerHours;
        while (true) {
            try {
                volunteerHours = Integer.parseInt(getInput(keyboard, "Please enter the total number of hours the volunteer served:"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number of hours. Please enter a valid integer.");
            }
        }

        // Validate the active status
        int isVolunteerActive;
        while (true) {
            try {
                isVolunteerActive = Integer.parseInt(getInput(keyboard, "Is the volunteer currently active? Enter 1 for Yes or 0 for No:"));
                if (isVolunteerActive == 0 || isVolunteerActive == 1) {
                    break;
                } else {
                    System.err.println("Please enter 0 for No or 1 for Yes.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter 0 or 1.");
            }
        }

        // Prepare the SQL statement
        String sqlProcedureCall = "{call insertVolunteerHours(?,?,?,?,?)}";
        try (CallableStatement statementToExecute = dbConnection.prepareCall(sqlProcedureCall)) {
            statementToExecute.setString(1, volunteerSSN); // Set the volunteer's SSN
            statementToExecute.setString(2, volunteerTeamName); // Set the team name
            statementToExecute.setString(3, monthServed); // Set the month served
            statementToExecute.setInt(4, volunteerHours); // Set the number of hours served
            statementToExecute.setInt(5, isVolunteerActive); // Set the active status  

            statementToExecute.execute(); // Execute the SQL statement
            System.out.println("Volunteer hours have been successfully updated in the system.");
        } catch (SQLException e) {
            System.err.println("An error occurred while updating volunteer hours: " + e.getMessage());
            throw e;
        }
    }

    // Query 5: Insert an employee into the database
    private static void insertEmployee(Connection dbConnection, Scanner keyboard) throws SQLException {
        String employeeSSN = getInput(keyboard, "Please enter the employee's Social Security Number (SSN):");
        String employeeName = getInput(keyboard, "Please enter the employee's full name:");
        String employeeGender = getInput(keyboard, "Please enter the employee's gender:");
        String employeeProfession = getInput(keyboard, "Please enter the employee's profession:");
        String employeeAddress = getInput(keyboard, "Please enter the employee's mailing address:");
        String employeeEmail = getInput(keyboard, "Please enter the employee's email address:");
        String employeePhone = getInput(keyboard, "Please enter the employee's phone number:");

        // Validate mailing list status
        boolean mailingListStatus;
        while (true) {
            String mailingListInput = getInput(keyboard, "Is the employee on the mailing list? (Enter 1 for Yes, 0 for No):");
            if (mailingListInput.equals("1")) {
                mailingListStatus = true;
                break;
            } else if (mailingListInput.equals("0")) {
                mailingListStatus = false;
                break;
            } else {
                System.err.println("Invalid input. Please enter 1 for Yes or 0 for No.");
            }
        }

        // Validate salary
        BigDecimal employeeSalary;
        while (true) {
            try {
                employeeSalary = new BigDecimal(getInput(keyboard, "Please enter the employee's salary:"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid salary format. Please enter a valid decimal number.");
            }
        }


        String employeeMaritalStatus = getInput(keyboard, "Please enter the employee's marital status:");
        String employeeHireDate = getInput(keyboard, "Please enter the employee's hire date (format: yyyy-MM-dd):");

        // Validate hire date
        if (isInvalidDate(employeeHireDate)) {
            System.err.println("Invalid date format for hire date. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        String employeeInsertProcedure = "{call InsertEmployee(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement insertEmployeeStmt = dbConnection.prepareCall(employeeInsertProcedure)) {
            insertEmployeeStmt.setString(1, employeeSSN); // Set the employee's SSN
            insertEmployeeStmt.setString(2, employeeName); // Set the employee's name
            insertEmployeeStmt.setString(3, employeeGender); // Set the gender
            insertEmployeeStmt.setString(4, employeeProfession); // Set the profession
            insertEmployeeStmt.setString(5, employeeAddress); // Set the mailing address
            insertEmployeeStmt.setString(6, employeeEmail); // Set the email
            insertEmployeeStmt.setString(7, employeePhone); // Set the phone number
            insertEmployeeStmt.setBoolean(8, mailingListStatus); // Set the mailing list status
            insertEmployeeStmt.setBigDecimal(9, employeeSalary); // Set the salary
            insertEmployeeStmt.setString(10, employeeMaritalStatus); // Set the marital status
            insertEmployeeStmt.setString(11, employeeHireDate); // Set the hire date

            insertEmployeeStmt.execute(); // Execute the SQL statement
            System.out.println("Employee information has been successfully recorded in the system.");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting employee data: " + e.getMessage());
            throw e;
        }

        // Insert emergency contacts for the employee
        insertEmergencyContact(dbConnection, keyboard, employeeSSN, "employee");

        // Insert team-related reports for each team the employee belongs to
        List<String> employeeTeamNames = new ArrayList<>();
        int teamCount;
        while (true) {
            try {
                teamCount = Integer.parseInt(getInput(keyboard, "Enter the number of teams the employee belongs to:"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Get the names of the teams
        for (int i = 0; i < teamCount; i++) {
            employeeTeamNames.add(getInput(keyboard, "Enter the name of team " + (i + 1) + ":"));
        }

        // Insert reports for each team
        String reportInsertSQL = "INSERT INTO ReportsTo (social_sec_number, team_name, date_of_report, report_description) VALUES (?, ?, ?, ?)";
        for (String teamName : employeeTeamNames) {
            String reportDate = getInput(keyboard, "Enter the report date (yyyy-MM-dd) for team " + teamName + ":");
            if (isInvalidDate(reportDate)) {
                System.err.println("Invalid date format for report date. Please enter the date in yyyy-MM-dd format.");
                continue;
            }

            String reportDescription = getInput(keyboard, "Enter the report description for team " + teamName + ":");

            // Prepare the SQL statement
            try (PreparedStatement reportStmt = dbConnection.prepareStatement(reportInsertSQL)) {
                reportStmt.setString(1, employeeSSN); // Set the employee's SSN
                reportStmt.setString(2, teamName); // Set the team name
                reportStmt.setString(3, reportDate); // Set the report date
                reportStmt.setString(4, reportDescription); // Set the report description

                reportStmt.executeUpdate(); // Execute the SQL statement
            } catch (SQLException e) {
                System.err.println("An error occurred while inserting into ReportsTo: " + e.getMessage());
                throw e;
            }
        }
        System.out.println("Reports have been successfully inserted for each team.");
    }

    // Query 6: Insert an expense charged by an employee into the database
    private static void insertExpense(Connection dbConnection, Scanner keyboard) throws SQLException {
        String ssn = getInput(keyboard, "Enter Employee SSN:");

        // Validate the date
        String expenseDate = getInput(keyboard, "Please enter the date of the expense (yyyy-MM-dd):");
        if (isInvalidDate(expenseDate)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Get the expense description and amount
        String expenseDescription = getInput(keyboard, "Enter Expense Description:");
        BigDecimal expenseAmount;
        while (true) {
            try {
                expenseAmount = new BigDecimal(getInput(keyboard, "Enter Expense Amount:"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid decimal format. Please enter a valid decimal number.");
            }
        }

        // Prepare the SQL statement
        String sql = "{call InsertExpenses(?, ?, ?, ?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, ssn); // Set the employee's SSN
            stmt.setString(2, expenseDate); // Set the expense date
            stmt.setString(3, expenseDescription); // Set the expense description
            stmt.setBigDecimal(4, expenseAmount); // Set the expense amount

            stmt.execute(); // Execute the SQL statement
            System.out.println("Expense inserted successfully.");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting expense data: " + e.getMessage());
            throw e;
        }
    }

    // Query 7: Insert a donor into the database
    private static void insertDonor(Connection dbConnection, Scanner keyboard) throws SQLException {
        String donorSSN = getInput(keyboard, "Enter Donor SSN:"); 
        String donorName = getInput(keyboard, "Enter Donor name:");
        String donorGender = getInput(keyboard, "Enter the gender of the donor:");
        String donorProfession = getInput(keyboard, "Enter the profession of the donor:");
        String donorMailingAddress = getInput(keyboard, "Enter the donor mailing address:");
        String donorEmail = getInput(keyboard, "Enter the donor email:");
        String donorPhoneNumber = getInput(keyboard, "Enter the donor phone number:");

        // Validate mailing list status
        int donorMailingListStatus;
        while (true) {
            try {
                donorMailingListStatus = Integer.parseInt(getInput(keyboard, "Is the donor on the mailing list? (1 for yes, 0 for no):"));
                if (donorMailingListStatus == 0 || donorMailingListStatus == 1) {
                    break;
                } else {
                    System.err.println("Please enter 0 for no or 1 for yes.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter 0 or 1.");
            }
        }

        // Validate anonymous status
        int isAnonymous;
        while (true) {
            try {
                isAnonymous = Integer.parseInt(getInput(keyboard, "Is the donor anonymous? (1 for yes, 0 for no):"));
                if (isAnonymous == 0 || isAnonymous == 1) {
                    break;
                } else {
                    System.err.println("Please enter 0 for no or 1 for yes.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter 0 or 1.");
            }
        }

        // Get the number of donations
        int donations;
        while (true) {
            try {
                donations = Integer.parseInt(getInput(keyboard, "How many donations does the donor wish to make?"));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Insert the donor
        String sql = "{call InsertDonor(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, donorSSN); // Set the donor's SSN
            stmt.setString(2, donorName); // Set the donor's name
            stmt.setString(3, donorGender); // Set the donor's gender
            stmt.setString(4, donorProfession); // Set the donor's profession
            stmt.setString(5, donorMailingAddress); // Set the donor's mailing address
            stmt.setString(6, donorEmail); // Set the donor's email
            stmt.setString(7, donorPhoneNumber); // Set the donor's phone number
            stmt.setInt(8, donorMailingListStatus); // Set the mailing list status
            stmt.setInt(9, isAnonymous); // Set the anonymous status

            stmt.execute(); // Execute the SQL statement
            System.out.println("Donor inserted successfully.");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting donor data: " + e.getMessage());
            throw e;
        }

        // Insert emergency contacts for the donor
        insertEmergencyContact(dbConnection, keyboard, donorSSN, "donor");

        // Insert donations
        for (int i = 0; i < donations; i++) {
            String donationDate = getInput(keyboard, "Enter the date of the donation (yyyy-MM-dd):");
            if (isInvalidDate(donationDate)) {
                System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                continue;
            }

            // Get donation amount
            BigDecimal donationAmount;
            while (true) {
                try {
                    donationAmount = new BigDecimal(getInput(keyboard, "Enter the amount donated:"));
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid amount. Please enter a valid decimal number.");
                }
            }

            String donationType = getInput(keyboard, "Enter donation type (credit card or check):");
            String campaignName = getInput(keyboard, "Enter campaign name:");

            // Insert donation based on type
            if (donationType.equalsIgnoreCase("credit card")) {
                String cardNumber = getInput(keyboard, "Enter card number:");
                String cardType = getInput(keyboard, "Enter card type:");
                String expirationDate = getInput(keyboard, "Enter expiration date (yyyy-MM-dd):");
                if (isInvalidDate(expirationDate)) {
                    System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                    continue;
                }

                // Prepare the SQL statement
                String creditCardSql = "INSERT INTO DonationCard (social_sec_number, donation_date, donation_amount, donation_type, donation_campaign, card_type, card_number, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement creditCardStmt = dbConnection.prepareStatement(creditCardSql)) {
                    creditCardStmt.setString(1, donorSSN); // Set the donor's SSN
                    creditCardStmt.setString(2, donationDate); // Set the donation date
                    creditCardStmt.setBigDecimal(3, donationAmount); // Set the donation amount
                    creditCardStmt.setString(4, donationType); // Set the donation type
                    creditCardStmt.setString(5, campaignName); // Set the campaign name
                    creditCardStmt.setString(6, cardType); // Set the card type
                    creditCardStmt.setString(7, cardNumber); // Set the card number
                    creditCardStmt.setString(8, expirationDate); // Set the expiration date
                    creditCardStmt.executeUpdate(); // Execute the SQL statement
                } catch (SQLException e) {
                    System.err.println("An error occurred while inserting credit card donation: " + e.getMessage());
                    throw e;
                }

            } else if (donationType.equalsIgnoreCase("check")) {
                String checkNumber = getInput(keyboard, "Enter check number:");

                // Prepare the SQL statement
                String checkSql = "INSERT INTO DonationCheck (social_sec_number, donation_date, donation_amount, donation_type, donation_campaign, check_number) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement checkStmt = dbConnection.prepareStatement(checkSql)) {
                    checkStmt.setString(1, donorSSN); // Set the donor's SSN
                    checkStmt.setString(2, donationDate); // Set the donation date
                    checkStmt.setBigDecimal(3, donationAmount); // Set the donation amount
                    checkStmt.setString(4, donationType); // Set the donation type
                    checkStmt.setString(5, campaignName); // Set the campaign name
                    checkStmt.setString(6, checkNumber); // Set the check number
                    checkStmt.executeUpdate(); // Execute the SQL statement
                } catch (SQLException e) {
                    System.err.println("An error occurred while inserting check donation: " + e.getMessage());
                    throw e;
                }

            } else {
                System.out.println("Invalid donation type. Please enter either 'credit card' or 'check'.");
            }
        }
    }

    // Query 8: Retrieve the doctor information of a particular client
    private static void getDoctorInfo(Connection dbConnection, Scanner keyboard) throws SQLException {
        String ssn = getInput(keyboard, "Enter the SSN of the client:");

        // Prepare the SQL statement
        String sql = "{call GetDoctorInformation(?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, ssn); // Set the client's SSN
            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    System.out.println("Doctor name: " + rs.getString("doctor_name"));
                    System.out.println("Doctor phone number: " + rs.getString("doctor_phone_number"));
                }
                
                // If no results are found
                if (!hasResults) {
                    System.out.println("No doctor information found for the given client SSN.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving doctor information: " + e.getMessage());
            throw e;
        }
    }

    // Query 9: Retrieve the employee charges during a particular time period
    private static void getTotalExpenses(Connection dbConnection, Scanner keyboard) throws SQLException {
        
        // Get the start date
        String beginningDate = getInput(keyboard, "Enter the start date (yyyy-MM-dd):");
        if (isInvalidDate(beginningDate)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Get the end date
        String endDate = getInput(keyboard, "Enter the end date (yyyy-MM-dd):");
        if (isInvalidDate(endDate)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Prepare the SQL statement
        String sql = "{call GetTotalExpenses(?, ?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, beginningDate); // Set the start date
            stmt.setString(2, endDate);     
            try (ResultSet result = stmt.executeQuery()) {
                boolean hasResults = false;
                while (result.next()) {
                    hasResults = true;
                    System.out.println("SSN: " + result.getString("social_sec_number")); // Get the employee's SSN
                    System.out.println("Total expenses: " + result.getDouble("total_expenses")); // Get the total expenses
                }

                // If no results are found
                if (!hasResults) {
                    System.out.println("No expenses found for the given period.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving expenses: " + e.getMessage());
            throw e;
        }
    }

    // Query 10: Retrieve Volunteers that are members of teams that support a particular client
    private static void getVolunteersForClient(Connection dbConnection, Scanner keyboard) throws SQLException {
        String clientSSN = getInput(keyboard, "Enter the SSN of the client:");

        String sql = "{call GetVolunteersForClient(?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, clientSSN); // Set the client's SSN
            try (ResultSet result = stmt.executeQuery()) {
                boolean hasResults = false;
                System.out.println("Volunteers supporting client with SSN " + clientSSN + ":");

                // Print the volunteers
                while (result.next()) {
                    hasResults = true;
                    String volunteerName = result.getString("person_name");
                    System.out.println("- " + volunteerName);
                }

                // If no results are found
                if (!hasResults) {
                    System.out.println("No volunteers found for this client.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving volunteer information: " + e.getMessage());
            throw e;
        }
    }

    // Query 11: Retrieve teams founded after a particular date
    private static void getTeamsCreatedAfterDate(Connection dbConnection, Scanner keyboard) throws SQLException {
        String dateFormed = getInput(keyboard, "Enter the date to retrieve teams founded after (yyyy-MM-dd):");
        if (isInvalidDate(dateFormed)) {
            System.err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        // Prepare the SQL statement
        String sql = "{call getTeamsAfterDate(?)}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.setString(1, dateFormed);  // Set the date formed
            try (ResultSet result = stmt.executeQuery()) {
                boolean hasResults = false;
                while (result.next()) {
                    hasResults = true;
                    System.out.println("Team name: " + result.getString("team_name"));
                }

                // If no results are found
                if (!hasResults) {
                    System.out.println("No teams found founded after the given date.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving team information: " + e.getMessage());
            throw e;
        }
    }

    // Query 12: Retrieve information of all people in the database
    private static void getAllPersonsInfo(Connection dbConnection) throws SQLException {
        String sql = "{call GetAllPersonsInformation()}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            try (ResultSet result = stmt.executeQuery()) {
                boolean hasResults = false;
                while (result.next()) {
                    hasResults = true;
                    System.out.println("The Person Name: " + result.getString("person_name")); // Get the person's name
                    System.out.println("SSN: " + result.getString("social_sec_number")); // Get the person's SSN
                    System.out.println("Mailing Address: " + result.getString("mailing_address")); // Get the person's mailing address
                    System.out.println("Email Address: " + result.getString("email_address")); // Get the person's email address
                    System.out.println("Phone Number: " + result.getString("phone_number")); // Get the person's phone number
                    System.out.println("Emergency Contact Name: " + result.getString("ec_name")); // Get the emergency contact's name
                    System.out.println("Emergency Contact Phone: " + result.getString("ec_phone_number")); // Get the emergency contact's phone number
                    System.out.println("Emergency Contact Relationship: " + result.getString("ec_relationship")); // Get the emergency contact's relationship
                    System.out.println("---------------------------------------");
                }

                // If no results are found
                if (!hasResults) {
                    System.out.println("No person information found in the database.");
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving person information: " + e.getMessage());
            throw e;
        }
    }

    // Query 13: Retrieve name and amount donated by donors that are also employees
    private static void getDonorsThatAreEmployeesFromDB(Connection dbConnection) throws SQLException {
        String sql = "{call GetEmployeeDonors()}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            try (ResultSet result = stmt.executeQuery()) {
                boolean hasResults = false;
                while (result.next()) { // Iterate through the results
                    hasResults = true;
                    System.out.println("Donor Name: " + result.getString("person_name")); // Get the donor's name
                    System.out.println("Is Anonymous: " + result.getString("is_anonymous")); // Get the anonymous status
                    System.out.println("Total Donations: " + result.getDouble("total_donations")); // Get the total donations
                    System.out.println("---------------------------------------");
                }

                // If no results are found
                if (!hasResults) {
                    System.out.println("No donors found who are also employees."); 
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while retrieving donor information: " + e.getMessage());
            throw e;
        }
    }

    // Query 14: Increase salary of employees with more than one team
    private static void increaseSalaryForEmployeesProcedureInDB(Connection dbConnection) throws SQLException {
        String sql = "{call IncreaseSalary()}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.execute(); // Execute the SQL statement
            System.out.println("Salaries increased for eligible employees."); // Print success message
        } catch (SQLException e) {
            System.err.println("An error occurred while increasing salaries: " + e.getMessage());
            throw e;
        }
    }

    // Query 15: Delete Clients who don't have health insurance and priority for transportation is less than 5
    private static void deleteClientsInDB(Connection dbConnection) throws SQLException {
        String sql = "{call DeleteClients()}";
        try (CallableStatement stmt = dbConnection.prepareCall(sql)) {
            stmt.execute(); // Execute the SQL statement
            System.out.println("Deleted clients that are uninsured and have a low transportation priority.");
        } catch (SQLException e) {
            System.err.println("Error executing delete procedure: " + e.getMessage());
            throw e;
        }
    }

    // Query 16: Import a new team from data file
    public static void importTeam(Connection dbConnection, Scanner keyboard) throws IOException {
        String filePath = getFilePathFromUser(keyboard);

        // Read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean hasErrors = false; 
            while ((line = reader.readLine()) != null) {
                if (!processTeamLine(line, dbConnection)) {
                    hasErrors = true;
                }
            }

            // Print success message
            if (!hasErrors) {
                System.out.println("Team import successfully completed from file: " + filePath);
            } else {
                System.out.println("Team import completed with some errors. Please check the logs.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Helper method to get file path from user
    private static String getFilePathFromUser(Scanner keyboard) throws IOException {
        String fileName = getInput(keyboard, "Enter the input file name (e.g., team117.txt):");
        String baseFilePath = new java.io.File(".").getCanonicalPath(); // Get the base file path
        return baseFilePath + java.io.File.separator + fileName;
    }

    // Process each line to parse team data and insert it into the database
    private static boolean processTeamLine(String line, Connection dbConnection) {
        String[] teamData = line.split(","); // Split the line by comma

        // Validate the team data format
        if (!isValidTeamData(teamData)) {
            System.out.println("Incorrect format. Expected: Team Name, Team Type, Date of Creation (yyyy-MM-dd)");
            return false;
        }

        try {
            insertTeamIntoDatabase(dbConnection, teamData[0].trim(), teamData[1].trim(), teamData[2].trim());
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting team '" + teamData[0].trim() + "': " + e.getMessage());
            return false;
        }
    }

    // Validate the format of team data
    private static boolean isValidTeamData(String[] teamData) {
        if (teamData.length != 3) {
            return false;
        }
        return !isInvalidDate(teamData[2].trim());
    }

    // Insert team data into the database using the stored procedure
    private static void insertTeamIntoDatabase(Connection dbConnection, String teamName, String teamType, String dateString) throws SQLException {
        String query = "{CALL InsertTeam(?, ?, ?)}";

        try (CallableStatement callableStatement = dbConnection.prepareCall(query)) {
            callableStatement.setString(1, teamName); // Set the team name
            callableStatement.setString(2, teamType); // Set the team type
            callableStatement.setString(3, dateString); // Set the date of creation
            callableStatement.executeUpdate();
            System.out.println("Inserted team: " + teamName + " into the database.");
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting team data: " + e.getMessage());
            throw e;
        }
    }

    // Query 17: Export the names and mailing address of people on mailing list to a user specified file
    private static void exportMailingList(Connection dbConnection, Scanner keyboard) throws IOException {
        String fileName = getInput(keyboard, "Enter the output file name (e.g., output.txt):");
        String basePath = new java.io.File(".").getCanonicalPath(); // Get the base file path
        String filePath = basePath + java.io.File.separator + fileName;

        String query = "{CALL exportMailingList()}";
        try (CallableStatement callableStatement = dbConnection.prepareCall(query);
             ResultSet resultSet = callableStatement.executeQuery()) {

            try (FileWriter writer = new FileWriter(filePath)) {
                boolean hasResults = false;

                // Write the results to the file
                while (resultSet.next()) {
                    hasResults = true;
                    String name = resultSet.getString("person_name");
                    String address = resultSet.getString("mailing_address");
                    writer.write("Name: " + name + ", Address: " + address + "\n");
                }

                // If no results are found
                if (!hasResults) {
                    writer.write("No data found for people on the mailing list.\n");
                }
                System.out.println("Data successfully exported to " + filePath);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error executing stored procedure: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Load database properties from application.properties file
        Properties properties = new Properties();
        try (InputStream input = Hicks_Cole_IP_Task5b.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("An error occurred while loading properties: " + ex.getMessage());
            return;
        }

        System.out.println("Welcome to the Patient Assistance Network!");

        // Establish a database connection
        try (Connection dbConnection = DriverManager.getConnection(properties.getProperty("url"), properties);
             Scanner keyboard = new Scanner(System.in)) {

            int querySelection = 0; // Variable to store user query selection

            // Main menu loop
            while (querySelection != 18) {
                System.out.println("""
                        Please choose an option:\s
                        1: Insert a team into the database.\s
                        2: Insert a client into the database.\s
                        3: Insert a volunteer into the database.\s
                        4: Insert the number of hours a volunteer worked this month.\s
                        5: Insert an employee into the database.\s
                        6: Insert an expense charged by an employee into the database.\s
                        7: Insert a donor into the database.\s
                        8: Get a doctor's name & phone number of a particular client.\s
                        9: Get each employee's expenses during a particular time period.\s
                        10: Get the volunteers that are members of teams that support particular client.\s
                        11: Get the names of all the teams that are founded after particular date.\s
                        12: Get the information of all people in database.\s
                        13: Get the name and amount donated by donors that are also employees.\s
                        14: Increase the salary of employees in which more than one team reports to.\s
                        15: Delete clients who don't have health insurance & also have a low transportation priority (less than 5).\s
                        16: Import a new team from data file.\s
                        17: Export the names and mailing address of people on mailing list to a user specified file.\s
                        18: Quit\s""");
                try {
                    querySelection = Integer.parseInt(keyboard.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Please select a valid option (1-18).");
                    continue;
                }

                try {
                    switch (querySelection) {
                        case 1 -> insertTeam(dbConnection, keyboard); // Call the insertTeam method
                        case 2 -> insertClientIntoDB(dbConnection, keyboard); // Call the insertClientIntoDB method
                        case 3 -> insertVolunteerIntoDB(dbConnection, keyboard); // Call the insertVolunteerIntoDB method
                        case 4 -> insertVolunteerHours(dbConnection, keyboard); // Call the insertVolunteerHours method
                        case 5 -> insertEmployee(dbConnection, keyboard); // Call the insertEmployee method
                        case 6 -> insertExpense(dbConnection, keyboard); // Call the insertExpense method
                        case 7 -> insertDonor(dbConnection, keyboard); // Call the insertDonor method
                        case 8 -> getDoctorInfo(dbConnection, keyboard); // Call the getDoctorInfo method
                        case 9 -> getTotalExpenses(dbConnection, keyboard); // Call the getTotalExpenses method
                        case 10 -> getVolunteersForClient(dbConnection, keyboard); // Call the getVolunteersForClient method
                        case 11 -> getTeamsCreatedAfterDate(dbConnection, keyboard); // Call the getTeamsCreatedAfterDate method
                        case 12 -> getAllPersonsInfo(dbConnection); // Call the getAllPersonsInfo method
                        case 13 -> getDonorsThatAreEmployeesFromDB(dbConnection); // Call the getDonorsThatAreEmployeesFromDB method
                        case 14 -> increaseSalaryForEmployeesProcedureInDB(dbConnection); // Call the increaseSalaryForEmployeesProcedureInDB method
                        case 15 -> deleteClientsInDB(dbConnection); // Call the deleteClientsInDB method
                        case 16 -> importTeam(dbConnection, keyboard); // Call the importTeam method
                        case 17 -> exportMailingList(dbConnection, keyboard); // Call the exportMailingList method
                        case 18 -> System.out.println("Terminating program."); // Exit the program
                        default -> System.out.println("Invalid option. Please select a valid option (1-18)."); // Invalid option
                    }
                } catch (SQLException e) {
                    System.err.println("An SQL error occurred: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("An IO error occurred: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
