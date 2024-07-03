# Country Information Service

## Overview

This service provides functionality to retrieve and store country information, including details about languages spoken in each country. It uses a SOAP client to fetch data from an external service and stores the information in a local database.

## Features

- Retrieve country information by name
- Store country details including ISO code, name, and languages
- Handle errors and exceptions for invalid country names or service failures

## Technologies Used

- Java
- Spring Boot
- JPA/Hibernate
- SOAP Web Service Client
- Logging (SLF4J)

## Setup

1. Clone the repository from `https://github.com/Eve-rine/CountriesIntegration` and navigate to the project directory or download the zip file and extract it
2. Configure your database connection in `application.properties`
3. Build the project using Maven: `mvn clean install`
4. Run the application using the maven plugin `mvn spring-boot:run`
5. Access the application at `http://localhost:8080`

## Usage

## API Endpoints

### Countries

- `POST /api/countries`: Create a new country entry
    - Request body: `{ "name": "Country Name" }`
    - Response: Country information including languages
    - Status codes:
        - 201: Created successfully
        - 404: Country not found
        - 500: Internal server error

- `GET /api/countries`: Get all countries
    - Response: List of all country information
    - Status code: 200

- `GET /api/countries/{id}`: Get a specific country by Id saved in the database
    - Path variable: `id` - The id of the country
    - Response: Country information including languages
    - Status codes:
        - 200: OK
        - 404: Country not found

- `PUT /api/countries/{id}`: Update a country
    - Path variable: `id` - The id of the country to update
    - Request body: Updated country information
    - Response: Updated country information
    - Status codes:
        - 200: Updated successfully
        - 404: Country not found
        - 400: Bad request (invalid data)

- `DELETE /api/countries/{id}`: Delete a country
    - Path variable: `id` - The id of the country to delete
    - Response: No content
    - Status codes:
        - 204: Deleted successfully
        - 404: Country not found

