package br.com.fiap.mercadoexpress.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain publico(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig.requestMatchers("/", "/api", "/home",
                            "/cadastro", "/login", "/logout",
                            "/css/**", "/js/**", "/acesso-negado", "/error").permitAll();

                    authorizeConfig.requestMatchers(HttpMethod.GET, "/produtos", "/produtos/{id:[0-9]+}",
                            "/mercado", "/mercado/{id:[0-9]+}").permitAll();

                    authorizeConfig.requestMatchers("/produtos/{id}/comprar").authenticated();

                    authorizeConfig.requestMatchers("/produtos/**").hasRole("ADMIN");
                    authorizeConfig.requestMatchers("/mercado/**").hasRole("ADMIN");
                    authorizeConfig.requestMatchers("/usuarios/**").hasRole("ADMIN");
                    authorizeConfig.requestMatchers("/admin/**").hasRole("ADMIN");

                    authorizeConfig.anyRequest().authenticated();
                })
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(loginSuccessHandler))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}