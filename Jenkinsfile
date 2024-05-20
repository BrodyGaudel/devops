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
                            sh 'mvn deploy'
                            sh 'docker build -t brodygaudel/discovery-service:latest .'
                            sh 'docker push brodygaudel/discovery-service:latest'
                        }
                    }
                }
                stage('Gateway-Service') {
                    steps {
                        dir('devops/gateway-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn package -DskipTests'
                            sh 'mvn deploy'
                            sh 'docker build -t brodygaudel/gateway-service:latest .'
                            sh 'docker push brodygaudel/gateway-service:latest'
                        }
                    }
                }
                stage('Customer-Service') {
                    steps {
                        dir('devops/customer-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn test'
                            sh 'mvn package -DskipTests'
                            sh 'mvn deploy'
                            sh 'docker build -t brodygaudel/customer-service:latest .'
                            sh 'docker push brodygaudel/customer-service:latest'
                        }
                    }
                }
                stage('Account-Service') {
                    steps {
                        dir('devops/account-service') {
                            sh 'mvn clean install -DskipTests'
                            sh 'mvn test'
                            sh 'mvn package -DskipTests'
                            sh 'mvn deploy'
                            sh 'docker build -t brodygaudel/account-service:latest .'
                            sh 'docker push brodygaudel/account-service:latest'
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