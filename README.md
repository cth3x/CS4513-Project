# Patient Assistant Network (PAN) Database System

## Features

- Team Management: Create and track support teams
- Client Management: Register clients and associate them with care teams
- Volunteer Management: Track volunteer participation and team assignments
- Employee Management: Monitor employee activities and team supervision
- Donor Management: Record donations and manage donor information
- Emergency Contact System: Maintain emergency contact information for all personnel
- Report Generation: Generate various reports on organizational activities
- File Import/Export: Support for data import and export operations

## Technical Stack

- Java
- JDBC
- Azure Data Studio

## Key Functionalities

### Data Entry Operations

- Create new teams
- Register new clients, volunteers, employees, and donors
- Record volunteer work hours
- Track employee expenses
- Process donations

### Data Retrieval Operations

- Retrieve client medical contact information
- Generate expense reports
- List team volunteers
- Access team formation history
- View comprehensive personnel information
- Generate donor reports

### Data Maintenance Operations

- Update employee salaries
- Manage client records
- Import team data from files
- Export mailing list information

## Database Structure

- Implements a relational database model
- Features multiple interconnected tables for different entities
- Includes indexing for optimal performance
- Maintains referential integrity through foreign key constraints

## Code Organization

- `Hicks_Cole_IP_Task4.sql`: Database schema creation scripts
- `Hicks_Cole_IP_Task5a.sql`: SQL query implementations
- `Hicks_Cole_IP_Task5b.java`: Main Java application

## Installation and Setup

1. Set up Azure SQL Database instance
2. Execute the schema creation scripts
3. Configure the application.properties file with your database credentials
4. Compile and run the Java application

## Usage

The system presents a menu-driven interface with the following options:

```
1: Insert a team
2: Insert a client
3: Insert a volunteer
4: Insert volunteer hours
5: Insert an employee
6: Insert employee expense
7: Insert a donor
8: Get client's doctor information
9: Get employee expenses
10: Get team volunteers
11: Get teams by formation date
12: Get all personnel information
13: Get employee-donors
14: Increase employee salaries
15: Delete inactive clients
16: Import teams from file
17: Export mailing list
18: Quit
```

## Error Handling

- Comprehensive input validation
- Database constraint enforcement
- Detailed error messaging
- Transaction management

## Author

Cole Hicks

## License

This project is part of academic coursework and is not licensed for redistribution.

## Acknowledgments

- Dr. Le Gruenwald
- The University of Oklahoma
