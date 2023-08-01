pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                // Checkout your Maven project from Git
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
        stage('Test Credentials') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'Nexus-user', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                echo "Nexus Username: ${NEXUS_USERNAME}"
                echo "Nexus Password: ${NEXUS_PASSWORD}"
            }
        }
    }
        }
        
        stage('Publish to Nexus') {
    steps {
      withCredentials([usernamePassword(credentialsId: 'Nexus-user', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
            // Declare the Maven path using 'withMaven'
            withMaven(maven: 'Maven') {
                // Publish Maven artifacts to Nexus hosted repository
                sh "mvn deploy -Dmaven.repo.url=http://172.18.0.4:8081/repository/NexusRepo/ -Dmaven.username=${NEXUS_USERNAME} -Dmaven.password=${NEXUS_PASSWORD}"
            }
        }
    }
}

}
}
