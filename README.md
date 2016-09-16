##Student Manager(Sample project demonstrating setting up spring security on basic rest)

###How to build

**Pre-requisites:** jdk 8 is necessary

####Build Steps
**./gradlew clean build**

**java -jar build/libs/student-manager-0.0.1.jar**

##### This starts jetty server on port 8080
  * /GET localhost:8080/students           - Lists all students
  
  * /GET localhost:8080/students/{id}      - Provides details of a particular student
  
  * /POST localhost:8080/students          - To Create a student
       **Body**   {"name":"Sample Student"}
       **Content-Type**  application/json
  
  * /PUT localhost:8080/students           - To Update a student
       **Body**   {"name":"Sample Student"}
       **Content-Type**  application/json

#### Use Application

* **Authentication is done using BasicAuthentication.**
* **Any user password credentials with same username and password is valid**
* **Sample header "Authorization" is "Basic cmVkZHk6cmVkZHk=" for username/password as reddy/reddy **