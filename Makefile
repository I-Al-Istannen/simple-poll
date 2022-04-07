.PHONY: frontend backend clean

all: backend frontend

clean:
	rm -rf .docker
	mvn clean
	make -C frontend clean

.docker:
	mkdir .docker

target/simple-poll.jar: $(shell find -name '*.java' -o -name '*.sql' -o -name pom.xml)
	@echo '#######################'
	@echo '## Building backend  ##'
	@echo '#######################'
	mvn package

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
	(cd .docker && sudo docker build -t registry.ialistannen.de/uni/simplepoll .)
