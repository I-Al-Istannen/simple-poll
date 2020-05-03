
.docker:
	mkdir .docker

docker: .docker
	JAVA_HOME=/usr/lib/jvm/java-14-adoptopenjdk/ mvn package
	cp target/simple-poll.jar .docker/SimplePoll.jar
	cp src/main/resources/example_config.yml .docker/config.yml
	cp src/docker/Dockerfile .docker
	(cd .docker && sudo docker build -t simplepoll .)
