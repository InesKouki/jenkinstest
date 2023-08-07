pipeline {
   agent any
     tools {
        jdk 'OpenJDK11'
        maven 'Maven'
    }
    
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "172.18.0.4:8081"
        NEXUS_REPOSITORY = "JenkinsNexus"
        NEXUS_CREDENTIAL_ID = "NEXUS_CRED"

        APP_NAME = "Jenkins-pipeline-test"
        RELEASE = "1.0.0"
        DOCKER_USER = "ineskouki"
        DOCKER_CRED_ID = "dockerhub"
        IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
       
    }
    stages {
            stage("Cleanup Workspace"){
            steps {
                cleanWs()
            }

        }
        stage('Checkout from SCM') {
            steps {
                git 'https://github.com/InesKouki/jenkinstest.git'
            }
        }
       stage("Build Application"){
            steps {
                sh "mvn clean package"
            }
        }
        stage("Test Application"){
            steps {
                sh "mvn test"
            }
        }
         

        stage('Build Docker Image') {
            steps {
                script {
                  sh 'docker build -t ineskouki/my-app-1.0 .'
                }
            }
        }
        stage('Deploy Docker Image') {
            steps {
                script {
                 withCredentials([string(credentialsId: 'dockerhub', variable: 'dockerhub')]) {
                    sh 'docker login -u ineskouki -p ${dockerhub}'
                 }  
                 sh 'docker push ineskouki/my-app-1.0'
                }
            }
        }

     

}
}
