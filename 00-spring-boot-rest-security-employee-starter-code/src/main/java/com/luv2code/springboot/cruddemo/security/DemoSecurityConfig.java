package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class DemoSecurityConfig {
/*
    Since username and password are declared here,
    Spring Boot will not use the username and password from application.properties
*/
    @Bean
//    Spring Security’s InMemoryUserDetailsManager implements UserDetailsService to provide support
//    for username/password based authentication that is stored in memory
    public InMemoryUserDetailsManager userDetailsManager() {

//        UserDetails is returned by the UserDetailsService.
//        The DaoAuthenticationProvider validates the UserDetails and then returns an Authentication
//        that has a principal that is the UserDetails returned by the configured UserDetailsService.
        UserDetails john = User.builder()
                .username("john").password("{noop}test123") // {noop} <- plain text
                .roles("EMPLOYEE")
                .build();

        UserDetails mary = User.builder()
                .username("mary").password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER")
                .build();

        UserDetails susan = User.builder()
                .username("susan").password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }
}
