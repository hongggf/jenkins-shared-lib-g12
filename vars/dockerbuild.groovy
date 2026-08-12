def call(String appType, String imageName) {

    if (appType == 'reactjs') {

        echo "======================================"
        echo "Building ReactJS application"
        echo "======================================"

        // Get Dockerfile from Shared Library
        writeFile(
            file: 'reactjs.Dockerfile',
            text: libraryResource('docker/reactjs.Dockerfile')
        )

        // Show files so we can verify
        sh '''
            echo "Dockerfile created:"
            ls -l reactjs.Dockerfile

            echo "React application:"
            ls -la reactjs-frontend/
        '''

        // Build using reactjs-frontend as Docker context
        sh """
            docker build \
                -t ${imageName} \
                -f reactjs.Dockerfile \
                reactjs-frontend
        """

    } else if (appType == 'spring') {

        echo "======================================"
        echo "Building Spring application"
        echo "======================================"

        // Get Dockerfile from Shared Library
        writeFile(
            file: 'spring.Dockerfile',
            text: libraryResource('docker/spring.Dockerfile')
        )

        sh '''
            echo "Dockerfile created:"
            ls -l spring.Dockerfile

            echo "Spring application:"
            ls -la spring-api/
        '''

        // Build using spring-api as Docker context
        sh """
            docker build \
                -t ${imageName} \
                -f spring.Dockerfile \
                spring-api
        """

    } else {

        error("Unknown application type: ${appType}")
    }
}