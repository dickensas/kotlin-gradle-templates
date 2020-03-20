package sboot

import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {
   override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .resourceChain(true)
            
        registry
            .addResourceHandler("/**")
            //change "sboot-jython" below to the project folder name
            .addResourceLocations("file:../sboot-jython/")
            .setCachePeriod(3600)
            .resourceChain(true)
   }
}