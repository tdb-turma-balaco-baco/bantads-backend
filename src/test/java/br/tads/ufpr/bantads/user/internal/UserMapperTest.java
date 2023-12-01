package br.tads.ufpr.bantads.user.internal;

import br.tads.ufpr.bantads.user.utils.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Test
    @DisplayName("should convert the entity to response")
    void itShouldConvertEntityToResponse() {
        var user = UserBuilder.of();
        user.setId(1L);

        var response = UserMapper.toResponse.apply(user);
        assertNotNull(response);

        assertEquals(response.userId(), user.getId());
        assertEquals(response.email(), user.getEmail());
    }
}