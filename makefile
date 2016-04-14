SHELL := /usr/bin/env bash

### pom.xml is the source of truth for project version and name (aka artifactId for maven)

build-props = target/classes/build.properties
$(build-props): compile
get-prop = $(shell cat $(build-props) | grep $1 | cut -d '=' -f 2)

name = $(call get-prop,name)
version = $(call get-prop,version)
release = $(name)-$(version)

port = 8081
dev-db-conn = jdbc:h2://file/./target/db/$(name)
server-db-conn = jdbc:h2://file/%/db/$(name)

# development targets

repl: compile
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
dev-db-%:
	mvn compile flyway:$* -Dflyway.url=$(dev-db-conn)

h2-shell:
	-rlwrap mvn exec:java -Dmaven.test.skip=true -Dexec.mainClass=org.h2.tools.Shell \
	-Dexec.args="-url $(dev-db-conn);AUTO_SERVER=TRUE"

# deployable application targets

server: db-migrate
	-echo ./target/$(release)-standalone | \
	xargs -I % bash -c "PORT=$(port) JDBC_DATABASE_URL=$(server-db-conn) %/bin/server.sh"

db-migrate: package
	-echo ./target/$(release)-standalone | \
	xargs -I % bash -c "JDBC_DATABASE_URL=$(server-db-conn) %/bin/migrate.sh"

tarball: package
	tar czvf ./target/$(release).tgz -C ./target $(release)-standalone

# deployment

heroku-deploy: check-env* clean package
	cd ./target/$(release)-standalone && \
	cp ../../Procfile . && \
	find ./* -name *.* -a ! -name *$(release).jar* | \
	tr '\n' ':' | \
	xargs heroku deploy:jar --app $(HEROKU_APP) --jar lib/$(release).jar --includes

heroku-jdbc-url: check-env*
	heroku run echo \$$JDBC_DATABASE_URL --app $(HEROKU_APP)

check-env*:
	@[[ ! -z "$$HEROKU_APP" ]] || \
	{ echo "Missing app name. 'export HEROKU_APP=yourname' or 'make deploy-heroku -e HEROKU_APP=yourname'" ; exit 1 ; }
