package net.keithyw.game

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class GameApplication

fun main(args: Array<String>) {
	runApplication<GameApplication>(*args)
}
