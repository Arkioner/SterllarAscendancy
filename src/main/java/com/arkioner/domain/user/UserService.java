package com.arkioner.domain.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Mono<User> register(String username, String password, String avatarUrl) {
        return repository.findByUsername(username)
                .flatMap(existing -> Mono.<User>error(new IllegalStateException("username taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    String hash = pwEncoder.encode(password);
                    User u = new User(UUID.randomUUID(), username, hash, avatarUrl, List.of("USER"));
                    return repository.save(u);
                }));
    }

    public Mono<User> authenticate(String username, String password) {
        return repository.findByUsername(username)
                .flatMap(u -> {
                    boolean ok = pwEncoder.matches(password, u.passwordHash());
                    return ok ? Mono.just(u) : Mono.empty();
                });
    }

    public Mono<User> findById(UUID id){
        return repository.findById(id);
    }
}
