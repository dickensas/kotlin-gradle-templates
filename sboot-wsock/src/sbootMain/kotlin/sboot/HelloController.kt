package sboot

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class HelloController {
   @MessageMapping("/hello")
   @SendTo("/topic/greetings")
   @Throws(Exception::class)
   fun greetings(message:HelloMessage):Greeting {
      Thread.sleep(1000);
      return Greeting("Hello, " + message.name + "!");
   }
}