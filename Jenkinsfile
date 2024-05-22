pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git 'https://github.com/BrodyGaudel/devops.git'
            }
        }
        stage('Build and Test') {
            parallel {
                stage('Discovery-Service') {
                    steps {
                        dir('devops/discovery-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn package -DskipTests'
                            sh 'docker build -t mounanga/discovery-service:latest .'
                            sh 'docker push mounanga/discovery-service:latest'
                        }
                    }
                }
                stage('Gateway-Service') {
                    steps {
                        dir('devops/gateway-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn package -DskipTests'
                            sh 'docker build -t mounanga/gateway-service:latest .'
                            sh 'docker push mounanga/gateway-service:latest'
                        }
                    }
                }
                stage('Customer-Service') {
                    steps {
                        dir('devops/customer-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn test'
                            sh 'mvn package -DskipTests'
                            sh 'docker build -t mounanga/customer-service:latest .'
                            sh 'docker push mounanga/customer-service:latest'
                        }
                    }
                }
                stage('Account-Service') {
                    steps {
                        dir('devops/account-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn test'
                            sh 'mvn package -DskipTests'
                            sh 'docker build -t mounanga/account-service:latest .'
                            sh 'docker push mounanga/account-service:latest'
                        }
                    }
                }
            }
        }
        stage('Deploy with Docker Compose') {
            steps {
                dir('devops') {
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}
