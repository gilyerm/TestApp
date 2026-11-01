package com.example.testapp.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidatorTest {

    @Test
    public void testIsPasswordValid() {
        assertTrue(Validator.isPasswordValid("123456"));
        assertTrue(Validator.isPasswordValid("password123"));
        assertFalse(Validator.isPasswordValid("12345"));
        assertFalse(Validator.isPasswordValid(""));
        assertFalse(Validator.isPasswordValid(null));
    }

    @Test
    public void testIsNameValid() {
        assertTrue(Validator.isNameValid("Bob"));
        assertTrue(Validator.isNameValid("Alice"));
        assertFalse(Validator.isNameValid("Bo"));
        assertFalse(Validator.isNameValid(""));
        assertFalse(Validator.isNameValid(null));
    }
}
