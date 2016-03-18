SHELL := /usr/bin/env bash

name = sparkler
version = 1.0.0-SNAPSHOT

username = sa
port = 8081
dev-db = jdbc:h2:file:./target/db/$(name)
exec = exec:java -Dmaven.test.skip=true

# development targets

go: mvn-compile
	-mvn $(exec) -Dexec.mainClass=com.testedminds.template.Server -Dexec.args="$(port) $(dev-db) $(username)"

### wrapper for maven commands
mvn-%:
	mvn $*

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
### example: make dev-db-migrate
dev-db-%:
	mvn compile flyway:$* -Dflyway.user=$(username) -Dflyway.url=$(dev-db)

h2-shell:
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell -Dexec.args="-url $(dev-db);AUTO_SERVER=TRUE -user $(username)"

# application targets

server: db-migrate
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/server.sh $(port) jdbc:h2:file:%/db/$(name) $(username)"

db-migrate: mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/migrate.sh jdbc:h2:file:%/db/$name $(username)"

release: mvn-package
	tar czvf ./target/$(name)-$(version).tgz -C ./target $(name)-$(version)-standalone
