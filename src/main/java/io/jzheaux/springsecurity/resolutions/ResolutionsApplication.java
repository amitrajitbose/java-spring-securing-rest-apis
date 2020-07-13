package io.jzheaux.springsecurity.resolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;


@SpringBootApplication //(exclude = SecurityAutoConfiguration.class)
public class ResolutionsApplication extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .mvcMatchers(HttpMethod.GET, "/resolutions", "/resolution/**").hasAuthority("resolution:read")
                        .anyRequest().hasAuthority("resolution:write"))
                .httpBasic(basic -> {
                });
    }

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    public static void main(String[] args) {
        SpringApplication.run(ResolutionsApplication.class, args);
    }

}
