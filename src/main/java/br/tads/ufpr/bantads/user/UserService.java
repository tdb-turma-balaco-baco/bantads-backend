package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.inbound.UpdateUser;
import br.tads.ufpr.bantads.user.internal.User;
import br.tads.ufpr.bantads.user.internal.UserMapper;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import br.tads.ufpr.bantads.user.outbound.UserResponse;
import br.tads.ufpr.bantads.user.outbound.event.UserCreated;
import br.tads.ufpr.bantads.user.outbound.event.UserUpdated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public List<UserResponse> findAllUsers() {
        return this.repository.findAll()
                .stream()
                .map(UserMapper.toResponse)
                .toList();
    }

    @Transactional
    public UserCreated create(@Valid CreateUser request) {
        User user = UserMapper.createUserToEntity.apply(request);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        log.debug("saving user to database {}", user.getEmail());
        user = this.repository.save(user);

        var event = new UserCreated(user.getId());

        log.info("user created, publishing event {}", event);
        eventPublisher.publishEvent(event);

        return event;
    }

    @Transactional
    public UserResponse update(@Valid UpdateUser request) {
        User user = this.repository.findById(request.userId()).orElseThrow(RuntimeException::new);

        if (Objects.nonNull(request.firstName())) {
            log.debug("updating firstName for user {}", request.userId());
            user.setFirstName(request.firstName());
        }

        if (Objects.nonNull(request.lastName())) {
            log.debug("updating lastName for user {}", request.userId());
            user.setLastName(request.lastName());
        }

        if (Objects.nonNull(request.email())) {
            log.debug("updating email for user {}", request.userId());
            user.setEmail(request.email());
        }

        if (Objects.nonNull(request.password())) {
            log.debug("updating password for user {}", request.userId());
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        log.debug("updating user on database {}", user);
        user = repository.saveAndFlush(user);

        var event = new UserUpdated(user.getId());
        log.info("user updated, publishing event {}", event);
        eventPublisher.publishEvent(event);

        return UserMapper.toResponse.apply(user);
    }
}
