SHELL := /usr/bin/env bash

# metadata

exec = exec:java -Dmaven.test.skip=true

### pom.xml is the source of truth for project version and name (aka artifactId for maven)
pom-version = /tmp/pom.version
pom-name = /tmp/pom.name

name = $(shell cat $(pom-name))
version = $(shell cat $(pom-version))

$(pom-version): pom.xml
	mvn help:evaluate -Dexpression=project.version | grep -v "INFO" > $@

$(pom-name): pom.xml
	mvn help:evaluate -Dexpression=project.artifactId | grep -v "INFO" > $@

pom: $(pom-name) $(pom-version)

# development targets

port = 8081
db-user = sa
db-conn = jdbc:h2:file:./target/db/$(name)

go: pom compile
	-mvn $(exec) -Dexec.mainClass=com.testedminds.template.Server \
	-Dlog4j.configurationFile="./config/log4j2.xml" \
	-Dexec.args="--port $(port) --url $(db-conn) --user $(db-user)"

help: pom compile
	mvn $(exec) -Dexec.mainClass=com.testedminds.template.Server -Dexec.args="--help"

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

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
### example: make dev-db-migrate h2-shell
dev-db-%: pom
	mvn compile flyway:$* -Dflyway.user=$(db-user) -Dflyway.url=$(db-conn)

h2-shell: pom
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell \
	-Dexec.args="-url $(db-conn);AUTO_SERVER=TRUE -user $(db-user)"

# deployable application targets

server: pom db-migrate
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/server.sh $(port) jdbc:h2:file:%/db/$(name) $(db-user)"

db-migrate: pom package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/migrate.sh jdbc:h2:file:%/db/$(name) $(db-user)"

tarball: pom package
	tar czvf ./target/$(name)-$(version).tgz -C ./target $(name)-$(version)-standalone

# deployment

deploy-heroku: check-env* pom package
	cd ./target/$(name)-$(version)-standalone && \
	cp ../../Procfile . && \
	find ./* -name *.* -a ! -name *$(name)-$(version).jar* | \
	tr '\n' ':' | \
	xargs heroku deploy:jar --app $(HEROKU_APP) --jar lib/$(name)-$(version).jar --includes

check-env*:
	@[[ ! -z "$$HEROKU_APP" ]] || \
	{ echo "Missing app name. 'export HEROKU_APP=yourname' or 'make deploy-heroku -e HEROKU_APP=yourname'" ; exit 1 ; }

