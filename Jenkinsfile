pipeline {
    agent any
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "172.18.0.4:8081"
        NEXUS_REPOSITORY = "JenkinsNexus"
        NEXUS_CREDENTIAL_ID = "NEXUS_CRED"
    }
    stages {
        stage('GIT Checkout ') {
            steps {
                // Checkout your Maven project from Git
                // test
                git 'https://github.com/InesKouki/jenkinstest.git'
            }
        }
        
        stage('Build') {
            steps {
                // Declare the Maven path using 'withMaven'
                withMaven(maven: 'Maven') {
                    // Run the Maven build
                    sh 'mvn clean package'
                }
            }
        }

          stage('Test') {
            steps {
                // Declare the Maven path using 'withMaven'
                withMaven(maven: 'Maven') {
                    // Run the tests with Maven
                    sh 'mvn test'
                }
            }
            
            post {
                // Archive the test results
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                // Run SonarQube scanner with custom PATH
                withSonarQubeEnv('Sonarqube') {
                    // Add Maven binary to PATH
                    script {
                        def mavenHome = tool 'Maven'
                        env.PATH = "${mavenHome}/bin:${env.PATH}"
                    }

                    // Run the SonarQube analysis on your Maven project
                    sh 'mvn sonar:sonar -Dsonar.host.url=http://172.18.0.3:9000 -Dsonar.login=squ_b3452d6629db6a310d42645b4361740d1e0e8bc9'
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

}
}
