package io.jzheaux.springsecurity.resolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@SpringBootApplication //(exclude = SecurityAutoConfiguration.class)
public class ResolutionsApplication {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User
                        .withUsername("user")
                        .password("$2a$04$6Cuo9RIa1tOGrD.WsgCxGuCLoOfqChCg0dq0JVYh3R1hMgRWjgTnu")
                        .authorities("resolution:read")
                        .build());
    }

    public static void main(String[] args) {
        SpringApplication.run(ResolutionsApplication.class, args);
    }

}
