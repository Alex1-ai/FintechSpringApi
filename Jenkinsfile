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
//     environment {
//         // Define any environment variables here
//         DOCKER_IMAGE = 'chidi123/bank-app:1.2'
//     }



    stages {

        stage('increment version') {
//             when {
//                 expression {
//                     BRANCH_NAME == 'main'
//                 }
//             }
            steps {
                script {
                    echo "incrementing app version...."

                    sh """
                    mvn build-helper:parse-version versions:set \
                      -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                      versions:commit
                    """

                    def matcher = readFile('pom.xml') =~ '<version>(.*)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "chidi123/bank-app:${version}-${BUILD_NUMBER}"
                }
            }
        }
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
                        buildImage "${IMAGE_NAME}"
                        dockerLogin()
                        dockerPush "${IMAGE_NAME}"


                }
            }
        }
       stage('Deploy') {
            steps {
                // Deploy the application (this is a placeholder, replace with actual deployment steps)
                script {
//                     def dockerCmd = "docker run --env-file .env -d -p 8080:8080 ${DOCKER_IMAGE}"
//                     def dockerComposeCmd = "docker compose -f docker-compose.yaml up --detach"
                    def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
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
        stage("commit version update") {


            steps {
                script {
                    withCredentials([
                            usernamePassword(
                                    credentialsId: 'github-access-token-credentials',
                                    usernameVariable: 'GIT_USER',
                                    passwordVariable: 'GIT_PASS'
                            )
                    ]) {
                        sh '''
                            set -e

                            # Configure git
                            git config --global user.email "jenkins@example.com"
                            git config --global user.name "jenkins"

                            # Check current branch/state
                            echo "Current branch/state:"
                            git branch -a || true
                            git status

                            # Commit changes on detached HEAD
                            git add pom.xml
                            git commit -m "ci: version bump" || echo "No changes to commit"

                            # Push directly to main (force if needed)
                            git push https://${GIT_USER}:${GIT_PASS}@github.com/Alex1-ai/FintechSpringApi.git HEAD:main
                        '''
                    }
                }
            }
        }

    }


}
