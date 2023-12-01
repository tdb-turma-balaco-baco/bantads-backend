package br.tads.ufpr.bantads.manager.internal;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "manager")
@Table(name = "manager")
@Data
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_id_seq")
    @SequenceGenerator(name = "manager_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;
    @Column(name = "phone", nullable = false)
    private String phone;
}
