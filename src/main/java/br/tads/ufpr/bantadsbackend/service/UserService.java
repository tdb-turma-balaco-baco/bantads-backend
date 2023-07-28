package br.tads.ufpr.bantadsbackend.service;

import br.tads.ufpr.bantadsbackend.domain.model.User;
import br.tads.ufpr.bantadsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public User register(User user) {
        log.info("Salvando o usuário = " + user);
        return repository.save(user);
    }

    public User findUserByEmail(String email) {
        return repository
                .findUserByEmail(email)
                .stream().findFirst()
                .orElseThrow();
    }
}
