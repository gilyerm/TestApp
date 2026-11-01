package com.example.testapp.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId("user_1");
        user.setEmail("john.doe@example.com");
        user.setPassword("secret123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setAdmin(true);

        assertEquals("user_1", user.getId());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("secret123", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1234567890", user.getPhone());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testGetFullName() {
        User user = new User("u1", "a@b.com", "pass", "Alice", "Smith", "123", false);
        assertEquals("Alice Smith", user.getFullName());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("u1", "email1@example.com", "p1", "A", "B", "111", false);
        User user2 = new User("u1", "email2@example.com", "p2", "C", "D", "222", true);
        User user3 = new User("u2", "email1@example.com", "p1", "A", "B", "111", false);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
