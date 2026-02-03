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
        DOCKER_IMAGE = 'chidi123/bank-app:1.0'
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
            when {
                expression {
                    BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    buildJar()
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
                    def dockerCmd = "docker run -d -p 8080:8080 ${DOCKER_IMAGE}"
                    echo 'Deploying the Bank API...'
                    sshagent(['ec2-server-key']) {

                        // some block
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@18.205.238.229 ${dockerCmd}"

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
