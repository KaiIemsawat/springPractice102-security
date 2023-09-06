package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

/*
    Since username and password are declared here,
    Spring Boot will not use the username and password from application.properties
*/
//    @Bean
////    Spring Security’s InMemoryUserDetailsManager implements UserDetailsService to provide support
////    for username/password based authentication that is stored in memory
//    public InMemoryUserDetailsManager userDetailsManager() {
//
////        UserDetails is returned by the UserDetailsService.
////        The DaoAuthenticationProvider validates the UserDetails and then returns an Authentication
////        that has a principal that is the UserDetails returned by the configured UserDetailsService.
//        UserDetails john = User.builder()
//                .username("john").password("{noop}test123") // {noop} <- plain text
//                .roles("EMPLOYEE")
//                .build();
//
//        UserDetails mary = User.builder()
//                .username("mary").password("{noop}test123")
//                .roles("EMPLOYEE", "MANAGER")
//                .build();
//
//        UserDetails susan = User.builder()
//                .username("susan").password("{noop}test123")
//                .roles("EMPLOYEE", "MANAGER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(john, mary, susan);
//    }

//    The code above is replaced bt the next block of code


//    Add support for JDBC. No more hard code
//    Tell Spring Security to use JDBC authentication with data source
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

//        define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT user_id, pw, active FROM members WHERE user_id=?"
        );

//        define query to retrieve the authorities/role by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT user_id, role FROM roles WHERE user_id=?"
        );

        return jdbcUserDetailsManager;
    }


/*
    Restrict accessing based on roles
*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
        );

//      Two asterisks, **, works like * but crosses directory boundaries.
//      This syntax is generally used for matching complete paths.
//      /home/*/*       Matches /home/gus/data on UNIX platforms
//      /home/**        Matches /home/gus and /home/gus/data on UNIX platforms

//        use HTTP Basic authentication
        httpSecurity.httpBasic(Customizer.withDefaults());

//        disable Cross Site Request Forgery (CSRF)
//        in general, not required for stateless REST PAIs that use POST, PUT, DELETE and/or PATCH
        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }


}
