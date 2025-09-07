package com.arkioner.infrastructure;

import com.arkioner.domain.user.UserProfileRepository;
import com.arkioner.domain.user.UserProfileService;
import com.arkioner.domain.user.UserRepository;
import com.arkioner.domain.user.UserService;
import com.arkioner.infrastructure.repository.R2dbcUserProfileRepository;
import com.arkioner.infrastructure.repository.R2dbcUserRepository;
import com.arkioner.infrastructure.repository.UserProfileReactiveCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DomainConfig {

    @Bean
    public UserService userService(UserRepository userRepository){
        return new UserService(userRepository);
    }

    @Bean
    public UserProfileService userProfileService(UserProfileRepository userProfileRepository){
        return new UserProfileService(userProfileRepository);
    }
}
