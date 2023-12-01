package br.tads.ufpr.bantads.manager;

import br.tads.ufpr.bantads.manager.internal.Manager;
import br.tads.ufpr.bantads.manager.internal.ManagerRepository;
import br.tads.ufpr.bantads.user.internal.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ApplicationModuleTest
class ManagerServiceTest {

    @Autowired
    private ManagerRepository repository;
    @Autowired
    private ManagerService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should create a manager")
    void createManager() {
        var createManager = new CreateManager("manager", "mock", "manager@mock.com", "12312312311", "12312312311");
        service.create(createManager);

        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("should retrieve created manager")
    void findManagerById() {
        Manager entity = new Manager();
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        entity.setEmail("email@email.com");
        entity.setCpf("12312312311");
        entity.setPhone("12312312311");

        Manager saved = repository.save(entity);
        ManagerResponse response = service.findManagerById(saved.getId());

        assertNotNull(response);
        assertEquals(response.email(), saved.getEmail());
    }
}