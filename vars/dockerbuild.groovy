def call(String appType, String imageName) {

    echo "Application type: ${appType}"

    if (appType == 'reactjs') {

        writeFile(
            file: 'reactjs.Dockerfile',
            text: libraryResource(
                'docker/reactjs.Dockerfile'
            )
        )
        dir('reactjs-frontend'){
                sh """
                    docker build \
                        -t ${imageName} \
                        -f reactjs.Dockerfile .
                """
        }

    } else if (appType == 'spring') {

        writeFile(
            file: 'spring.Dockerfile',
            text: libraryResource(
                'docker/spring.Dockerfile'
            )
        )
        dir('spring-backend'){
            sh """
                docker build \
                    -t ${imageName} \
                    -f spring.Dockerfile .
            """
        }
      

    } else {

        error("Unsupported application type: ${appType}")
    }
}