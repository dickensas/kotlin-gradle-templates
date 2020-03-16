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
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker

import java.nio.file.Paths

@SpringBootApplication
@Configuration
@EnableWebSocketMessageBroker
class App : WebSocketMessageBrokerConfigurer {
   override fun configureMessageBroker(config:MessageBrokerRegistry) {
      config.enableSimpleBroker("/topic")
      config.setApplicationDestinationPrefixes("/app")
   }
   
   override fun registerStompEndpoints(registry:StompEndpointRegistry) {
      registry.addEndpoint("/websocket").withSockJS()
   }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
