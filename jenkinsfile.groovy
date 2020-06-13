stage('pull source code'){
    node('master'){
        git([url: 'https://github.com/jeremy8250/helloworld.git', branch: 'master'])
    }
}

stage('maven compile & package'){
    node('master'){
        sh ". ~/.bash_profile"

		def jdkHome = tool 'jdk8'
		def mvnHome = tool 'maven'
		env.PATH = "${jdkHome}/bin:${env.PATH}"
		env.PATH = "${mvnHome}/bin:${env.PATH}"
		sh "mvn clean package"
		sh "mv target/helloworld.war target/ROOT.war"
    }
}

stage('clean docker environment'){
    node('master'){
        try{
    	    sh 'docker stop mytomcat'
	}catch(exc){
	    echo 'mytomcat container is not running!'
        }

	try{
	    sh 'docker rm mytomcat'
        }catch(exc){
	    echo 'mytomcat container does not exist!'
	}

        try{
            sh 'docker rmi tomcat:latest'
        }catch(exc){
            echo 'tomcat image does not exist!'
        }

    }
}

stage('make docker imager'){
    node('master'){
        try{
	    sh 'docker build -t mytomcat .'
	}catch(exc){
      	echo 'make mytomcat image failed, please check the environment!'
        }
    }
}

stage('start docker container') {
	node('master') {
		try {
			sh 'docker run --name mytomcat -d -p 8089:8080 tomcat:latest'
		} catch (exec) {
			echo 'Start docker images failed, please check the environment!'
		}
	}
}
