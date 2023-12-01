package br.tads.ufpr.bantads.user;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.outbound.event.UserCreated;
import br.tads.ufpr.bantads.user.outbound.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(this.service.findAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserCreated> create(@RequestBody CreateUser body) {
        UserCreated userCreated = this.service.create(body);
        return ResponseEntity
                .created(URI.create("/user/" + userCreated.userId()))
                .body(userCreated);
    }
}
