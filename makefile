SHELL := /usr/bin/env bash

name = java-spark-h2-sql2o-template
username = sa
version = 1.0.0-SNAPSHOT
port = 8081
dev-db = jdbc:h2:file:./target/db/java-spark-h2-sql2o-template
exec = exec:java -Dmaven.test.skip=true

go: mvn-compile
	-mvn $(exec) -Dexec.mainClass=com.testedminds.template.Service -Dexec.args="$(port) $(dev-db) $(username)"

mvn-%:
	mvn $*

## db targets

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
### example: make db-migrate
db-%:
	mvn compile flyway:$* -Dflyway.user=$(username) -Dflyway.url=$(dev-db)

h2-shell:
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell -Dexec.args="-url $(dev-db);AUTO_SERVER=TRUE -user $(username)"

## application targets

app: app-migrate app-run

app-run: mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/server.sh $(port) jdbc:h2:file:%/db/java-spark-h2-sql2o-template $(username)"

app-migrate: mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/migrate.sh jdbc:h2:file:%/db/java-spark-h2-sql2o-template $(username)"

release: mvn-package
	tar czvf ./target/$(name)-$(version).tgz -C ./target $(name)-$(version)-standalone
