package obss.pokedex.user.config;

import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(customizer -> //
                        customizer.anyRequest().permitAll())
                .exceptionHandling(customizer -> customizer.accessDeniedHandler( //
                                (req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN)) // if someone tries to access protected resource but doesn't have enough permissions
                        .authenticationEntryPoint((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED))) //
//                .sessionManagement(customizer -> customizer.invalidSessionStrategy((req, resp) -> resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED))) //
                .formLogin(customizer -> customizer.loginProcessingUrl("/login") //
                        .successHandler((req, resp, auth) -> resp.setStatus(HttpServletResponse.SC_OK)) // success authentication).and().formLogin()
                        .failureHandler((req, resp, ex) -> resp.setStatus(HttpServletResponse.SC_FORBIDDEN))) //  bad credentials
                .logout(customizer -> customizer.logoutUrl("/logout") //
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())) //
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}