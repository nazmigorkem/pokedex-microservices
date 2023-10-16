package obss.pokedex.user.config;

import feign.codec.ErrorDecoder;
import obss.pokedex.user.exception.RetrieveMessageErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetrieveMessageErrorDecoder();
    }
}
