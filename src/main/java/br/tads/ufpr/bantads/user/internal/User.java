package br.tads.ufpr.bantads.user.internal;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, unique = true, name = "email")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;
}
