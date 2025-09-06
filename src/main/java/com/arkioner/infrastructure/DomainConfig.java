package com.arkioner.infrastructure;

import com.arkioner.domain.user.UserRepository;
import com.arkioner.domain.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public UserService userService(UserRepository userRepository){
        return new UserService(userRepository);
    }
}
