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
		}catch(exec){
	    	echo 'mytomcat container is not running!'
        }

		try{
	    	sh 'docker rm mytomcat'
        }catch(exec){
	    	echo 'mytomcat container does not exist!'
	}

    }
}

stage('start docker container') {
	node('master') {
		try {
			sh 'docker run --name mytomcat -d -p 8089:8080 tomcat:9.0.34'
		} catch (exec) {
			echo 'Start docker images failed, please check the environment!'
		}
	}
}
