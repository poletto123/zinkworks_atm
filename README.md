## General info:

This is currently deployed in the cloud for easy access:

https://zinkworks-atm.herokuapp.com/

Note: if this link is not accessed for a few hours, it may take a while for it to load back up since Heroku
puts inactive servers to sleep.

This is an API system that has a front-end built with vanilla javascript (axios) and HTML/CSS.
It features Swagger API documentation and H2 in-memory database. It is secured with Spring Security,
which allows for separate accounts to be accessed.

It has 99% coverage in unit testing using Junit/Mockito, and exception handling for the most common errors.

Note: ATM bills are shared amongst all users, and can be depleted.

Test login/password:

123456789 \
1234

987654321 \
4321

## Technologies used:

* Spring Boot
* Spring Security
* Spring Data JPA
* H2
* Vanilla javascript
* Bootstrap
* Docker
* Junit
* Mockito

## Additional information:

For database access: \
/h2-console
* Url 'jdbc:h2:mem:testdb'
* username 'sa'
* no password

If needed, refer to data.sql and schema.sql for queries to reset data on DB

For Swagger API documentation:

/swagger-ui.html

Note: To make a request with Postman, you first need to send a POST request to /login with
form-data populated using parameters 'username' (accountNumber) and 'password' (PIN), which will create a cookie

## Running with Docker:

mvn clean install \
docker build -t zinkworks-atm . \
docker run -d -p 8080:8080 zinkworks-atm \
http://localhost:8080

## Running locally:

Open in your favorite IDE, import dependencies with Maven, build, and run