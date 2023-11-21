pipeline {
    agent any

    stages {

        stage('Build app') {
            agent {
                docker {
                    image 'maven:3.8.6-openjdk-18'
                    args '-v /root/.m2:/root/.m2 -v /var/lib/jenkins/workspace:/out'
                }
            }
            steps {
                sh '''
                      mvn clean install
                      cp backend/target/backend-0.0.1-SNAPSHOT.jar /out/
                   '''
            }
        }

        stage('Stop and remove old docker image') {
            steps {
                sh '''
                        docker stop retrospecto || true
                    '''
            }
        }

        stage('Create new retrospecto docker image') {
            steps {
                sh '''mv ../backend-0.0.1-SNAPSHOT.jar .
                              docker build -t retrospecto-backend:latest .'''
            }
        }

        stage('Start retrospecto docker image') {
            steps {
                sh 'docker run -d -p 8098:8098 --name retrospecto --rm retrospecto-backend'
            }
        }
    }
}
