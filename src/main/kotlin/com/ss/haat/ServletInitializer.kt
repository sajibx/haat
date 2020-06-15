package com.ss.haat

import org.springframework.boot.Banner
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer


class ServletInitializer : SpringBootServletInitializer() {

	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(HaatApplication::class.java)
	}


	fun main(args: Array<String>) {
		configureApplication(SpringApplicationBuilder()).run(*args)
	}

	private fun configureApplication(builder: SpringApplicationBuilder): SpringApplicationBuilder {
		return builder.sources(HaatApplication::class.java).bannerMode(Banner.Mode.OFF)
	}

}
