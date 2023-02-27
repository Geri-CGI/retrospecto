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
    }
}