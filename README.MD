## Building and running

Requirements:

    - java 17


### Building the app (commands should be executed from the project's root):

`./gradlew clean build`

Built jar files located here:

`build/libs/`

### Running the app

The app can be run in dev or default modes. 
In the dev mode the DB is populated with test data and H2 console exposed.


Running in `dev` mode: 

`SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun`

Running in `default` mode: 

`./gradlew bootRun`

By default, the app runs on `8080` port. 

#### Access the H2 console (dev mode only):

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:memdb

user: sa

password: (leave blank)


## Configuration

App can be configured using application.yaml file: 

`src/main/resources/application.yaml`

## Authentication

The app is using  HTTP basic authentication. 
Credentials are configured using `application.yaml`: 

`src/main/resources/application.yaml`

config params: 

`spring.security.user.name`

`spring.security.user.password`


Test request example: 

`curl -u user:Password_123! \
"http://localhost:8080/api/heatmap/answer-rate?dateInput=2025-04-20&numberOfShades=5"`



