pipeline {
    agent any
    environment {
        JDBC_URL = credentials('JDBC_URL')
        JDBC_USER = credentials('JDBC_USER')
        JDBC_PASSWORD = credentials('JDBC_PASSWORD')
        TEST_JDBC_URL = credentials('TEST_JDBC_URL')
        TEST_JDBC_USER = credentials('TEST_JDBC_USER')
        TEST_JDBC_PASSWORD = credentials('TEST_JDBC_PASSWORD')

        // Define Docker Hub credentials ID
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        // Define Docker Hub repository name
        DOCKERHUB_REPO = 'aoiivantsov/inventory-management-system'
        // Define Docker image tag
        DOCKER_IMAGE_TAG = 'latest_v1'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/oiivantsov/inventory-management-system.git'
            }
        }
        stage('Prepare .env') {
            steps {
                bat '''
                echo JDBC_URL=%JDBC_URL% > .env
                echo JDBC_USER=%JDBC_USER% >> .env
                echo JDBC_PASSWORD=%JDBC_PASSWORD% >> .env
                echo TEST_JDBC_URL=%TEST_JDBC_URL% >> .env
                echo TEST_JDBC_USER=%TEST_JDBC_USER% >> .env
                echo TEST_JDBC_PASSWORD=%TEST_JDBC_PASSWORD% >> .env
                '''
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build Docker image
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                // Push Docker image to Docker Hub
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

    }
}