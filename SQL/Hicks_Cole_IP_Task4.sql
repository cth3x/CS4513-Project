-- Cole Hicks
-- CS 4513

DROP TABLE IF EXISTS EmergencyContactDonor;
DROP TABLE IF EXISTS EmergencyContactVolunteer;
DROP TABLE IF EXISTS EmergencyContactEmployee;
DROP TABLE IF EXISTS EmergencyContactClient;
DROP TABLE IF EXISTS Has;
DROP TABLE IF EXISTS Gives;
DROP TABLE IF EXISTS Charged;
DROP TABLE IF EXISTS Leads;
DROP TABLE IF EXISTS ReportsTo;
DROP TABLE IF EXISTS CaresFor;
DROP TABLE IF EXISTS ServesOn;
DROP TABLE IF EXISTS DonationCard;
DROP TABLE IF EXISTS DonationCheck;
DROP TABLE IF EXISTS Donor;
DROP TABLE IF EXISTS EmployeeExpenses;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Team;
DROP TABLE IF EXISTS InsurancePolicy;
DROP TABLE IF EXISTS Volunteer;
DROP TABLE IF EXISTS Needs;
DROP TABLE IF EXISTS Client;


-- Table Creation For Team
CREATE TABLE Team (
    team_name VARCHAR(256) PRIMARY KEY,
    team_type VARCHAR(256),
    date_of_creation DATE
);

-- Table Creation For Client
CREATE TABLE Client (
    social_sec_number INT PRIMARY KEY,
    person_name VARCHAR(256),
    person_gender VARCHAR(256),
    person_profession VARCHAR(256),
    mailing_address VARCHAR(256),
    email_address VARCHAR(256),
    phone_number VARCHAR(256),
    is_on_mailing_list BIT,
    doctor_name VARCHAR(256),
    doctor_phone_number VARCHAR(256),
    date_of_assignment DATE,

);

-- Table Creation For Volunteer
CREATE TABLE Volunteer (
    social_sec_number INT PRIMARY KEY,
    person_name VARCHAR(256),
    person_gender VARCHAR(256),
    person_profession VARCHAR(256),
    mailing_address VARCHAR(256),
    email_address VARCHAR(256),
    phone_number VARCHAR(256),
    is_on_mailing_list BIT,
    date_joined DATE,
    date_of_training DATE,
    location_of_training VARCHAR(256)
);

-- Table Creation For Employee
CREATE TABLE Employee (
    social_sec_number INT PRIMARY KEY,
    person_name VARCHAR(256),
    person_gender VARCHAR(256),
    person_profession VARCHAR(256),
    mailing_address VARCHAR(256),
    email_address VARCHAR(256),
    phone_number VARCHAR(256),
    is_on_mailing_list BIT,
    employee_salary DECIMAL(10, 2),
    is_employee_married VARCHAR(256),
    date_of_hire DATE
);

-- Table Creation For Donor
CREATE TABLE Donor (
    social_sec_number INT PRIMARY KEY,
    person_name VARCHAR(256),
    person_gender VARCHAR(256),
    person_profession VARCHAR(256),
    mailing_address VARCHAR(256),
    email_address VARCHAR(256),
    phone_number VARCHAR(256),
    is_on_mailing_list BIT,
    is_donor_anonymous BIT
);

-- Emergency Contact Table For Client
CREATE TABLE EmergencyContactClient (
    ssn_client INT,
    ec_name VARCHAR(256),
    ec_phone_number VARCHAR(256),
    ec_relationship VARCHAR(256),
    PRIMARY KEY (ssn_client, ec_name, ec_phone_number),
    FOREIGN KEY (ssn_client) REFERENCES Client(social_sec_number) ON DELETE CASCADE
);

