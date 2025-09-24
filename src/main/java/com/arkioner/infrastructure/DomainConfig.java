package com.arkioner.infrastructure;

import com.arkioner.domain.loginuser.LoginUserRepository;
import com.arkioner.domain.user.ProfileUserRepository;
import com.arkioner.domain.loginuser.LoginUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public LoginUserService userService(LoginUserRepository loginUserRepository){
        return new LoginUserService(loginUserRepository);
    }
}
