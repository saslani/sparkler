## Sparkler - A REST API microservice reference implementation

In 2002, Clinton Begin released [JPetStore](http://www.theserverside.com/news/thread.tss?thread_id=14243), "a completely rewritten Pet Store application based on Sun's original J2EE Pet Store." For those who don't remember, developing web applications in the early 2000's was a debacle based on J2EE and EJB's. Spring hadn't yet been released, and simple examples of Java web applications like JPetStore were in short supply.

Now nearly 15 years later, in the spirit of JPetStore, Sparkler demonstrates a simple, microservices-based approach to build, test, deploy, and monitor a small API server that allows you to GET, POST, PUT, and DELETE simple domain objects.

Sparkler's domain is an "Example" object with two string attributes.

Sparkler isn't about the domain: It's about demonstrating everything else required to deploy a small but production-ready API, while keeping things fun and productive for developers and ops teams. Complicated domains shouldn't require complicated applications: Sparkler provides a simple foundation on which to build powerful Java-based REST API's.


### Features

RESTful:

* Powered by the [Spark Framework](http://sparkjava.com), a Sinatra-inspired simple and lightweight Java web framework built for rapid development.
* JSON parsing with [Gson](https://github.com/google/gson).

Persistent:

* Automated database migrations with [Flyway](flywaydb.org).
* Lightweight SQL to object mapping using [sql2o](http://www.sql2o.org).

Developer-friendly:

* Fast, repeatable fully automated functional testing of the public REST API using JUnit and the [Apache HttpClient](https://hc.apache.org/httpcomponents-client-ga).
* Allows database migrations to be applied before each suite and tables cleared before each test case.
* Groovy-based REPL for interactive development.

Ready for [Continuous Delivery](http://continuousdelivery.com):

* Includes an automated deployment to Heroku.
* Includes a fully automated build for a versioned standalone tarball for other deployment environments.
* Public API defined by automated tests, providing a meaningful semantic versioning contract.
* Conforms to a [twelve-factor SaaS methodology](http://12factor.net).
* Logging configured with Log4j 2. The config file is outside of the compiled application, allowing redefinition of logging behavior without stopping or redeploying the server.


### Getting Started

Clone the repository and `cd` into the application directory.

Start the server: `make server`

This starts up the server in the same manner that will be used in the actual release.

Visit the welcome page: http://localhost:8081

See the `makefile` for other automation targets.

### Public API

POST: http://localhost:8081/examples
```
{
  "name" : "foo",
  "type" : "bar"
}
```

GET: http://localhost:8081/examples/1

PUT: http://localhost:8081/examples/1 with JSON as in POST

DELETE: http://localhost:8081/examples/1

See the [functional tests](./src/test/java/com/testedminds/template/RestfulApiFunctionalTest.java) for the full contract with Examples.


### Releasing

#### Update the version in pom.xml

Versioning follows [Semantic Versioning](http://semver.org) conventions: major.minor.patch

* The RestfulApiFunctionalTest suite defines the contract for the public API. If this API changes in a way that isn't backwards compatible, that's a major version change.
* Bug fixes and refactoring are patch revisions.
* Backwards compatible features are a minor revision.
* Follow the SemVer spec for anything else.

Commit the appropriate version change, then tag the repo:

```
git commit -am "Update version for release"
git push
git tag -a 1.0.0
git push --tags
```

### Deploying to Heroku

Sparkler includes support for a deployment to Heroku. You'll need the [Heroku Toolbelt](https://toolbelt.heroku.com/) and the heroku-deploy plugin for these steps.

Install the heroku-deploy plugin: `heroku plugins:install https://github.com/heroku/heroku-deploy`

Login with your Heroku credentials: `heroku login`

`cd` into the project directory and create an application: `heroku create`

Set a `HEROKU_APP` environment variable with the name of your project: `export HEROKU_APP=polar-sea-31843`

Deploy the project: `make heroku-deploy`

The included Procfile migrates the database before starting the application to ensure things stay up to date.

Check the logs to ensure everything starts up correctly: `heroku logs --tail`

If everything looks good, try out the Public API described above.


### Deploying via Tarball

#### Create the release artifact

Let's say we're deploying version 1.0.0.

`make tarball` will create `target/sparkler-1.0.0.tgz` from the `target/sparkler-1.0.0-standalone` directory.

#### Run the release

Unpack the tarball in the location of your choice with `tar xzf sparkler-1.0.0.tgz`:

```
sparkler-1.0.0-standalone/
├── bin      - scripts used to run the application
├── config   - log4j configuration file
└── lib      - all jars needed for the application
```

Switch to the directory where you untarred your distribution: `cd sparkler-1.0.0-standalone`

Run database migrations (assuming a local H2 database for the moment): `JDBC_DATABASE_URL=jdbc:h2://file/./db/sparkler bin/migrate.sh`

Start the service: `PORT=8081 JDBC_DATABASE_URL=jdbc:h2://file/./db/sparkler bin/server.sh`

#### Change the Log4j configuration at runtime (optional)

It can be useful to be able to switch the logging levels of one of the applications sub-systems without shutting down.
Try hitting an invalid url like http://localhost:8081/examples/fubar. Note the WARN message in the logs.

Edit `config/log4j2.xml` to change the com.testedminds logger level to error. Within 30 seconds (or the value you set in monitorInterval), the application will reload the log4j config and you should see a message in the console that the config was modified. Try visiting the invalid url again, and notice how there is now no log entry.


### FAQ

#### make?

Yes. Instead of having a bin directory with a whole bunch of scripts, it's much easier to have a `makefile` with very simple commands that take care of your project's frequently used tasks.

`make` is also already on just about everyone's machine, unless you're on Windows. Even then, it's available.

#### Have you tested developing on Windows?

No. This has been build on OS X Yosemite and El Capitan. YMMV.

#### This is interesting. How can I contribute?

Check the issues and send a PR!

### Useful Links

These came in handy while building Sparkler:

* [Postman](https://www.getpostman.com) for simple interactive, ad-hoc RESTful API testing
* [asciiflow](asciiflow.com) for quick plain-text whiteboard-like sessions
* [json editor](http://www.jsoneditoronline.org)
* [marked](http://marked2app.com) for nice Markdown previews
* [API-Craft discussion group](https://groups.google.com/forum/?fromgroups=#!forum/api-craft)

Flyway is a simple solution to a simple problem:

* [flyway with H2](http://flywaydb.org/documentation/database/h2.html)
* [flyway for an existing database setup](http://flywaydb.org/documentation/existing.html)
* [Why migrate?](http://flywaydb.org/getstarted/why.html)

