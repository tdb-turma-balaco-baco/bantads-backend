package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.inbound.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Test
    @DisplayName("should convert a CreateUser to a User Entity")
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
    @DisplayName("should convert a User Entity to UserResponse")
    void itShouldConvertEntityToResponse() {
        var user = new User();
        user.setId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");

        var response = UserMapper.toResponse.apply(user);
        assertNotNull(response);

        assertEquals(response.userId(), user.getId());
        assertEquals(response.firstName(), user.getFirstName());
        assertEquals(response.lastName(), user.getLastName());
        assertEquals(response.email(), user.getEmail());
    }
}