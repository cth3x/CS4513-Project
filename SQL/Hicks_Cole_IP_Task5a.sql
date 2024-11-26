-- Cole Hicks
-- CS 4513

-- Query 1: Insert a new team into the database (1/month)
DROP PROCEDURE IF EXISTS InsertTeam;
GO

CREATE PROCEDURE InsertTeam
(
    @team_name VARCHAR(256), -- 1
    @team_type VARCHAR(256), -- 2
    @date_of_creation DATE -- 3
)
AS
BEGIN
    INSERT INTO Team (team_name, team_type, date_of_creation) VALUES (@team_name, @team_type, @date_of_creation);
END;
GO

-- Query 2: Insert a new client into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS InsertClient;
GO

CREATE PROCEDURE InsertClient
(
    -- Client Attributes
    @social_sec_number INT, -- 1
    @person_name VARCHAR(256), -- 2
    @person_gender VARCHAR(256), -- 3
    @person_profession VARCHAR(256), -- 4
    @mailing_address VARCHAR(256), -- 5
    @email_address VARCHAR(256), -- 6
    @phone_number VARCHAR(256), -- 7
    @is_on_mailing_list BIT, -- 8
    @doctor_name VARCHAR(256), -- 9
    @doctor_phone_number VARCHAR(256), -- 10
    @date_of_assignment DATE, -- 11

    -- Needs Attributes
    @need_type VARCHAR(256), -- 12
    @priority INT, -- 13

    -- Insurance Policy Attributes
    @insurance_policy_id VARCHAR(256), -- 14
    @provider_name VARCHAR(256), -- 15
    @provider_address VARCHAR(256), -- 16
    @type_of_insurance VARCHAR(256) -- 17
)
AS
BEGIN

    -- Insert into Clients table
    INSERT INTO Client (social_sec_number, person_name, person_gender, person_profession, mailing_address, email_address, phone_number, is_on_mailing_list, doctor_name, doctor_phone_number, date_of_assignment)
    VALUES (@social_sec_number, @person_name, @person_gender, @person_profession, @mailing_address, @email_address, @phone_number, @is_on_mailing_list, @doctor_name, @doctor_phone_number, @date_of_assignment);

    -- Insert into Needs table
    INSERT INTO Needs (social_sec_number, need_type, priority)
    VALUES (@social_sec_number, @need_type, @priority);

    -- Insert into InsurancePolicies table
    INSERT INTO InsurancePolicy (insurance_policy_id, provider_name, provider_address, type_of_insurance)
    VALUES (@insurance_policy_id, @provider_name, @provider_address, @type_of_insurance);

END;
GO

-- Query 3: Insert a new volunteer into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS InsertVolunteer;
GO

CREATE PROCEDURE InsertVolunteer
(
    -- Volunteer Attributes
    @social_sec_number INT, -- 1
    @person_name VARCHAR(256), -- 2
    @person_gender VARCHAR(256), -- 3
    @person_profession VARCHAR(256), -- 4
    @mailing_address VARCHAR(256), -- 5
    @email_address VARCHAR(256), -- 6
    @phone_number VARCHAR(256), -- 7
    @is_on_mailing_list BIT, -- 8
    @date_joined DATE, -- 9
    @date_of_training DATE, -- 10
    @location_of_training VARCHAR(256) -- 11

)
AS
BEGIN

    -- Insert into Volunteers table
    INSERT INTO Volunteer (social_sec_number, person_name, person_gender, person_profession, mailing_address, email_address, phone_number, is_on_mailing_list, date_joined, date_of_training, location_of_training)
    VALUES (@social_sec_number, @person_name, @person_gender, @person_profession, @mailing_address, @email_address, @phone_number, @is_on_mailing_list, @date_joined, @date_of_training, @location_of_training);

END;
GO

-- Query 4: Insert number of hours a volunteer worked this month for a particular team
DROP PROCEDURE IF EXISTS InsertVolunteerHours;
GO

