package br.tads.ufpr.bantads.saga;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/saga/customer")
@RequiredArgsConstructor
public class CustomerController {
    @GetMapping
    public ResponseEntity<String> findAll() {
        return ResponseEntity.ok("Customers[]");
    }

    @PostMapping
    public ResponseEntity<String> create() {
        return ResponseEntity
                .created(URI.create("/customer/" + UUID.randomUUID()))
                .build();
    }
}
