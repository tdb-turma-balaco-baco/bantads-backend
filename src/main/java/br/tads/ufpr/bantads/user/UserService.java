package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.internal.UserMapper;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import br.tads.ufpr.bantads.user.outbound.UserResponse;
import br.tads.ufpr.bantads.user.outbound.event.UserCreated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        var user = UserMapper.createUserToEntity.apply(request);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user = this.repository.save(user);

        UserCreated event = new UserCreated(user.getId());
        eventPublisher.publishEvent(event);

        return event;
    }

    @Transactional
    public UserResponse update(@Positive Long userId) {
        var user = this.repository.findById(userId).orElseThrow(RuntimeException::new);
        UserResponse response = UserMapper.toResponse.apply(user);

        this.repository.save(user);
        return response;
    }
}
