SHELL := /usr/bin/env bash

name = java-spark-h2-sql2o-template
version = 1.0.0-SNAPSHOT
port = 8081
dev-db = jdbc:h2:file:./target/db/java-spark-h2-sql2o-template
exec = exec:java -Dmaven.test.skip=true

go: mvn-compile
	-PORT=$(port) mvn $(exec) -Dexec.mainClass=Service

mvn-%:
	mvn $*

## db targets

### See http://flywaydb.org/documentation/maven/ for list of flyway commands
db-%:
	mvn compile flyway:$* -Dflyway.user=sa -Dflyway.url=$(dev-db)

h2-shell:
	-rlwrap mvn $(exec) -Dexec.mainClass=org.h2.tools.Shell -Dexec.args="-url $(dev-db);AUTO_SERVER=TRUE -user sa"


## application targets

app: app-migrate app-run

app-run: mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/server.sh $(port) jdbc:h2:file:%/db/java-spark-h2-sql2o-template sa"

app-migrate: mvn-package
	-echo ./target/$(name)-$(version)-standalone | \
	xargs -I % bash -c "%/bin/migrate.sh jdbc:h2:file:%/db/java-spark-h2-sql2o-template sa"
