package com.xlr8code.server.user.utils;

import com.xlr8code.server.user.entity.Role;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRoleTest {

    @Test
    void it_should_have_members_as_default() {
        var expected = UserRole.MEMBER;
        var actual = UserRole.DEFAULT;

        assertEquals(expected, actual);
    }

    @Test
    void it_should_convert_to_role() {
        var actual = UserRole.MEMBER.toRole();

        var expected = Role.class;

        assertNotNull(actual);
        assertInstanceOf(expected, actual);
    }

}