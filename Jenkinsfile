pipeline {
    agent any

    stages {
        stage('CLONE') {
            steps {
                sh 'git clone https://github.com/BrodyGaudel/devops.git'
            }
        }
        stage('INSTALL'){
            steps{
                sh 'cd discovery-service'
                sh 'mvn clean install -DskipTests'
                sh 'cd ..'
                sh 'cd gateway-service'
                sh 'mvn clean install -DskipTests'
                sh 'cd ..'
                sh 'cd customer-service'
                sh 'mvn clean install -DskipTests'
                sh '..'
                sh 'cd account-service'
                sh 'mvn clean install -DskipTests'
                sh 'cd ..'
            }
        }
        stage('TEST'){
            steps{
                sh 'cd customer-service'
                sh 'mvn test'
                sh '..'
                sh 'cd account-service'
                sh 'mvn test'
                sh 'cd ..'
            }
        }
        stage('PACKAGE'){
            steps{
                sh 'cd discovery-service'
                sh 'mvn package -DskipTests'
                sh 'cd ..'
                sh 'cd gateway-service'
                sh 'mvn package -DskipTests'
                sh 'cd ..'
                sh 'cd customer-service'
                sh 'mvn package -DskipTests'
                sh '..'
                sh 'cd account-service'
                sh 'mvn package -DskipTests'
                sh 'cd ..'
            }
        }
        stage('IMAGE') {
            steps{
                sh 'cd discovery-service'
                sh 'docker build -t discovery-service .'
                sh 'cd ..'
                sh 'cd gateway-service'
                sh 'docker build -t gateway-service .'
                sh 'cd ..'
                sh 'cd customer-service'
                sh 'docker build -t customer-service .'
                sh '..'
                sh 'cd account-service'
                sh 'docker build -t account-service .'
                sh 'cd ..'
            }
        }
        stage('RUN') {
            steps{
                sh 'docker-compose up -d'
            }
        }
    }
}
