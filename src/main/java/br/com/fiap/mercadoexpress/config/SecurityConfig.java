package br.com.fiap.mercadoexpress.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain publico(HttpSecurity http) throws Exception {
            return http.authorizeHttpRequests(authorizeConfig -> {
                authorizeConfig.requestMatchers("/api", "/home", "/home/publico", "/cadastro").permitAll();

                authorizeConfig.requestMatchers("/logout").permitAll();

                authorizeConfig.requestMatchers("/login").permitAll();

                authorizeConfig.anyRequest().authenticated();
            }).formLogin(form -> form.loginPage("/home")
                    .defaultSuccessUrl("/home/publico",
                    true)).build();

        }


        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

    }



