SHELL := /usr/bin/env bash

# metadata

exec = exec:java -Dmaven.test.skip=true
RABBITMQ_URL="amqp://guest:guest@localhost"

### pom.xml is the source of truth for project version and name (aka artifactId for maven)
pom-version = /tmp/pom.version
pom-name = /tmp/pom.name

name = $(shell cat $(pom-name))
version = $(shell cat $(pom-version))

$(pom-version): $(pom.name)
	mvn help:evaluate -Dexpression=project.version | grep -v "INFO" > $@

$(pom-name): pom.xml
# Force maven to download all dependencies before attempting to read project metadata
	mvn dependency:resolve
	mvn help:evaluate -Dexpression=project.groupId
	mvn help:evaluate -Dexpression=project.artifactId | grep -v "INFO" > $@

pom: $(pom-name) $(pom-version)

# development targets

port = 8081
dev-db-conn = jdbc:h2://file/./target/db/$(name)
server-db-conn = jdbc:h2://file/%/db/$(name)

repl:
	mvn groovy:shell

deps-tree:
	mvn dependency:tree -Dverbose

clean:
	mvn clean

test:
	mvn test

compile:
	mvn compile

package:
	mvn package

package-skip-test:
	mvn package	-Dmaven.test.skip=true

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
### example: make dev-db-migrate h2-shell
dev-db-%: pom
	mvn compile flyway:$* -Dflyway.url=$(dev-db-conn)

h2-shell: pom
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell \
	-Dexec.args="-url $(dev-db-conn);AUTO_SERVER=TRUE"

# deployable application targets

server: pom db-migrate
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "PORT=$(port) JDBC_DATABASE_URL=$(server-db-conn) %/bin/server.sh"

db-migrate: pom package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "JDBC_DATABASE_URL=$(server-db-conn) %/bin/migrate.sh"

tarball: pom package
	tar czvf ./target/$(name)-$(version).tgz -C ./target $(name)-$(version)-standalone

# deployment

heroku-deploy: check-env* clean pom package
	cd ./target/$(name)-$(version)-standalone && \
	cp ../../Procfile . && \
	find ./* -name *.* -a ! -name *$(name)-$(version).jar* | \
	tr '\n' ':' | \
	xargs heroku deploy:jar --app $(HEROKU_APP) --jar lib/$(name)-$(version).jar --includes

heroku-jdbc-url: check-env*
	heroku run echo \$$JDBC_DATABASE_URL --app $(HEROKU_APP)

check-env*:
	@[[ ! -z "$$HEROKU_APP" ]] || \
	{ echo "Missing app name. 'export HEROKU_APP=yourname' or 'make deploy-heroku -e HEROKU_APP=yourname'" ; exit 1 ; }