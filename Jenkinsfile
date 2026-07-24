pipeline {
    agent any

    tools {
        maven 'maven'
    }

    parameters {
        choice(
            name: 'ACTION',
            choices: [
                'BUILD_AND_PUSH',
                'DEPLOY_DATABASE',
                'DEPLOY_APPLICATION',
                'REMOVE_APPLICATION',
                'REMOVE_DATABASE'
            ],
            description: 'Choose pipeline action'
        )
    }


    environment {
        IMAGE_NAME = "suprita11/nammaestock"
        IMAGE_TAG = "v1"
    }


    stages {


        stage('Prepare Build') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                echo "Preparing build..."
                sh 'mvn clean'
            }
        }



        stage('Build Project') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                sh 'mvn package'
            }
        }



        stage('Build Docker Image') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                sh 'docker build -t nammaestock:v1 .'
            }
        }



        stage('Docker Login') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    '''
                }
            }
        }



        stage('Tag Docker Image') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                sh 'docker tag nammaestock:v1 $IMAGE_NAME:$IMAGE_TAG'
            }
        }



        stage('Push Docker Image') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                sh 'docker push $IMAGE_NAME:$IMAGE_TAG'
            }
        }



        stage('Deploy Database') {

            when {
                expression {
                    params.ACTION == 'DEPLOY_DATABASE'
                }
            }

            steps {

                sh '''
                kubectl apply -f kubernetes/mysql-statefulset.yaml
                kubectl apply -f kubernetes/mysql-service.yaml
                '''
            }
        }



        stage('Deploy Application') {

            when {
                expression {
                    params.ACTION == 'DEPLOY_APPLICATION'
                }
            }

            steps {

                sh '''
                kubectl apply -f kubernetes/nammaestock-deployment.yaml
                kubectl apply -f kubernetes/nammaestock-service.yaml
                '''
            }
        }



        stage('Remove Application') {

            when {
                expression {
                    params.ACTION == 'REMOVE_APPLICATION'
                }
            }

            steps {

                sh '''
                kubectl delete -f kubernetes/nammaestock-deployment.yaml
                kubectl delete -f kubernetes/nammaestock-service.yaml
                '''
            }
        }



        stage('Remove Database') {

            when {
                expression {
                    params.ACTION == 'REMOVE_DATABASE'
                }
            }

            steps {

                sh '''
                kubectl delete -f kubernetes/mysql-statefulset.yaml
                kubectl delete -f kubernetes/mysql-service.yaml
                '''
            }
        }



        stage('Clean System') {

            when {
                expression {
                    params.ACTION == 'BUILD_AND_PUSH'
                }
            }

            steps {
                sh 'docker system prune -f'
            }
        }
    }


    post {

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}