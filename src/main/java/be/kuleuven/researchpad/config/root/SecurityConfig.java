package be.kuleuven.researchpad.config.root;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:springSecurity.xml")
public class SecurityConfig {

}
