# Money Account Application

## Task description
### We'd like to have money account application. Our team lead wants web application which tracks money flow of the organization.
The application will be hosted on a highly secured with its own auth system. So don't worry about authentication.

### The must-have features:
The App must be able to receive credit and debit transactions.
Any transaction, which leads to negative amount in the system, should be refused. Please provide http code which you think suits best for this situation.
The App must store transactions history.
In general the App will be used programmatically via its RESTful API.

### UX/UI requirements:
Additionally our team lead wants to perform some transactions via Web UI. He likes RIA application design very much so try to use some modern, nasty JS framework for it.
So please add Web Page with methods for sending transactions and display history list. It's better to have some color coding for credit and debit transactions.

## Build & Run server part:
1) cd project_folder_here
2) mvn clean install
3) java -jar ./target/money-account-application-0.1.0.jar

## Build & Run frontend part:
1) cd project_folder_here
2) cd ui
3) npm install
4) npm start
5) Open your favorite browser with URL: http://localhost:3000

## API documentation
URL: http://localhost:8080/app/swagger-ui.html

## Database client
http://localhost:8080/app/h2-console

Connection parameters:
JDBC URL: jdbc:h2:mem:money-account-application-db
User Name: sa
Password please left empty.
