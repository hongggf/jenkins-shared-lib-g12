@Library ('shared-lib@master') _

pipeline{
    agent any
    environment{
        TAG="v1.0.${env.BUILD_NUMBER}"
        IMG_NAME="jenkins-g12-reactjs"
        DH_USER="hong16"
        FULL_IMG="${DH_USER}/${IMG_NAME}:${TAG}"
        CHAT_ID="1016156192"
        TOKEN="8953973990:AAHuIA3Ef5jDlTVDZO77hi0GLMm1EZgiXvI"
    }
    stages{
        stage('Checkout'){
            steps{

                git 'https://github.com/hongggf/jenkins-shared-lib-g12.git'

                }
                }

          stage("Scan with Sonarqube "){
            environment{
                scannerHome = tool 'sonar-scanner'
            }
            steps{
                script{
                    scanSonarqube("REACT","1.0.0","react")
                    sendTelegram("Scanning is done , you can check sonarqube now","${TOKEN}","${CHAT_ID}")
                }
                
            }

        }
            // wait to get result Passed or Failed when scan is done
        stage("Wait for QualityGate"){
            
            steps{
                script{
                    def qg = waitForQualityGate()
                    env.QG_STATUS=qg.status 

                    if(qg.status !='OK'){
                        echo "Quality Gate is Failed !! "
                        // send telegram to team to fix 
                        currentBuild.result = 'FAILURE'
                        error("Quality Gate Failed cannot proceced")
                        return // to stop the pipeline

                    }else{
                        echo "Quality Gate is Passed ! "
                        currentBuild.result='SUCCESS'
                    }
                }
            }
        }

            stage ('Build') {
                steps{
                    script{
                        dockerbuild("reactjs","${FULL_IMG}")
                    }
                }
            }
            stage ('Push') {
                steps{
                    script{
                        dockerPush("${FULL_IMG}")   
                    }
                }
            }

            stage('Deploy') {
                steps{
                    script{
                        dockerDeploy('reactjs-app', "${FULL_IMG}", 3000, 80)
                    }
                }
            }
         
     post {
        success {
            script {
                def message = """
                    ✅ *SONARQUBE QUALITY GATE PASSED*

                    📦 *Project:* `${env.JOB_NAME}`
                    🌿 *Branch:* `${env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'N/A'}`
                    🔢 *Build:* `#${env.BUILD_NUMBER}`

                    🔍 *Quality Gate:* `${env.QG_STATUS}`
                    🟢 *Pipeline Status:* `SUCCESS`

                    🔗 *SonarQube Dashboard:*
                    ${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_PROJECT_KEY}

                    🔗 *Jenkins Build:*
                    ${env.BUILD_URL}

                    🎉 Code quality checks passed. Pipeline can continue.
                    """

                    sendTelegram(
                                message,
                                "${TOKEN}",
                                "${CHAT_ID}"
                )
            }
        }

        failure {
            script {
                def message = """
                    ❌ *SONARQUBE QUALITY GATE FAILED*

                        📦 *Project:* `${env.JOB_NAME}`
                        🌿 *Branch:* `${env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'N/A'}`
                        🔢 *Build:* `#${env.BUILD_NUMBER}`

                        🔍 *Quality Gate:* `${env.QG_STATUS ?: 'UNKNOWN'}`
                        🔴 *Pipeline Status:* `FAILED`

                        🔗 *SonarQube Dashboard:*
                        ${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_PROJECT_KEY}

                        🔗 *Jenkins Build:*
                        ${env.BUILD_URL}

                        ⚠️ Please check the SonarQube issues before merging or deploying.
                        """

                        sendTelegram(
                                    message,
                                    "${TOKEN}",
                                    "${CHAT_ID}"
                                )
            }
        }

        always {
            echo "Quality Gate stage finished with status: ${env.QG_STATUS ?: 'UNKNOWN'}"
        }
    } 
    }

}