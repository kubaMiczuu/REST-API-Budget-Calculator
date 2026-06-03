   # REST API Budget Calculator

A backend application designed for budget tracking and transaction management, built using Java and Spring Boot.

## How to start this application on your computer

### Requirements
* [Docker & Docker Compose](https://www.docker.com/) installed on your machine.
* *Note: You don't need Java or Maven installed locally – Docker will handle the entire build process inside the container!*

### Step-by-step instructions

1. **Clone this repository:**
   ```bash
   git clone https://github.com/kubaMiczuu/REST-API-Budget-Calculator.git
   cd REST-API-Budget-Calculator
   ```
2. **Configure environment variables:**
   Copy the example environment file and fill in your database credentials (name, username, password):
   ```bash
   cp .env.example .env
   ```
    or manually
3. **Build and run the project using Docker:**
   Open your terminal in the project root directory and execute:
   ```bash
   docker-compose up -d --build
   ```
     
### API Documentation & Testing
**Once the containers are running, you can explore, view, and test all the live endpoints using the interactive Swagger UI visualization.**

URL: http://localhost:8080/swagger-ui/index.html

### About used AI

I used AI (mostly Claude and Gemini) as a development assistant, primarily focusing on architectural decisions and design patterns. 

Additionally, I used it to resolve complex errors—such as issues with the `TransactionSpecification` class and understanding the core concepts of the Specification. AI was also helpful in configuring the `Dockerfile` and `docker-compose` setup, as well as scaffolding some of the unit and integration tests (with a strong emphasis on integration testing).

I treat AI like a personal help with things I do not understand completely or that i couldn't solve myself. It's like a tutor of mine that helps me to keep my code clean and effective, that simplyfies decision that I have to make.
