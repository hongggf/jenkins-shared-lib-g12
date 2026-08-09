// for scaning code with sonarqube 
// For Scaning Reactjs, Nextjs project with sonarqube 
def call(String projectName,String projectVersion, String projectKey){
withSonarQubeEnv(credentialsId: 'SONARQUBE-TOKEN', installationName: 'sonar-scanner') {
    script{
    
        sh """

        ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName="${projectName}" \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.projectVersion=${projectVersion}

        """
    }     
}
}