package com.example.bookshelf;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}u123").roles("USER")
            .and()
            .withUser("admin").password("{noop}a123").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET,    "/**").hasRole("USER")
            .antMatchers(HttpMethod.POST,   "/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT,    "/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN");

        // avoid getting a response when the password is incorrect
        http.sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS);
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // allow H2 console (having its own authentication)
        web.ignoring().antMatchers("/h2-console/**");
    }
}
