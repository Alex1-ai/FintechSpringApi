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
        DATABASE_URL = credentials("DATABASE_URL")
        MAIL_USERNAME = credentials("MAIL_USERNAME")
        MAIL_PASSWORD = credentials("MAIL_PASSWORD")
        JWT_SECRET = credentials("JWT_SECRET")
        JWT_EXPIRATION = credentials("JWT_EXPIRATION")
        FRONTEND_URL = credentials("FRONTEND_URL")
    }



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



       stage("provision server") {
//            tf proviosion server
            environment {
                AWS_ACCESS_KEY_ID = credentials("jenkins_aws_access_key_id")
                AWS_SECRET_ACCESS_KEY = credentials("jenkins_aws_secret_access_key")
                TF_VAR_my_ip = credentials("my_ip")
                TF_VAR_jenkins_ip = credentials("jenkins_ip")
                TF_VAR_env_prefix = "test"
            }
            steps {
               script {
                  dir('terraform'){
                     sh "terraform init"
                     sh "terraform apply --auto-approve"
                     EC2_PUBLIC_IP = sh(
                        script: "terraform output ec2_public_ip",
                        returnStdout: true
                        ).trim()


                  }


               }


            }

       }
       stage('Deploy') {
            environment {
                 DOCKER_CRED = credentials("docker-hub-repo")


            }
            steps {
                // Deploy the application (this is a placeholder, replace with actual deployment steps)
                script {
                    echo "waiting for EC2 server to initialize"
                    sleep(time: 90, unit: "SECONDS")
//                     def dockerCmd = "docker run --env-file .env -d -p 8080:8080 ${DOCKER_IMAGE}"
//                     def dockerComposeCmd = "docker compose -f docker-compose.yaml up --detach"
                    echo 'Deploying the Bank API...'
                    echo "${EC2_PUBLIC_IP}"


                    def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME} ${DOCKER_CRED_USR} ${DOCKER_CRED_PSW}"
                    def ec2Instance = "ec2-user@${EC2_PUBLIC_IP}"


                    sshagent(['server-ssh-key']) {
                        sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user"
                        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"


                        // Create .env file on the EC2 instance
                        sh """
                            ssh -o StrictHostKeyChecking=no ${ec2Instance} '
                                cat > /home/ec2-user/.env << EOF
DATABASE_URL=${DATABASE_URL}
MAIL_USERNAME=${MAIL_USERNAME}
MAIL_PASSWORD=${MAIL_PASSWORD}
JWT_SECRET=${JWT_SECRET}
JWT_EXPIRATION=${JWT_EXPIRATION}
FRONTEND_URL=${FRONTEND_URL}
EOF
                            '
                        """

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
                            git push https://${GIT_USER}:${GIT_PASS}@github.com/Alex1-ai/FintechSpringApi.git HEAD:feature/sshagent-terraform
                        '''
                    }
                }
            }
        }

    }


}
