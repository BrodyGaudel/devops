# E-BANK Application

E-BANK is a banking management application developed using a microservices architecture. It allows managing customer bank accounts and operations such as credit and debit. This application is composed of several microservices developed with modern technologies.

## Microservices

- **discovery-service**: Discovery service using Spring Cloud Netflix Eureka.
- **gateway-service**: API Gateway using Spring Cloud Gateway.
- **customer-service**: Customer management microservice.
- **account-service**: Bank account management microservice, developed using CQRS and Event-Sourcing architecture with Axon Framework.

## Architecture

The architecture of this application is based on microservices, which allows for scalability and easier maintenance. The `account-service` microservice uses CQRS (Command Query Responsibility Segregation) and Event-Sourcing to efficiently and reliably manage bank account operations.

## Technologies Used

### Development

- **Java**
- **Spring Boot**
- **Axon Framework**
- **Axon Server**
- **MySQL**

### Deployment

- **Docker**
- **Kubernetes**
- **Jenkins**
- **Nexus**
- **SonarQube**
- **Maven**
- **Git**

## Prerequisites

- **Docker**: Ensure Docker is installed and running.
- **Kubernetes**: A Kubernetes cluster should be configured and accessible.
- **Jenkins**: Jenkins should be installed to run CI/CD pipelines.
- **Maven**: Maven should be installed to build the Java projects.
- **Git**: Git should be installed to clone repositories.

## Installation and Deployment

### Clone the Repository

```bash
git clone https://github.com/BrodyGaudel/devops.git
cd devops
```

### Build the Services

Use Maven to build the Java services:

```bash
cd discovery-service
mvn clean install -DskipTests
cd ../gateway-service
mvn clean install -DskipTests
cd ../customer-service
mvn clean install -DskipTests
cd ../account-service
mvn clean install -DskipTests
```

### Build Docker Images

```bash
docker build -t mounanga/discovery-service ./discovery-service
docker build -t mounanga/gateway-service ./gateway-service
docker build -t mounanga/customer-service ./customer-service
docker build -t mounanga/account-service ./account-service
```

### Push Images to Docker Hub

Ensure your Docker credentials are configured in Jenkins.

```bash
docker push mounanga/discovery-service
docker push mounanga/gateway-service
docker push mounanga/customer-service
docker push mounanga/account-service
```

### Deploy to Kubernetes

Deploy the services to the Kubernetes cluster:

```bash
kubectl apply -f k8s/discovery-service.yaml
kubectl apply -f k8s/gateway-service.yaml
kubectl apply -f k8s/customer-service.yaml
kubectl apply -f k8s/account-service.yaml
kubectl apply -f k8s/mysql-service.yaml
kubectl apply -f k8s/axon-server.yaml
```

## Jenkins Pipeline

The Jenkins pipeline is defined in the `Jenkinsfile` and includes the following stages:

1. **CLONE**: Clone the Git repository.
2. **INSTALL**: Build the services with Maven.
3. **TEST**: Run unit tests.
4. **PACKAGE**: Package the services.
5. **IMAGE**: Build Docker images.
6. **PUSH**: Push Docker images to Docker Hub.
7. **DEPLOY**: Deploy the services to Kubernetes.

## Monitoring and Code Quality

- **SonarQube**: Used for code quality analysis.
- **Nexus**: Used as a repository manager for Maven dependencies.

## Contributions

Contributions are welcome! Please submit a pull request for any improvements or fixes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

For any questions or assistance, please open an issue on the GitHub repository.

Thank you for using E-BANK!