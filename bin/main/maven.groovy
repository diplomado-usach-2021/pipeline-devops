/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(listaEtapas){
  

  println "listaEtapas 2 groovy  + ${listaEtapas}"

        if (listaEtapas.contains("") ||  listaEtapas.contains("compile")){ 
             stage("Compile Code"){
                 STAGE = env.STAGE_NAME
                  sh  "chmod +x mvnw "
                  sh " ./mvnw clean compile -e"
    
            }
        }
            
        if (listaEtapas.contains("") ||  listaEtapas.contains("sonar")){ 
            stage('SonarQube analysis') {
                STAGE = env.STAGE_NAME
                def scannerHome = tool 'sonar-scanner';
                withSonarQubeEnv('sonarqube-server') { 
                                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-maven-key  -Dsonar.sources=src -Dsonar.java.binaries=build "
                         }
    
           }
        }
  
         
   
          if (listaEtapas.contains("") ||  listaEtapas.contains("test")){ 
                  stage("Test Code"){
                         STAGE = env.STAGE_NAME
                        sh  " ./mvnw clean test -e "
                  }
          }  


        if (listaEtapas.contains("") ||  listaEtapas.contains("package")){ 
            stage("Jar Code package "){
                      STAGE = env.STAGE_NAME
                      sh  " ./mvnw clean package -e "
            }
        }


        if (listaEtapas.contains("") ||  listaEtapas.contains("guardarJar")){ 
             stage('Guardando WAR') {
                              STAGE = env.STAGE_NAME             
                              archiveArtifacts 'build/*.jar'
      
            }
        }

/*
             stage("Upload to Nexus"){

                     sh 'echo ${WORKSPACE}'
                    script {
                       nexusPublisher nexusInstanceId: 'nexus_test', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
                }
             
            }
*/

 
        if (listaEtapas.contains("") ||  listaEtapas.contains("run")){ 
            stage("Run Jar"){
                              STAGE = env.STAGE_NAME
                               sh  "nohup bash mvnw spring-boot:run &"
                               sleep 20
            }
         }
			
        if (listaEtapas.contains("") ||  listaEtapas.contains("testJar")){ 
          stage("Testing Application"){
                              STAGE = env.STAGE_NAME
                               sh  " curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing' "
            }
        }

    if (listaEtapas.contains("") ||  listaEtapas.contains("nexus")){ 
            stage('nexus') {
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
                                version: '0.0.3'
                            ]
                        ]
                    ]
            }
    }
    
            
		

}

return this;