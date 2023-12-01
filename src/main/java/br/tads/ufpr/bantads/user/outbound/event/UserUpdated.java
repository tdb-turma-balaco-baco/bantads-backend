package br.tads.ufpr.bantads.user.outbound.event;

import org.jmolecules.event.types.DomainEvent;

public record UserUpdated(Long userId) implements DomainEvent {
}