CREATE PROCEDURE InsertVolunteerHours
(
    @social_sec_number INT, -- 1
    @team_name VARCHAR(256), -- 2
    @month_served VARCHAR(256), -- 3
    @hours_served INT, -- 4
    @is_active BIT -- 5
)
AS
BEGIN

    -- Update ServeOn table for hours from a volunteer if month is already present
    IF EXISTS (SELECT 1 FROM ServesOn WHERE social_sec_number = @social_sec_number AND team_name = @team_name AND month_served = @month_served)
    BEGIN
        UPDATE ServesOn -- using "update" to update the hours_served
        SET hours_served = @hours_served -- setting the hours_served to the new value
        WHERE social_sec_number = @social_sec_number AND team_name = @team_name AND month_served = @month_served; -- filtering by social_sec_number, team_name, and month_served
    END
    ELSE
    BEGIN
        -- Insert into ServesOn table
        INSERT INTO ServesOn (social_sec_number, team_name, month_served, hours_served, is_active)
        VALUES (@social_sec_number, @team_name, @month_served, @hours_served, @is_active);
    END;
END;
GO

-- Query 5: Insert a new employee into the database and associate with one or more teams
DROP PROCEDURE IF EXISTS InsertEmployee;
GO

CREATE PROCEDURE InsertEmployee
(

    -- Employee Attributes
    @social_sec_number INT, -- 1
    @person_name VARCHAR(256), -- 2
    @person_gender VARCHAR(256), -- 3
    @person_profession VARCHAR(256), -- 4
    @mailing_address VARCHAR(256), -- 5
    @email_address VARCHAR(256), -- 6
    @phone_number VARCHAR(256), -- 7
    @is_on_mailing_list BIT, -- 8
    @employee_salary DECIMAL(10,2), -- 9
    @is_employee_married VARCHAR(256), -- 10
    @date_of_hire DATE -- 11
)
AS
BEGIN

    -- Insert into Employees table
    INSERT INTO Employee (social_sec_number, person_name, person_gender, person_profession, mailing_address, email_address, phone_number, is_on_mailing_list, employee_salary, is_employee_married, date_of_hire)
    VALUES (@social_sec_number, @person_name, @person_gender, @person_profession, @mailing_address, @email_address, @phone_number, @is_on_mailing_list, @employee_salary, @is_employee_married, @date_of_hire);

END;
GO


-- Query 6: Insert an expense charged by an employee
DROP PROCEDURE IF EXISTS InsertExpenses;
GO

CREATE PROCEDURE InsertExpenses
(
    @social_sec_number INT, -- 1
    @expense_date DATE, -- 2
    @expense_description VARCHAR(256), -- 3
    @expense_amount DECIMAL(10, 2) -- 4
)
AS
BEGIN
    -- Insert into EmployeeExpenses table
    INSERT INTO EmployeeExpenses (social_sec_number, expense_date, expense_description, expense_amount)
    VALUES (@social_sec_number, @expense_date, @expense_description, @expense_amount);
END;
GO

-- Query 7: Insert a new donor and associate with multiple donations
DROP PROCEDURE IF EXISTS InsertDonor;
GO

CREATE PROCEDURE InsertDonor
(

    @social_sec_number INT, -- 1
    @person_name VARCHAR(256), -- 2
    @person_gender VARCHAR(256), -- 3   
    @person_profession VARCHAR(256), -- 4
    @mailing_address VARCHAR(256), -- 5
    @email_address VARCHAR(256), -- 6
    @phone_number VARCHAR(256), -- 7
    @is_on_mailing_list BIT, -- 8
    @is_donor_anonymous BIT -- 9
)
AS
BEGIN
    -- Insert into Donors table
    INSERT INTO Donor (social_sec_number, person_name, person_gender, person_profession, mailing_address, email_address, phone_number, is_on_mailing_list, is_donor_anonymous)
    VALUES (@social_sec_number, @person_name, @person_gender, @person_profession, @mailing_address, @email_address, @phone_number, @is_on_mailing_list, @is_donor_anonymous);
    
END;
GO


-- Query 8: Retrieve the name and phone number of the doctor of a particular client (1/week)
DROP PROCEDURE IF EXISTS GetDoctorInformation;
GO

CREATE PROCEDURE GetDoctorInformation
(
    @social_sec_number INT -- 1
)
AS
BEGIN
    -- Retrieve the doctor’s name and phone number for a specific client
    SELECT doctor_name, doctor_phone_number 
    FROM Client 
    WHERE social_sec_number = @social_sec_number;
END;
GO

-- Query 9: Retrieve total expenses charged by each employee for a particular period, sorted by amount (1/month)
DROP PROCEDURE IF EXISTS GetTotalExpenses;
GO

