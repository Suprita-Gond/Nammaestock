pipeline {
    agent any

    parameters {
        choice(
            name: 'ACTION',
            choices: ['DEPLOY', 'REMOVE'],
            description: 'Choose whether to deploy or remove containers'
        )
    }

    tools {
        maven 'maven'
    }

    environment {
        APP_NAME = "springboot-app"
    }

    stages {
        stage('Build JAR') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Building Spring Boot JAR..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy Application') {
            when {
                expression { params.ACTION == 'DEPLOY' }
            }
            steps {
                echo "Deploying Docker Containers..."
                // FIXED: Using docker-compose (with hyphen) instead of docker compose
                sh '''
                    docker-compose build
                    docker-compose up -d
                '''
            }
        }

        stage('Remove Application') {
            when {
                expression { params.ACTION == 'REMOVE' }
            }
            steps {
                echo "Stopping and Removing Containers..."
                // FIXED: Using docker-compose (with hyphen)
                sh 'docker-compose down'
                sh 'docker image prune -af'
            }
        }
    }
    post {
        success {
            echo "Pipeline executed successfully..."
        }
        failure {
            echo "Pipeline execution failed..."
        }
        always {
            echo "Pipeline completed..."
        }
    }
}