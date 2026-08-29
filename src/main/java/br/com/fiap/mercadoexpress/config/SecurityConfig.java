package br.com.fiap.mercadoexpress.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                authorizeConfig.requestMatchers("/home/publico").permitAll();

                authorizeConfig.requestMatchers("/logout").permitAll();

                authorizeConfig.anyRequest().authenticated();
            }).formLogin(form -> form.defaultSuccessUrl("/home",
                    true)).build();

        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

    }
