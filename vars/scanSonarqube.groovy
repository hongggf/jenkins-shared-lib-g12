// for scaning code with sonarqube 
// For Scaning Reactjs, Nextjs project with sonarqube 
def call(String projectName,String projectVersion, String projectKey,String appDirectory){
    dir(appDirectory){
withSonarQubeEnv(credentialsId: 'SONARQUBE-TOKEN', installationName: 'sonar-scanner') {
        def scannerHome = tool 'sonar-scanner'
        sh """

        ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName="${projectName}" \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.projectVersion=${projectVersion} \
            -Dsonar.sources=src

        """
    }     
}
}