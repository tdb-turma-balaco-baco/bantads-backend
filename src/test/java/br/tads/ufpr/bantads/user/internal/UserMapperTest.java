package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import br.tads.ufpr.bantads.user.utils.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Test
    @DisplayName("should convert a createUser record to a entity")
    void itShouldConvertCreateUserToEntity() {
        var request = new CreateUser(
                "firstName",
                "lastName",
                "email@email.com",
                "password"
        );

        var entity = UserMapper.createUserToEntity.apply(request);
        assertNotNull(entity);

        assertNull(entity.getId());
        assertEquals(entity.getFirstName(), request.firstName());
        assertEquals(entity.getLastName(), request.lastName());
        assertEquals(entity.getEmail(), request.email());
    }

    @Test
    @DisplayName("should convert the entity to response")
    void itShouldConvertEntityToResponse() {
        var user = UserBuilder.create();
        user.setId(1L);

        var response = UserMapper.toResponse.apply(user);
        assertNotNull(response);

        assertEquals(response.userId(), user.getId());
        assertEquals(response.firstName(), user.getFirstName());
        assertEquals(response.lastName(), user.getLastName());
        assertEquals(response.email(), user.getEmail());
    }
}