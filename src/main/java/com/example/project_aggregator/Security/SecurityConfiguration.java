package com.example.project_aggregator.Security;

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
public class SecurityConfiguration  {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/").permitAll();
                            authorizeHttp.requestMatchers("/mainPage").permitAll();
                            authorizeHttp.requestMatchers("/viewItem").permitAll();
                            authorizeHttp.requestMatchers("/styles.css").permitAll();
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .formLogin(l -> l.defaultSuccessUrl("/createItem"))
                .logout(l -> l.logoutSuccessUrl("/"))
                .build();
    }

}

@Bean
public UserDetailsService users(){
    UserDetails admin = User.builder()
            .username("admin")
            .password("password")
            .roles(UserRole.ADMIN)
            .build();
    UserDetails user = User.builder()
            .username("user")
            .password("password")
            .roles(UserRole.USER)
            .build();
}
