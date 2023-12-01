package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.internal.User;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import br.tads.ufpr.bantads.user.utils.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
class UserServiceTest {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should find list of saved users")
    void findAllUsers() {
        for (int i = 0; i < 3; i++) {
            User entity = UserBuilder.builder().password(passwordEncoder.encode("password")).build();

            entity.setEmail("email" + i + "@email.com");
            repository.save(entity);
        }

        var allUsers = service.findAllUsers();
        assertEquals(3, allUsers.size());
    }

    @Test
    @DisplayName("find user by id should return user info if it exists")
    void itShouldFindUserById() {
        User saved = repository.save(UserBuilder.of());
        UserResponse response = service.findUserById(saved.getId());

        assertNotNull(response);
        assertEquals(response.userId(), saved.getId());
    }

    @Test
    @DisplayName("find user by id should not return user info if it not exists")
    void itShouldNotFindUserById() {
        assertThrows(RuntimeException.class, () -> {
            UserResponse response = service.findUserById(1L);
            assertNull(response);
        });
    }

    @Test
    @DisplayName("should successfully create a user")
    void itShouldCreate() {
        service.create(new CreateUser("email@email.com"));
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("should not create user with a existing email")
    public void itShouldNotCreateWithDuplicateEmail() {
        User saved = repository.save(UserBuilder.of());

        assertThrows(DataIntegrityViolationException.class, () -> {
            service.create(new CreateUser(saved.getEmail()));
        });

        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("should update all user values successfully")
    void update() {
        var created = repository.save(UserBuilder.of());
        UpdateUser update = new UpdateUser(created.getId(), "newEmail@email.com", "newPassword");

        service.update(update);
        User updated = repository.findById(created.getId()).orElseThrow(AssertionError::new);

        assertEquals(update.email().toLowerCase(), updated.getEmail());
    }

    @Test
    @DisplayName("should update some of the user values")
    void updatePartial() {
        repository.save(UserBuilder.of());

        List<Long> ids = repository.findAll().stream().map(User::getId).toList();

        UpdateUser request = new UpdateUser(ids.get(0), "newEmail@email.com", null);
        service.update(request);

        User entity = repository.findById(ids.get(0)).orElseThrow(AssertionError::new);
        assertEquals("newemail@email.com", entity.getEmail());
    }

    @Test
    @DisplayName("should return user response when login successful")
    public void login() {
        User saved = repository.save(UserBuilder.builder().password(passwordEncoder.encode("password")).build());
        UserResponse response = service.userLogin(new UserLogin(saved.getEmail(), "password"));

        assertNotNull(response);
        assertEquals(saved.getId(), response.userId());
    }

    @Test
    @DisplayName("should not return user response when login with incorrect info")
    public void givenInvalidLoginShouldNotFind() {
        User saved = repository.save(UserBuilder.builder().password(passwordEncoder.encode("password")).build());
        UserResponse response = service.userLogin(new UserLogin(saved.getEmail(), "invalid"));
        assertThrows(RuntimeException.class, () -> service.userLogin(new UserLogin("invalid@email.com", "")));
    }
}