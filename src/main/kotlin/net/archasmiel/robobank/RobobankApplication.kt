package net.archasmiel.robobank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RobobankApplication

fun main(args: Array<String>) {
	runApplication<RobobankApplication>(*args)
}
