package com.ss.haat

import com.ss.haat.Service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
class HaatApplication{

	@Autowired
	lateinit var fileStorage: FileStorage

//	@Bean
//	fun run() = CommandLineRunner {
//		fileStorage.deleteAll()
//		fileStorage.init()
//	}
}

fun main(args: Array<String>) {
	runApplication<HaatApplication>(*args)
}

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurerAdapter() {

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**")
	}
}