CREATE PROCEDURE GetTotalExpenses
(
    @begin_date DATE, -- 1
    @end_date DATE -- 2
)
AS
BEGIN
    -- Calculate total expenses for each employee within a date range
    SELECT social_sec_number, SUM(expense_amount) AS total_expenses
    FROM EmployeeExpenses -- Using EmployeeExpenses table
    WHERE expense_date BETWEEN @begin_date AND @end_date
    GROUP BY social_sec_number
    ORDER BY total_expenses DESC;
END;
GO

-- Query 10: Retrieve the list of volunteers that are members of teams that support a particular client (4/year).
DROP PROCEDURE IF EXISTS GetVolunteersForClient;
GO

CREATE PROCEDURE GetVolunteersForClient
(
    @social_sec_number INT -- 1
)
AS
BEGIN
    SELECT vols.person_name -- Getting the names of volunteers
    FROM Volunteer vols -- Using Volunteer table
    JOIN ServesOn serve ON vols.social_sec_number = serve.social_sec_number -- Joining ServesOn table with Volunteer table (ssn)
    JOIN CaresFor care ON serve.team_name = care.team_name -- Joining CaresFor table with ServesOn table (team_name)
    WHERE care.social_sec_number = @social_sec_number; -- Filtering by client's social security number
END
GO

-- Query 11: Retrieve the names of all teams founded after a specified date (1/month)
DROP PROCEDURE IF EXISTS GetTeamsAfterDate;
GO

CREATE PROCEDURE GetTeamsAfterDate
(
    @date_of_creation DATE -- 1
)
AS
BEGIN
    -- Retrieve team names founded after a given date
    SELECT team_name  -- Selecting team names
    FROM Team -- Using Team table
    WHERE date_of_creation > @date_of_creation; -- Filtering by date of creation
END;
GO

-- Query 12: Retrieve names, SSNs, contact information, and emergency contacts for all people (1/week)
DROP PROCEDURE IF EXISTS GetAllPersonsInformation;
GO

CREATE PROCEDURE GetAllPersonsInformation
AS
BEGIN
    -- Get all people's information and emergency contacts
    SELECT e.social_sec_number, e.person_name, e.mailing_address, e.email_address, e.phone_number, ec.ec_name AS ec_name, ec.ec_phone_number AS ec_phone_number, ec.ec_relationship AS ec_relationship
    FROM Employee e
    
    -- Left join to include employees without emergency contacts
    LEFT JOIN EmergencyContactEmployee ec ON e.social_sec_number = ec.ssn_employee
    UNION ALL
    
    -- Get all volunteers' information and emergency contacts
    SELECT v.social_sec_number, v.person_name, v.mailing_address, v.email_address, v.phone_number, vc.ec_name AS ec_name, vc.ec_phone_number AS ec_phone_number, vc.ec_relationship AS ec_relationship
    FROM Volunteer v
   
--    Left join to include volunteers without emergency contacts
    LEFT JOIN EmergencyContactVolunteer vc ON v.social_sec_number = vc.ssn_volunteer
    UNION ALL
   
--    Get all clients' information and emergency contacts
    SELECT c.social_sec_number, c.person_name, c.mailing_address, c.email_address, c.phone_number, cc.ec_name AS ec_name, cc.ec_phone_number AS ec_phone_number, cc.ec_relationship AS ec_relationship
    FROM Client c
    
    -- Left join to include clients without emergency contacts
    LEFT JOIN EmergencyContactClient cc ON c.social_sec_number = cc.ssn_client
    UNION ALL
   
--    Get all donors' information and emergency contacts
    SELECT d.social_sec_number, d.person_name, d.mailing_address, d.email_address, d.phone_number, dc.ec_name AS ec_name, dc.ec_phone_number AS ec_phone_number, dc.ec_relationship AS ec_relationship
    FROM Donor d
    LEFT JOIN EmergencyContactDonor dc ON d.social_sec_number = dc.ssn_donor;
END;
GO


-- Query 13: Retrieve the name and total donations by donors who are also employees, sorted by amount (1/week)
DROP PROCEDURE IF EXISTS GetEmployeeDonors;
GO

