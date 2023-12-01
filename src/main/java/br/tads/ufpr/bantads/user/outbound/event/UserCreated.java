package br.tads.ufpr.bantads.user.outbound.event;

import org.jmolecules.event.types.DomainEvent;

public record UserCreated(Long userId) implements DomainEvent {
}
