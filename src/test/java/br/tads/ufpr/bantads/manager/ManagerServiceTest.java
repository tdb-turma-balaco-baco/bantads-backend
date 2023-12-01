package br.tads.ufpr.bantads.manager;

import br.tads.ufpr.bantads.manager.inbound.CreateManager;
import br.tads.ufpr.bantads.manager.internal.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}