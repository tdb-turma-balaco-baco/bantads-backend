package br.tads.ufpr.bantads.manager;

import br.tads.ufpr.bantads.manager.internal.Manager;
import br.tads.ufpr.bantads.manager.internal.ManagerRepository;
import br.tads.ufpr.bantads.user.CreateUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManagerService {
    private final ApplicationEventPublisher publisher;
    private final ManagerRepository repository;

    public ManagerResponse findManagerById(@Valid @Positive Long id) {
        Manager manager = repository.findById(id).orElseThrow(RuntimeException::new);

        return new ManagerResponse(
                manager.getId(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getEmail(),
                manager.getCpf(),
                manager.getPhone()
        );
    }

    @Transactional
    public void create(@Valid CreateManager createManager) {
        Manager manager = new Manager();
        manager.setFirstName(createManager.firstName());
        manager.setLastName(createManager.lastName());
        manager.setEmail(createManager.email());
        manager.setCpf(createManager.cpf());
        manager.setPhone(createManager.phone());

        repository.save(manager);

        CreateUser event = new CreateUser(manager.getEmail());
        publisher.publishEvent(event);
    }
}
