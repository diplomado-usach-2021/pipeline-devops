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
    }


    parameters {
        choice choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción', name: 'builtTool'
         string defaultValue: '', description: 'Agregar stage a ejecutar separados por coma', name: 'stage'
    }

    stages{
    
      stage("Pipeline"){
                steps{
                    script {

                            if (params.builtTool == "gradle") {
                                	//def ejecucion = load 'gradle.groovy'
	                                //ejecucion.call()
                                     gradle()
                            } else {
                                     maven()
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

return this;