package org.swarmbit

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("org.swarmbit")
		.start()
}

