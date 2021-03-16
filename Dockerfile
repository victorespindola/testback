FROM adoptopenjdk/openjdk11:alpine-slim

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
ARG MAVEN_BIN_URL=https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz

RUN mkdir /opt/app
WORKDIR /opt/app

RUN apk add --no-cache curl

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz ${MAVEN_BIN_URL} \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY . .
RUN mvn clean package

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./target/testback-1.0.jar"]
