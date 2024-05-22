pipeline {
    agent any

    stages {
        stage('CLONE') {
            steps {
                sh 'git clone https://github.com/BrodyGaudel/devops.git'
            }
        }
        stage('INSTALL') {
            steps {
                dir('devops/discovery-service') {
                    sh 'mvn clean install -DskipTests'
                }
                dir('devops/gateway-service') {
                    sh 'mvn clean install -DskipTests'
                }
                dir('devops/customer-service') {
                    sh 'mvn clean install -DskipTests'
                }
                dir('devops/account-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('TEST') {
            steps {
                dir('devops/customer-service') {
                    sh 'mvn test'
                }
                dir('devops/account-service') {
                    sh 'mvn test'
                }
            }
        }
        stage('PACKAGE') {
            steps {
                dir('devops/discovery-service') {
                    sh 'mvn package -DskipTests'
                }
                dir('devops/gateway-service') {
                    sh 'mvn package -DskipTests'
                }
                dir('devops/customer-service') {
                    sh 'mvn package -DskipTests'
                }
                dir('devops/account-service') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
        stage('IMAGE') {
            steps {
                dir('devops/discovery-service') {
                    sh 'docker build -t discovery-service .'
                }
                dir('devops/gateway-service') {
                    sh 'docker build -t gateway-service .'
                }
                dir('devops/customer-service') {
                    sh 'docker build -t customer-service .'
                }
                dir('devops/account-service') {
                    sh 'docker build -t account-service .'
                }
            }
        }
        stage('RUN') {
            steps {
                dir('devops') {
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}
