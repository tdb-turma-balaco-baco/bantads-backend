package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.internal.User;
import br.tads.ufpr.bantads.user.internal.UserMapper;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

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
        User user = repository
                .findUserByEmail(request.email().toLowerCase())
                .orElseThrow(RuntimeException::new);

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            log.debug("user login successful {}", user.getEmail());
            return UserMapper.toResponse.apply(user);
        }

        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void create(@Valid CreateUser request) {
        Optional<User> optional = repository.findUserByEmail(request.email());
        if (optional.isPresent()) {
            throw new DataIntegrityViolationException("email already in use");
        }

        String encodedPassword = passwordEncoder.encode(generateRandomPassword());

        var user = new User();
        user.setEmail(request.email().toLowerCase());
        user.setPassword(encodedPassword);

        log.debug("saving user to database {}", user.getEmail());
        repository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void update(@Valid UpdateUser request) {
        User user = repository.findById(request.userId()).orElseThrow(RuntimeException::new);

        if (Objects.nonNull(request.email())) {
            log.debug("updating email for user {}", request.userId());
            user.setEmail(request.email().toLowerCase());
        }

        if (Objects.nonNull(request.password())) {
            log.debug("updating password for user {}", request.userId());
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        log.debug("updating user on database {}", user);
        repository.save(user);
    }

    private static String generateRandomPassword() {
        List<CharacterRule> rules = List.of(
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(EnglishCharacterData.Alphabetical),
                new CharacterRule(EnglishCharacterData.Special));

        return new PasswordGenerator().generatePassword(16, rules);
    }
}
