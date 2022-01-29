/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(listaEtapas,pipelineType){
            figlet 'gradle'
            println "listaEtapas 2 groovy  + ${listaEtapas}"
           figlet pipelineType
        /*
         if (listaEtapas.contains("") ||  listaEtapas.contains("build")){ 
                println ("entro");
         }else{
                println ("no entro");
         }*/   

        if (pipelineType == 'CI'){
            figlet 'Integración Continua'

        }else if (pipelineType == 'CD'){
            figlet 'devilery Continua'
        }

    if (listaEtapas.contains("") ||  listaEtapas.contains("build")){ 
             stage("Build & unit test"){
                             STAGE = env.STAGE_NAME
                             println "Stage: ${env.STAGE_NAME}"
                             figlet "Stage: ${env.STAGE_NAME}"
                             sh " whoami; ls -ltr "
                             sh  "chmod +x gradlew "
                             sh "./gradlew clean build "
                             echo "${env.WORKSPACE}"
                             echo "${WORKSPACE}";                 
         
            }
    } 
            
    if (listaEtapas.contains("") ||  listaEtapas.contains("sonar")){ 
            stage("sonar"){
                         STAGE = env.STAGE_NAME
                          println "Stage: ${env.STAGE_NAME}"  
                          figlet "Stage: ${env.STAGE_NAME}"
                            def scannerHome = tool 'sonar-scanner';
                             withSonarQubeEnv('sonarqube-server') { 
                                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle-key  -Dsonar.sources=src -Dsonar.java.binaries=build "
         
  
              }
            }
    }

/*
           stage('Guardando WAR') {             
                              archiveArtifacts 'build/libs/*.jar'

            }
*/

    if (listaEtapas.contains("") ||  listaEtapas.contains("run")){ 
            stage("Run"){
                          STAGE = env.STAGE_NAME
                          figlet "Stage: ${env.STAGE_NAME}"
                          println "Stage: ${env.STAGE_NAME}"    
                          sh " nohup bash gradlew bootRun & "
                          sleep 20        
            }
    }
			
      if (listaEtapas.contains("") ||  listaEtapas.contains("test")){    
             stage("Testing Application"){
                             STAGE = env.STAGE_NAME
                             figlet "Stage: ${env.STAGE_NAME}"
                             println "Stage: ${env.STAGE_NAME}"
                             sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"                         
             }
          }

           
      if (listaEtapas.contains("") ||  listaEtapas.contains("nexus")){    
            stage('nexus') {
                figlet "Stage: ${env.STAGE_NAME}"
                STAGE = env.STAGE_NAME
                nexusPublisher nexusInstanceId: 'nexus_test',
                nexusRepositoryId: 'test-nexus',
                packages: [
                    [
                        $class: 'MavenPackage',
                        mavenAssetList: [
                            [classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/libs/DevOpsUsach2020-0.0.1.jar"]
                        ],
                        mavenCoordinate: [
                            artifactId: 'DevOpsUsach2020',
                            groupId: 'com.devopsusach2020',
                            packaging: 'jar',
                            version: '0.0.3'
                        ]
                    ]
                ]
        }
      }
            
		
   
  	

}

return this;