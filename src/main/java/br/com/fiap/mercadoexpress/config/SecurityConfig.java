package br.com.fiap.mercadoexpress.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publico(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig.requestMatchers("/", "/api", "/home", "/home/publico",
                            "/cadastro", "/login", "/logout",
                            "/css/**").permitAll();

                    authorizeConfig.requestMatchers(HttpMethod.GET, "/produtos", "/produtos/{id}",
                            "/mercado", "/mercado/{id}").permitAll();

                    authorizeConfig.requestMatchers("/produtos/{id}/comprar").authenticated();

                    authorizeConfig.requestMatchers("/produtos/**").hasRole("ADMIN");
                    authorizeConfig.requestMatchers("/mercado/**").hasRole("ADMIN");
                    authorizeConfig.requestMatchers("/usuarios/**").hasRole("ADMIN");

                    authorizeConfig.anyRequest().authenticated();
                }).formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/home/privado", false))
                .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}