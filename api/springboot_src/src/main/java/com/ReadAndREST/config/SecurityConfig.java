// package com.ReadAndREST.config;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http
//             .authorizeRequests(authorizeRequests ->
//                 authorizeRequests
//                     .antMatchers("/login", "/signup").permitAll() // Permit access to login and signup pages
//                     .anyRequest().authenticated() // Require authentication for all other requests
//             )
//             .formLogin(formLogin ->
//                 formLogin
//                     .loginPage("/login") // Custom login page URL
//                     .defaultSuccessUrl("/homepage") // Redirect after successful login
//                     .failureUrl("/login?error=true") // Redirect on login failure with error parameter
//                     .permitAll()
//             )
//             .logout(logout ->
//                 logout
//                     .logoutUrl("/logout")
//                     .logoutSuccessUrl("/login?logout")
//                     .permitAll()
//             );
//     }
// }




