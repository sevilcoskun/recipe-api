# Objective
Create a standalone java application which allows users to manage their favourite recipes. It should allow adding, updating, removing and fetching recipes. Additionally users should be able to filter available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.
For example, the API should be able to handle the following search requests:
• All vegetarian recipes
• Recipes that can serve 4 persons and have “potatoes” as an ingredient
• Recipes without “salmon” as an ingredient that has “oven” in the instructions.
## Requirements
Please ensure that we have some documentation about the architectural choices and also how to run the application. The project is expected to be delivered as a GitHub (or any other public git hosting) repository URL.
All these requirements needs to be satisfied:
1. It must be a REST application implemented using Java (use a framework of your choice)
2. Your code should be production-ready.
3. REST API must be documented
4. Data must be persisted in a database
5. Unit tests must be present
6. Integration tests must be present

# Tech Requirements
- Java 17
- Maven 3.x
- Spring-boot 2.x
- Swagger 2
- OpenAPI v3
- Lombok
- H2 Database
- JUnit and Mockito

# Application Overview
N-tier architecture approach is applied. Three main layers namely Controller, Service and Repository matches the Web, Business and Data tiers respectively.<br />
Repository later is an abstracted way to connect different data sources at the same time
<br />
Service layer is used to define business rules and checks
<br />
Controller layer is used to navigate coming requests into api and direct them with related services

# How to Run the applicaiton
### Prerequsite:
- Install Java 17
- Install Maven 3.7 or higehr 

### Preperation
- Pull this repository into your local

### Build & Test
> mvn clean install

### Run
> mvn sprint-boot:run

# How to connect in memory Database
You can check the persistet data via connectiong to H2 database from your browser, username and password filled by default
> localhost:8080/h2

# Swagger Page
browse in local: http://localhost:8080/swagger-ui/index.html#/

## Postman
- Download `RecipeAPI_Postman_Collection.json`
- Import that json in your Postman
- Ready to use! 






