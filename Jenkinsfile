pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Jenkins credentials ID
        DOCKERHUB_USERNAME = 'wassim4592'
        BACKEND_IMAGE = "wassim4592/restaurant_backend:latest"
        FRONTEND_IMAGE = "wassim4592/restaurant_frontend:latest"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Backend JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Build & Push Backend Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        def backendImage = docker.build(env.BACKEND_IMAGE, '.')
                        backendImage.push('latest')
                    }
                }
            }
        }
        stage('Build & Push Frontend Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        def frontendImage = docker.build(env.FRONTEND_IMAGE, 'frontend')
                        frontendImage.push('latest')
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}

