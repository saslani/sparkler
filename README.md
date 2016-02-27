### Architecture
  * Language: Java
  * Framework: Spark Java
  * Build tool: maven and make
  * Database: H2Database - Lightweight SQL2o - migrations with Flyway
  * JSON parsing with Gson

### Useful links
  * [asciiflow] (asciiflow.com)
  * [json editor] (http://www.jsoneditoronline.org/)
  * [spark web framework] (http://sparkjava.com/)
  * [markd] (http://marked2app.com/) 
  * [flyway] (http://flywaydb.org)
    * [flyway with H2] (http://flywaydb.org/documentation/database/h2.html)
    * [Existing database setup] (http://flywaydb.org/documentation/existing.html)
  
### Project structure 
  * assembly
    * standalone.xml
  * bin
    * migrate.sh
    * server.sh
  * docs
    * any project document
  * src
    * main > java > package
        * db
        * internal (models, exceptions, validators)
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
  
### About the DAO
### Separation of concerns
### Why did I separate ExampleDao from Example model?
### Why make
    * instead of having a bin directory with a whole bunch of scripts it's much easier to have a make file with 
    very simple commands that takes care of your project release steps
### Make target for project release
### Deployable target

### DB commands
  * create the database and tables
    
  * delete the database
### How to build the project and release this project
  