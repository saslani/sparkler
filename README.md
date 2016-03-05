### What is this template?
  > This template is designed for a Java 8 project and it uses simple and lightweight 
  [sprak](http://sparkjava.com/) micro framework. For the backend, this template uses [flyway](https://flywaydb.org/) 
  which is a database migration tool. 
   
### How to run it?
  > This template uses make and maven, you can find the available make commands in the makefile in addition to 
  all the maven commands. To run this template use: "make app", this command will create the database
  and runs the application on port 8081. 
 
### Why make
 >Instead of having a bin directory with a whole bunch of scripts it's much easier to have a make file with 
   very simple commands that takes care of your project release steps
 
### Deployable project
 >You can release this project using command "make release". This will create a gunzip file under "target" directory. 
 You can unzip the file anywhere, run migrage.sh (pass two arguments, url and username) and service.sh 
 (pass 3 arguments: port, url and username) and start using the live app.

### Separation of concerns
 > In this template I tried to make the models be as pure as possible, therefore used the ExampleRow to correspond
  to a row in the database, DAO to access the db, and payload to transfer JSON to Model and vice-versa where needed. 

   
### Architecture
  >* Language: Java
  * Framework: Spark Java
  * Build tool: maven and make
  * Database: H2Database - Lightweight SQL2o - migrations with Flyway
  * JSON parsing with Gson

### Useful links
  >* [asciiflow] (asciiflow.com)
  * [json editor] (http://www.jsoneditoronline.org/)
  * [spark web framework] (http://sparkjava.com/)
  * [markd] (http://marked2app.com/) 
  * [flyway] (http://flywaydb.org)
    * [flyway with H2] (http://flywaydb.org/documentation/database/h2.html)
    * [Existing database setup] (http://flywaydb.org/documentation/existing.html)
    * [why migrate](http://flywaydb.org/getstarted/why.html)
  
### Project structure 
  >* assembly
    * standalone.xml
  * bin
    * migrate.sh
    * server.sh
  * docs
    * any project document
  * src
    * main > java > package
        * db
        * models
        * exceptions
        * validators
        * payloads
        * Service.java
    * main > resources
        * db.migration
            * VX__Create_tables.sql
    * test > java > package    
  * .gitignore
  * makefile
  * pom.xml
  * README
  
