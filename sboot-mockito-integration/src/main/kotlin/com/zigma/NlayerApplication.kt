package com.zigma

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.zigma")
@EntityScan("com.zigma.vo")
public class NlayerApplication {

}

fun main(args: Array<String>) {
	runApplication<NlayerApplication>(*args)
}
