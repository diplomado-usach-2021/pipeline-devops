package helpers

def merge(String ramaOrigen, String ramaDestino){
	println "Este método realiza un merge ${ramaOrigen} y ${ramaDestino}"

	checkout(ramaOrigen)
	checkout(ramaDestino)

	sh """
		git merge ${ramaOrigen}
		git push origin ${ramaDestino}
	"""
}

def tag(String ramaOrigen, String ramaDestino){
	println "Este método realiza un tag en master de ${ramaOrigen}"

	if (ramaOrigen.contains('release-v')){
        sh "git pull --all --tags "
		checkout(ramaDestino)
		def tagValue = ramaOrigen.split('release-')[1]
		sh """
			git tag ${tagValue}
			git push origin ${tagValue}
		"""

	} else {
		error "La rama ${ramaOrigen} no cumple con nomenclatura definida para rama release (release-v(major)-(minor)-(patch))."
	}
}

def checkout(String rama){
	sh "git reset --hard HEAD; git checkout ${rama}; git pull origin ${rama}"
}

return this;