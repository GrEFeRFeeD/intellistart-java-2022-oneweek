# syntax=docker/dockerfile:1

FROM eclipse-temurin:11
WORKDIR /app

# Get Maven wrapper and pom.xml file into our image.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY checkstyle.xml .

# Install dependencies into the image.
# The `go-offline` goal of the Maven Dependency plugin downloads all the required dependencies and plugins for the project, based on the pom file.
RUN ./mvnw dependency:go-offline

# Add our source code into the image
COPY src ./src

# Tell Docker what command we want to run when our image is executed inside a container.
CMD ["./mvnw", "spring-boot:run"]