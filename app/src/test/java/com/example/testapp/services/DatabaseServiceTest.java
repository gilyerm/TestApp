package com.example.testapp.services;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class DatabaseServiceTest {

    @After
    public void tearDown() {
        // Reset to default backend after each test
        DatabaseService.setBackend(DatabaseService.DatabaseBackend.FIRESTORE);
    }

    @Test
    public void testDefaultBackendIsFirestore() {
        assertEquals(DatabaseService.DatabaseBackend.FIRESTORE, DatabaseService.getBackend());
    }

    @Test
    public void testSwitchBackendToRealtimeDatabase() {
        DatabaseService.setBackend(DatabaseService.DatabaseBackend.REALTIME_DATABASE);
        assertEquals(DatabaseService.DatabaseBackend.REALTIME_DATABASE, DatabaseService.getBackend());
    }

    @Test
    public void testDatabaseBackendEnumValues() {
        DatabaseService.DatabaseBackend[] backends = DatabaseService.DatabaseBackend.values();
        assertEquals(2, backends.length);
        assertEquals(DatabaseService.DatabaseBackend.FIRESTORE, DatabaseService.DatabaseBackend.valueOf("FIRESTORE"));
        assertEquals(DatabaseService.DatabaseBackend.REALTIME_DATABASE, DatabaseService.DatabaseBackend.valueOf("REALTIME_DATABASE"));
    }
}
