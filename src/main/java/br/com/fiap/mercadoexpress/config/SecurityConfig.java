package br.com.fiap.mercadoexpress.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

        public UserDetailsService usuariosCadastrados() {
            UserDetails usuario1 = User.builder().username("ganasevici")
                    .password("estudante230107").build();


        }


    }
