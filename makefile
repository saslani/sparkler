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

go: pom mvn-compile
	-mvn $(exec) -Dexec.mainClass=com.testedminds.template.Server -Dexec.args="$(port) $(db-conn) $(db-user)"

### wrapper for maven commands
mvn-%:
	mvn $*

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
### example: make dev-db-migrate h2-shell
dev-db-%: pom
	mvn compile flyway:$* -Dflyway.user=$(db-user) -Dflyway.url=$(db-conn)

h2-shell: pom
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell -Dexec.args="-url $(db-conn);AUTO_SERVER=TRUE -user $(db-user)"

# application targets

server: pom db-migrate
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/server.sh $(port) jdbc:h2:file:%/db/$(name) $(db-user)"

db-migrate: pom mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/migrate.sh jdbc:h2:file:%/db/$(name) $(db-user)"

release: pom mvn-package
	tar czvf ./target/$(name)-$(version).tgz -C ./target $(name)-$(version)-standalone
