package eu.filip.loginredis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.filip.loginredis.filter.AuthFailureHandler;
import eu.filip.loginredis.filter.AuthSuccessHandler;
import eu.filip.loginredis.filter.JsonAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    ObjectMapper objectMapper;
    AuthFailureHandler authFailureHandler;
    AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(ObjectMapper objectMapper, AuthFailureHandler authFailureHandler, AuthSuccessHandler authSuccessHandler) {
        this.objectMapper = objectMapper;
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("user1")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http
                .authorizeRequests()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("x").and()
                .addFilter(authenticationFilter())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JsonAuthenticationFilter authenticationFilter() throws Exception{
        JsonAuthenticationFilter authenticationFilter = new JsonAuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authFailureHandler);
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        return authenticationFilter;
    }
}
