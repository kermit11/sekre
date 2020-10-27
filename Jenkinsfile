node
{
    checkout scm

    stage('Build')
    {
        bat "mvn -DskipTests clean package"
    }

    stage('Build and push Docker image')
    {
        docker.withRegistry('https://registry.hub.docker.com', 'dockerHub')
        {
            def dockerImg = docker.build("kermit11/sekre")
            dockerImg.push()

        }
    }
}
