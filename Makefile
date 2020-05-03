.PHONY: frontend backend clean

.docker:
	mkdir .docker

backend/target/backend.jar: $(shell find -name '*.java' -o -name '*.sql' -o -name pom.xml)
	@echo '#######################'
	@echo '## Building backend  ##'
	@echo '#######################'
	JAVA_HOME=/usr/lib/jvm/java-14-adoptopenjdk/ mvn package

backend: target/simple-poll.jar
	@echo "Backend done :)"

frontend:
	@echo '#######################'
	@echo '## Building frontend ##'
	@echo '#######################'
	make -C frontend/

docker: .docker frontend backend
	cp target/simple-poll.jar .docker/SimplePoll.jar
	cp src/main/resources/example_config.yml .docker/config.yml
	cp src/docker/* .docker
	cp -r frontend/dist .docker
	(cd .docker && sudo docker build -t simplepoll .)
