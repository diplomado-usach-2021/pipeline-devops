/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(listaEtapas,pipelineType){
            figlet 'gradle'
            println "listaEtapas 2 groovy  + ${listaEtapas}"
           figlet pipelineType
  


        if (pipelineType == 'CD'){
                        figlet 'Integración Continua'

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

                    
                if (listaEtapas.contains("") ||  listaEtapas.contains("nexusCI")){    
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
                        

        }else if (pipelineType == 'CI'){

            figlet 'devilery Continua'
                stage("downloadNexus"){
                        figlet "Stage: ${env.STAGE_NAME}"
                       sh    "curl -X GET -u admin:victor25 http://192.168.0.15:8083/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"            
                    	                                    
                    
                    }
                    
            stage('Guardando WAR') { 
                     figlet "Stage: ${env.STAGE_NAME}"                    
                      archiveArtifacts '*.jar'               
                }
            }

           stage("runDownloadedJar"){
                    figlet "Stage: ${env.STAGE_NAME}"
                    sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
                    sleep 20
            }
			
          stage("rest"){
                  figlet "Stage: ${env.STAGE_NAME}"
                  sh  " curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing' "
            }  

        stage("nexusCD"){        
            figlet "Stage: ${env.STAGE_NAME}"      
             sh 'echo ${WORKSPACE}'
               nexusPublisher nexusInstanceId: 'nexus_test', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/libs/DevOpsUsach2020-1.0.0.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
         

            
        }

 
            
  

/*
           stage('Guardando WAR') {             
                              archiveArtifacts 'build/libs/*.jar'

            }
*/

		
   
  	

}

return this;