-- Emergency Contact Table For Volunteer
CREATE TABLE EmergencyContactVolunteer (
    ssn_volunteer INT,
    ec_name VARCHAR(256),
    ec_phone_number VARCHAR(256),
    ec_relationship VARCHAR(256),
    PRIMARY KEY (ssn_volunteer, ec_name, ec_phone_number),
    FOREIGN KEY (ssn_volunteer) REFERENCES Volunteer(social_sec_number)
);

-- Emergency Contact Table For Employee
CREATE TABLE EmergencyContactEmployee (
    ssn_employee INT,
    ec_name VARCHAR(256),
    ec_phone_number VARCHAR(256),
    ec_relationship VARCHAR(256),
    PRIMARY KEY (ssn_employee, ec_name, ec_phone_number),
    FOREIGN KEY (ssn_employee) REFERENCES Employee(social_sec_number)
);

-- Emergency Contact Table For Donor
CREATE TABLE EmergencyContactDonor (
    ssn_donor INT,
    ec_name VARCHAR(256),
    ec_phone_number VARCHAR(256),
    ec_relationship VARCHAR(256),
    PRIMARY KEY (ssn_donor, ec_name, ec_phone_number),
    FOREIGN KEY (ssn_donor) REFERENCES Donor(social_sec_number)
);

-- Table Creation For InsurancePolicy
CREATE TABLE InsurancePolicy (
    insurance_policy_id VARCHAR(256) PRIMARY KEY,
    provider_name VARCHAR(256),
    provider_address VARCHAR(256),
    type_of_insurance VARCHAR(256),
);

-- Table Creation For EmployeeExpenses
CREATE TABLE EmployeeExpenses (
    social_sec_number INT,
    expense_date DATE,
    expense_description VARCHAR(256),
    expense_amount DECIMAL(10, 2),
    PRIMARY KEY (social_sec_number, expense_date, expense_description),
    FOREIGN KEY (social_sec_number) REFERENCES Employee(social_sec_number)
);

-- Table Creation For DonationCheck
CREATE TABLE DonationCheck (
    social_sec_number INT,
    donation_date DATE,
    donation_amount DECIMAL(10, 2),
    donation_type VARCHAR(256),
    donation_campaign VARCHAR(256),
    check_number VARCHAR(256),
    PRIMARY KEY (social_sec_number, donation_date, donation_amount, donation_type),
    FOREIGN KEY (social_sec_number) REFERENCES Donor(social_sec_number)
);

-- Table Creation For DonationCard
CREATE TABLE DonationCard (
    social_sec_number INT,
    donation_date DATE,
    donation_amount DECIMAL(10, 2),
    donation_type VARCHAR(256),
    donation_campaign VARCHAR(256),
    card_type VARCHAR(256),
    card_number VARCHAR(256),
    expiration_date VARCHAR(256),
    PRIMARY KEY (social_sec_number, donation_date, donation_amount, donation_type),
    FOREIGN KEY (social_sec_number) REFERENCES Donor(social_sec_number)
);

-- Table Creation For ServesOn
CREATE TABLE ServesOn (
    social_sec_number INT,
    team_name VARCHAR(256),
    month_served VARCHAR(256),
    hours_served INT,
    is_active BIT,
    PRIMARY KEY (social_sec_number, team_name, month_served),
    FOREIGN KEY (social_sec_number) REFERENCES Volunteer(social_sec_number),
    FOREIGN KEY (team_name) REFERENCES Team(team_name)
);

-- Table Creation For CareFor
CREATE TABLE CaresFor (
    social_sec_number INT,
    team_name VARCHAR(256),
    is_client_active BIT,
    PRIMARY KEY (social_sec_number, team_name),
    FOREIGN KEY (social_sec_number) REFERENCES Client(social_sec_number) ON DELETE CASCADE,
    FOREIGN KEY (team_name) REFERENCES Team(team_name)
);

