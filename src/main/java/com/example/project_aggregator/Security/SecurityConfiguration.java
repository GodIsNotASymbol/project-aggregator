package com.example.project_aggregator.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {


    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        return http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/").permitAll();
                            authorizeHttp.requestMatchers("/mainPage").permitAll();
                            authorizeHttp.requestMatchers("/viewItem").permitAll();
                            authorizeHttp.requestMatchers("/styles.css").permitAll();
                            authorizeHttp.requestMatchers("/butterflies_background.jpg").permitAll();
                            /*authorizeHttp.requestMatchers("/api/auth/register").permitAll();
                            authorizeHttp.requestMatchers("/api/auth/loginUser").permitAll();
                            authorizeHttp.requestMatchers("/loginPage").permitAll();*/
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .formLogin(l -> l.defaultSuccessUrl("/createAndEditPage?page=1"))
                .csrf().disable()
                .logout(l -> l.logoutSuccessUrl("/"))
                .build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
