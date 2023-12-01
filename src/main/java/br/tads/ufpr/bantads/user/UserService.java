package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.inbound.UpdateUser;
import br.tads.ufpr.bantads.user.inbound.UserLogin;
import br.tads.ufpr.bantads.user.internal.User;
import br.tads.ufpr.bantads.user.internal.UserMapper;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import br.tads.ufpr.bantads.user.outbound.UserResponse;
import br.tads.ufpr.bantads.user.outbound.event.UserCreated;
import br.tads.ufpr.bantads.user.outbound.event.UserUpdated;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Positive;
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
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final Validator validator;

    public List<UserResponse> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper.toResponse)
                .toList();
    }

    public UserResponse findUserById(@Valid @Positive Long userId) {
        User user = repository.findById(userId).orElseThrow(RuntimeException::new);
        return UserMapper.toResponse.apply(user);
    }

    public UserResponse userLogin(@Valid UserLogin request) {
        User user = repository.findUserByEmail(request.email().toLowerCase()).orElseThrow(RuntimeException::new);

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            log.debug("user login successful {}", user.getEmail());
            return UserMapper.toResponse.apply(user);
        }

        return null;
    }

    @Transactional
    public UserCreated create(@Valid CreateUser request) {
        User user = UserMapper.createUserToEntity.apply(request);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        log.debug("saving user to database {}", user.getEmail());
        user = repository.save(user);

        var event = new UserCreated(user.getId());

        log.info("user created, publishing event {}", event);
        publisher.publishEvent(event);

        return event;
    }

    @Transactional
    public UserResponse update(@Valid UpdateUser request) {
        User user = repository.findById(request.userId()).orElseThrow(RuntimeException::new);

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
            user.setEmail(request.email().toLowerCase());
        }

        if (Objects.nonNull(request.password())) {
            log.debug("updating password for user {}", request.userId());
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        log.debug("updating user on database {}", user);
        user = repository.saveAndFlush(user);

        var event = new UserUpdated(user.getId());
        log.info("user updated, publishing event {}", event);
        publisher.publishEvent(event);

        return UserMapper.toResponse.apply(user);
    }
}
