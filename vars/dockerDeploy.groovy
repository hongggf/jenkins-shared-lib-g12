def call (String containerName, String imageName, int hostPort, int containerPort){
   { sh """

        docker rm -f ${containerName} || true

        docker run -d \
            --name ${containerName} \
            -p ${hostPort}:${containerPort} \
            ${imageName}
        """
        echo " container deployed successfully with name: ${containerName} and image: ${imageName} on port: ${hostPort}"    

   }
}