#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
   [$class: 'GitSCMSource',
    remote: 'https://github.com/Alex1-ai/jenkins-shared-library',
    credentialsId: 'github-credentials'
   ]
)


pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    }
    environment {
        // Define any environment variables here
        DOCKER_IMAGE = 'chidi123/bank-app:1.2'
    }

    stages {
        stage('test') {
            steps {
                script {
                    echo 'This is a test stage to verify Jenkins pipeline setup.'
                    sh "mvn test"
                }
            }
        }

        stage("build") {
            steps {
                script {
                    sh "mvn clean package -DskipTests"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build the Docker image
                script {
                    // docker.build(DOCKER_IMAGE)

                        echo "Building a docker application"
                        buildImage(env.DOCKER_IMAGE)
                        dockerLogin()
                        dockerPush(env.DOCKER_IMAGE)


                }
            }
        }
       stage('Deploy') {
            steps {
                // Deploy the application (this is a placeholder, replace with actual deployment steps)
                script {
//                     def dockerCmd = "docker run --env-file .env -d -p 8080:8080 ${DOCKER_IMAGE}"
//                     def dockerComposeCmd = "docker compose -f docker-compose.yaml up --detach"
                    def shellCmd = "bash ./server-cmds.sh ${DOCKER_IMAGE}"
                    def ec2Instance = "ec2-user@18.205.238.229"
                    echo 'Deploying the Bank API...'
                    sshagent(['ec2-server-key']) {
                        sh "scp server-cmds.sh ${ec2Instance}:/home/ec2-user"
                        sh "scp docker-compose.yaml ${ec2Instance}:/home/ec2-user"

                        // some block
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"

                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up resources, send notifications, etc.
            echo 'Pipeline completed.'
        }
    }
}
