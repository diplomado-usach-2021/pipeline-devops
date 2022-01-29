/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(listaEtapas,pipelineType){
    figlet 'maven'
    figlet pipelineType

        if (pipelineType == 'CI'){
             figlet 'Integración Continua'

                    if (listaEtapas.contains("") ||  listaEtapas.contains("compile")){ 
                        stage("Compile Code"){
                            STAGE = env.STAGE_NAME
                            figlet "Stage: ${env.STAGE_NAME}"
                            sh  "chmod +x mvnw "
                            sh " ./mvnw clean compile -e"
                
                        }
                    }
                                         
            
                    if (listaEtapas.contains("") ||  listaEtapas.contains("test")){ 
                            
                            stage("Test Code"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                                    STAGE = env.STAGE_NAME
                                    sh  " ./mvnw clean test -e "
                            }
                    }  


                    if (listaEtapas.contains("") ||  listaEtapas.contains("package")){ 
                        stage("Jar Code package "){
                               figlet "Stage: ${env.STAGE_NAME}"
                                STAGE = env.STAGE_NAME
                                sh  " ./mvnw clean package -e "
                        }
                    }

                    if (listaEtapas.contains("") ||  listaEtapas.contains("sonar")){ 
                        stage('sonar') {
                            STAGE = env.STAGE_NAME
                            figlet "Stage: ${env.STAGE_NAME}"

                            def scannerHome = tool 'sonar-scanner';
                            withSonarQubeEnv('sonarqube-server') { 
                                            sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven-key  -Dsonar.sources=src -Dsonar.java.binaries=build "
                                    }
                
                         }
                    }
            
            
                    if (listaEtapas.contains("") ||  listaEtapas.contains("run")){
                        stage("runJar"){
                            figlet "Stage: ${env.STAGE_NAME}"
                            STAGE = env.STAGE_NAME
                            sh  "nohup bash mvnw spring-boot:run &"
                            sleep 20
                        }
                    }
                        
                    if (listaEtapas.contains("") ||  listaEtapas.contains("testJar")){
                   
                    stage("rest"){
                            figlet "Stage: ${env.STAGE_NAME}"   
                            STAGE = env.STAGE_NAME
                            sh  " curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing' "
                        }
                    }

                if (listaEtapas.contains("") ||  listaEtapas.contains("nexus")){ 
                        stage('nexusCI') {
                                figlet "Stage: ${env.STAGE_NAME}"
                                STAGE = env.STAGE_NAME
                                nexusPublisher nexusInstanceId: 'nexus_test',
                                nexusRepositoryId: 'test-nexus',
                                packages: [
                                    [
                                        $class: 'MavenPackage',
                                        mavenAssetList: [
                                            [classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]
                                        ],
                                        mavenCoordinate: [
                                            artifactId: 'DevOpsUsach2020',
                                            groupId: 'com.devopsusach2020',
                                            packaging: 'jar',
                                            version: '0.0.1'
                                        ]
                                    ]
                                ]
                        }
                }
    
        }else if (pipelineType == 'CD'){

            figlet 'devilery Continua'
                stage("downloadNexus"){
                        figlet "Stage: ${env.STAGE_NAME}"
                       sh    "curl -X GET -u admin:victor25 http://192.168.0.15:8083/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"            
                    	                                    
                    
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
             //  nexusPublisher nexusInstanceId: 'nexus_test', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/libs/DevOpsUsach2020-1.0.0.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
                STAGE = env.STAGE_NAME
                    nexusPublisher nexusInstanceId: 'nexus_test',
                    nexusRepositoryId: 'test-nexus',
                    packages: [
                        [
                            $class: 'MavenPackage',
                            mavenAssetList: [
                                [classifier: '', extension: '', filePath: "${env.WORKSPACE}/DevOpsUsach2020-0.0.1.jar"]
                            ],
                            mavenCoordinate: [
                                artifactId: 'DevOpsUsach2020',
                                groupId: 'com.devopsusach2020',
                                packaging: 'jar',
                                version: '1.0.0'
                            ]
                        ]
                    ]

            
             }
        }
 
            
		

}

return this;