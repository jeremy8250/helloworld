stage('pull source code'){
    node('master'){
        git([url: 'https://github.com/jeremy8250/helloworld.git', branch: 'master'])
    }
}

stage('maven compile & package'){
    node('master'){
        sh ". ~/.bash_profile"

		def jdkHome = tool 'jdk_1.8.0'
		def mvnHome = tool 'maven-3.6.3'
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
		}catch(exec){
	    	echo 'mytomcat container is not running!'
        }

		try{
	    	sh 'docker rm mytomcat'
        }catch(exec){
	    	echo 'mytomcat container does not exist!'
	}

		try{
	    	sh 'docker rmi mytomcat'
		}catch(exec){
	    	echo 'mytomcat images does not exist!'
		}
    }
}

stage('start docker container'){
    node('master')
	try{
	    sh 'docker run --name mytomcat od -p 8089:8080 tomcat'
	}catch(exec){
	    echo 'Start docker images failed, please check the environment!'
	}
}
