pipeline {
    agent any
     tools {
        jdk 'OpenJDK8'
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
         stage("Sonarqube Analysis") {
            steps {
                script {
                    withSonarQubeEnv('Sonarqube') {
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }
        
        stage("Quality Gate") {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
                }
            }

        }
        
        stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
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
