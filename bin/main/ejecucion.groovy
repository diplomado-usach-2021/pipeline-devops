/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
    pipeline {
    agent any

    environment{
        STAGE = '';
        STAGEFALTANTES = '';
    }


    parameters {
        choice choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción', name: 'builtTool'
         string defaultValue: '', description: 'Agregar stage a ejecutar separados por coma', name: 'etapasPipeline'
    }

    stages{
    
      stage("Pipeline"){
                steps{
                    script {

                         //   def ci_or_cd = verifyBranchName();

                            def etapasPipeline = params.etapasPipeline;

                                println "etapasPipeline  + ${etapasPipeline}"

                                def listaEtapas = etapasPipeline.split(',')
                                println "listaEtapas  + ${listaEtapas}"

                                if (params.builtTool == "gradle") {

                                    if (listaEtapas.size()==1 && listaEtapas.contains("")){
                                                     gradle(listaEtapas,verifyBranchName())
                                           }else{
                                      
                                              def  listaEtapasCantidad = listaEtapas.size();
                                              println "listaEtapasCantidad : + ${listaEtapasCantidad}"  


                                                def etapasDefinidas = ["build","sonar","run","test","nexus"]
                                                    //def ejecucion = load 'gradle.groovy'
                                                    //ejecucion.call()
                                                def etapasNoExistente = "";
                                                def marca = false;
                                                for(etapa in listaEtapas){
                                                if (!etapasDefinidas.contains(etapa)){
                                                    marca = true;
                                                    if (etapasNoExistente == ""){
                                                            etapasNoExistente = etapa;
                                                    }else{
                                                            etapasNoExistente = etapasNoExistente + "," + etapa ;
                                                    }
                                            }
                                            }
                                            if (marca == false){
                                                gradle(listaEtapas,verifyBranchName())
                                            }else{
                                                 println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                 slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                  throw new Exception("${etapasNoExistente}")  
                                            }

                               
                                  }
                                } else {
                                    
                                     
                                         if (listaEtapas.size()==1 && listaEtapas.contains("")){
                                                      maven(listaEtapas,verifyBranchName())
                                           }else{
                                      
                                              def  listaEtapasCantidad = listaEtapas.size();
                                              println "listaEtapasCantidad : + ${listaEtapasCantidad}"  


                                                def etapasDefinidas =  ["compile","sonar","test","package","guardarJar","run","nexus"]
                                                    //def ejecucion = load 'gradle.groovy'
                                                    //ejecucion.call()
                                                def etapasNoExistente = "";
                                                def marca = false;
                                                for(etapa in listaEtapas){
                                                if (!etapasDefinidas.contains(etapa)){
                                                    marca = true;
                                                    if (etapasNoExistente == ""){
                                                            etapasNoExistente = etapa;
                                                    }else{
                                                            etapasNoExistente = etapasNoExistente + "," + etapa ;
                                                    }
                                            }
                                            }
                                            if (marca == false){
                                                 maven(listaEtapas,verifyBranchName())
                                            }else{
                                                 println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                 slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}] las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                throw new Exception("${etapasNoExistente}")  
                                            }

                               
                                  }

                                     
                                        //def ejecucion = load 'maven.groovy'
                                    // ejecucion.call()
                                }

                        

                           echo "pipeline"
                    }
                } 
            }


        }

      post {
		success {
			   slackSend (color: '#00FF00', message: "Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}] Ejecución exitosa")
		}
		
		failure {
            slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}] Ejecución fallida en stage : ${STAGE}")
			error "Ejecución fallida en stage ${STAGE}"
		}
	 }


  }  	


  

}

def verifyBranchName(){

    //def is_ci_or_cd = (env.GIT_BRANCH.contains('feature-')) ? 'CI' : 'CD'

    if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop-')){
        return 'CI'
    } else if (){
        return 'CD'
    }


}

return this;