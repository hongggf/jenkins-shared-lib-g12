// for scaning code with sonarqube 
// For Scaning Reactjs, Nextjs project with sonarqube 
def call(String projectName,String projectVersion, String projectKey,String appDirectory){
withSonarQubeEnv(credentialsId: 'SONARQUBE-TOKEN', installationName: 'sonar-scanner') {
    dir(appDirectory){
        
        sh """

        ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName="${projectName}" \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.projectVersion=${projectVersion}

        """
    }     
}
}