-- Table Creation For ReportsTo
CREATE TABLE ReportsTo (
    social_sec_number INT,
    team_name VARCHAR(256),
    date_of_report DATE,
    report_description VARCHAR(512),
    PRIMARY KEY (social_sec_number, team_name),
    FOREIGN KEY (social_sec_number) REFERENCES Employee(social_sec_number),
    FOREIGN KEY (team_name) REFERENCES Team(team_name)
);

-- Table Creation For Team Leader
CREATE TABLE Leads (
    social_sec_number INT,
    team_name VARCHAR(256),
    is_leader BIT,
    PRIMARY KEY (social_sec_number, team_name),
    FOREIGN KEY (social_sec_number) REFERENCES Volunteer(social_sec_number),
    FOREIGN KEY (team_name) REFERENCES Team(team_name)
);

-- Table Creation For Has
CREATE TABLE Has (
    social_sec_number INT,
    insurance_policy_id VARCHAR(256),
    PRIMARY KEY (social_sec_number, insurance_policy_id),
    FOREIGN KEY (social_sec_number) REFERENCES Client(social_sec_number),
    FOREIGN KEY (insurance_policy_id) REFERENCES InsurancePolicy(insurance_policy_id)
);

-- Table Creation For Needs
CREATE TABLE Needs (
    social_sec_number INT,
    need_type VARCHAR(256),
    priority INT,
    PRIMARY KEY (social_sec_number, need_type),
    FOREIGN KEY (social_sec_number) REFERENCES Client(social_sec_number) ON DELETE CASCADE,
);


-- Indexes Based on Task 3

-- Index for Client
DROP INDEX IF EXISTS Client.idx_social_sec_number;
CREATE INDEX idx_client_social_sec_number ON Client(social_sec_number);

-- Index for Volunteer
DROP INDEX IF EXISTS Volunteer.idx_social_sec_number;
CREATE INDEX idx_volunteer_social_sec_number ON Volunteer(social_sec_number);

-- Index for Employee
DROP INDEX IF EXISTS Employee.idx_social_sec_number;
CREATE INDEX idx_employee_social_sec_number ON Employee(social_sec_number);

-- Index for Donor
DROP INDEX IF EXISTS Donor.idx_social_sec_number;
CREATE INDEX idx_donor_social_sec_number ON Donor(social_sec_number);

-- Index for EmergencyContactClient
DROP INDEX IF EXISTS EmergencyContactClient.idx_ssn_client;
CREATE INDEX idx_client_emergency_contact_ssn ON EmergencyContactClient(ssn_client);

-- Index for EmergencyContactVolunteer
DROP INDEX IF EXISTS EmergencyContactVolunteer.idx_ssn_volunteer;
CREATE INDEX idx_volunteer_emergency_contact_ssn ON EmergencyContactVolunteer(ssn_volunteer);

-- Index for EmergencyContactEmployee
DROP INDEX IF EXISTS EmergencyContactEmployee.idx_ssn_employee;
CREATE INDEX idx_employee_emergency_contact_ssn ON EmergencyContactEmployee(ssn_employee);

-- Index for EmergencyContactDonor
DROP INDEX IF EXISTS EmergencyContactDonor.idx_ssn_donor;
CREATE INDEX idx_donor_emergency_contact_ssn ON EmergencyContactDonor(ssn_donor);

-- Index for Team
DROP INDEX IF EXISTS Team.idx_team_name;
CREATE INDEX idx_team_name ON Team(team_name);

-- Index for EmployeeExpenses
DROP INDEX IF EXISTS EmployeeExpenses.idx_expense_amount;
CREATE INDEX idx_employee_expense_amount ON EmployeeExpenses(expense_amount);

--DonationChecks index
DROP INDEX IF EXISTS DonationCheck.idx_donation_amount;
CREATE INDEX idx_donation_check_amount ON DonationCheck(donation_amount);

--CreditCards index 
DROP INDEX IF EXISTS DonationCard.idx_donation_amount;
CREATE INDEX idx_donation_card_amount ON DonationCard(donation_amount);
