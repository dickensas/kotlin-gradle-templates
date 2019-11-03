package sboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.boot.web.server.MimeMappings
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

import java.nio.file.Paths

@SpringBootApplication
@Configuration
@EnableWebMvc
class App : WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    override fun customize(factory: ConfigurableServletWebServerFactory) {
        var mappings: MimeMappings = MimeMappings(MimeMappings.DEFAULT);
        mappings.add("wasm", "application/wasm;charset=utf-8");
        factory.setMimeMappings(mappings);
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/**")
            //change "sboot-wasm" below to the project folder name
            .addResourceLocations("file:../sboot-wasm/")
            .setCachePeriod(3600)
            .resourceChain(true)
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
