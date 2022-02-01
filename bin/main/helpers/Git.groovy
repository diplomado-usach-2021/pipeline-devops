package helpers

def merge(String ramaOrigen, String ramaDestino){
   println "Este método realiza un merge ${ramaOrigen} y ${ramaDestino} "

    // checkout de la ranaOrigen
    checkout(ramaOrigen)
    // checkout de la rama ramaDestino
    checkout(ramaDestino)
    
    // merge ramaOrigen
    sh  """
        git merge ${ramaOrigen}
        git push origin ${ramaDestino}

    """
   
}

def tag(String ramaOrigen, String ramaDestino){
    println "este método realiza un tag ${ramaOrigen}"

    if (ramaOrigen.contains('release-v')){
        // cortar la variable y dejar solamente v1-0-0
        checkout(ramaDestino)
        def tagValue = ramaOrigen.split('release-v')[1] // {['','1-0-0']}

        tagValue.each{println it}

        sh """
        git tag ${tagValue}
        git push origin ${tagValue}

        """

    }else{
        error "la rama ${ramaOrigen} no cumple con nomenclatura definida param rama release-v(major)-(minor)-(patch) "
    }
    /*
        -validar que la rama release cumpla con un patron o una nomenclatura definida release-v(major)-(minor)-(patch)
        -obtener el valor desde la "v" en adelanre, ejemplo release--V1-0-0 ... obtener el tag 1-0-0 
    */
}

def checkout(String rama){
      sh "git reset --hard HEAD; git checkout ${rama}; git pull origin ${rama} "
}

return this;