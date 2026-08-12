def call (String containerName, String imageName, int hostPort, int containerPort){
   sh """

        docker rm -f ${containerName} 2>/dev/null || true
        docker pull ${imageName}
        docker run -d \
            --name ${containerName} \
            -p ${hostPort}:${containerPort} \
            ${imageName}
        docker ps
        """
        echo " container deployed successfully with name: ${containerName} and image: ${imageName} on port: ${hostPort}"    

   
}