CREATE PROCEDURE GetEmployeeDonors
AS
BEGIN
    -- Combine donations from both DonationCard and DonationCheck for donors who are also employees
    SELECT 
        d.person_name, -- Get the name of the donor
        CASE 
            WHEN d.is_donor_anonymous = 1 THEN 'Yes' -- Check if donor is anonymous
            ELSE 'No'
        END AS is_anonymous, 
        SUM(all_donations.donation_amount) AS total_donations -- Sum the total donations
    FROM Donor d
    JOIN (
        -- Combine DonationCard and DonationCheck data
        SELECT social_sec_number, donation_amount
        FROM DonationCard
        UNION ALL
        SELECT social_sec_number, donation_amount -- Union all to combine the two tables
        FROM DonationCheck
    ) all_donations ON d.social_sec_number = all_donations.social_sec_number
    JOIN Employee e ON d.social_sec_number = e.social_sec_number
    GROUP BY d.person_name, d.is_donor_anonymous
    ORDER BY total_donations DESC;
END;
GO
    
-- Query 14: Increase salary by 10% for employees with reports from more than one team (1/year)
DROP PROCEDURE IF EXISTS IncreaseSalary
GO

CREATE PROCEDURE IncreaseSalary
AS
BEGIN
    -- Increase salary by 10% for employees reporting on multiple teams
    UPDATE Employee
    SET employee_salary = employee_salary * 1.1 -- Increase salary by 10%
    WHERE social_sec_number IN (
        SELECT social_sec_number 
        FROM ReportsTo
        GROUP BY social_sec_number
        HAVING COUNT(DISTINCT team_name) > 1 -- Check if employee has reports from more than one team
    );
END;
GO

-- Query 15: Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5 (4/year).

DROP PROCEDURE IF EXISTS DeleteClients;
GO

CREATE PROCEDURE DeleteClients
AS
BEGIN
    DECLARE @clientDeletion TABLE (social_sec_number INT);
    
    -- Insert the SSNs of clients who meet the criteria for deletion
    INSERT INTO @clientDeletion (social_sec_number)
    SELECT c.social_sec_number
    FROM Client c 
    JOIN Has h ON c.social_sec_number = h.social_sec_number -- Joining Client and Has tables
    JOIN InsurancePolicy ip ON h.insurance_policy_id = ip.insurance_policy_id -- Joining Has and InsurancePolicy tables
    WHERE ip.type_of_insurance != 'Health'  -- Check if client does not have health insurance
    AND c.social_sec_number IN ( 
        SELECT social_sec_number 
        FROM Needs  
        WHERE need_type = 'Transportation' -- Must have entered transportation as a need (in Java)
        AND priority < 5 -- Check if client's importance for transportation is less than 5
    );

    -- Delete from the 'Needs' table
    DELETE FROM Needs 
    WHERE social_sec_number IN (SELECT social_sec_number FROM @clientDeletion);

    -- Delete from the 'Has' table
    DELETE FROM Has 
    WHERE social_sec_number IN (SELECT social_sec_number FROM @clientDeletion);

    -- Delete from the 'InsurancePolicies' table if not referenced in the 'Has' table
    DELETE FROM InsurancePolicy
    WHERE insurance_policy_id NOT IN (SELECT insurance_policy_id FROM Has);

    -- Delete from 'ClientEmergencyContacts' table
    DELETE FROM EmergencyContactClient
    WHERE ssn_client IN (SELECT social_sec_number FROM @clientDeletion);

    -- Finally, delete from 'Clients' table
    DELETE FROM Client 
    WHERE social_sec_number IN (SELECT social_sec_number FROM @clientDeletion);
END
GO


-- Query 17: Retrieve names and mailing addresses of all people on the mailing list
DROP PROCEDURE IF EXISTS ExportMailingList;
GO

CREATE PROCEDURE ExportMailingList
AS
BEGIN
    -- Retrieve names and mailing addresses of all people on the mailing list
    SELECT person_name, mailing_address
    FROM Employee
    WHERE is_on_mailing_list = 1 -- Check if employee is on the mailing list for Employee table
    UNION ALL
    SELECT person_name, mailing_address 
    FROM Volunteer
    WHERE is_on_mailing_list = 1 -- Check if volunteer is on the mailing list for Volunteer table
    UNION ALL
    SELECT person_name, mailing_address
    FROM Client
    WHERE is_on_mailing_list = 1 -- Check if client is on the mailing list for Client table
    UNION ALL
    SELECT person_name, mailing_address
    FROM Donor
    WHERE is_on_mailing_list = 1; -- Check if donor is on the mailing list for Donor table
END;