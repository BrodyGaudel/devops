pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        DOCKER_HUB_REPO = 'mounanga'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-credentials'
    }

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
                    sh 'docker build -t ${DOCKER_HUB_REPO}/discovery-service .'
                }
                dir('devops/gateway-service') {
                    sh 'docker build -t ${DOCKER_HUB_REPO}/gateway-service .'
                }
                dir('devops/customer-service') {
                    sh 'docker build -t ${DOCKER_HUB_REPO}/customer-service .'
                }
                dir('devops/account-service') {
                    sh 'docker build -t ${DOCKER_HUB_REPO}/account-service .'
                }
            }
        }
        stage('PUSH') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh 'docker push ${DOCKER_HUB_REPO}/discovery-service'
                        sh 'docker push ${DOCKER_HUB_REPO}/gateway-service'
                        sh 'docker push ${DOCKER_HUB_REPO}/customer-service'
                        sh 'docker push ${DOCKER_HUB_REPO}/account-service'
                    }
                }
            }
        }
        stage('DEPLOY') {
            steps {
                script {
                    withCredentials([file(credentialsId: KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
                        sh 'kubectl apply -f devops/k8s/mysql-service.yaml'
                        sh 'kubectl apply -f devops/k8s/axon-server.yaml'
                        sh 'kubectl apply -f devops/k8s/discovery-service.yaml'
                        sh 'kubectl apply -f devops/k8s/gateway-service.yaml'
                        sh 'kubectl apply -f devops/k8s/customer-service.yaml'
                        sh 'kubectl apply -f devops/k8s/account-service.yaml'
                    }
                }
            }
        }
    }
}
