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
        git commit -m "merge de  ${ramaOrigen}"
        git push origin ${ramaDestino}


    """
   
}

def tag(String ramaOrigen){
    println "este método realiza un tag ${ramaOrigen}"
    /*
        -validar que la rama release cumpla con un patron o una nomenclatura definida release-v(major)-(minor)-(patch)
        -obtener el valor desde la "v" en adelanre, ejemplo release--V1-0-0 ... obtener el tag 1-0-0 
    */
}

def checkout(String rama){
      sh "git reset --hard head; git checkout origin  ${rama}; git pull origin ${rama} "
}

return this;