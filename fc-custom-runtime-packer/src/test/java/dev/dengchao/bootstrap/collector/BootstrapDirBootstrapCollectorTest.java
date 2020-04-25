package dev.dengchao.bootstrap.collector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BootstrapDirBootstrapCollectorTest {

    private static File playground;
    private static BootstrapDirBootstrapCollector collector;

    @BeforeAll
    static void beforeAll() {
        playground = new File(System.getProperty("java.io.tmpdir"), "bootstrap-dir-bootstrap-collector-playground");
        if (playground.exists()) {
            assertTrue(playground.delete());
        }
        assertTrue(playground.mkdirs());

        collector = new BootstrapDirBootstrapCollector();
    }

    @AfterAll
    static void afterAll() {
        assertTrue(playground.delete());
    }

    @Test
    void notFound() {
        File dir = new File(playground, "not-found");
        assertTrue(dir.mkdir());
        Map<String, File> map = collector.collect(dir);
        assertTrue(map.isEmpty());
        assertTrue(dir.delete());
    }

    @Test
    void singleBootstrap() throws Exception {
        File dir = new File(playground, "single-bootstrap");
        assertTrue(dir.mkdir());
        File bootstrap = new File(dir, "bootstrap");
        assertTrue(bootstrap.createNewFile());

        Map<String, File> map = collector.collect(dir);
        assertEquals(1, map.size());
        assertEquals(bootstrap, map.get(BootstrapCollector.DEFAULT_PROFILE));

        assertTrue(bootstrap.delete());
        assertTrue(dir.delete());
    }

    @Test
    void multipleBootstrap() throws Exception {
        File dir = new File(playground, "multiple-bootstrap");
        assertTrue(dir.mkdir());
        File defaultProfile = new File(dir, "bootstrap");
        assertTrue(defaultProfile.createNewFile());
        File specifiedProfile = new File(dir, "bootstrap-profile");
        assertTrue(specifiedProfile.createNewFile());

        File specifiedProfileWithShSuffix = new File(dir, "bootstrap-shell.sh");
        assertTrue(specifiedProfileWithShSuffix.createNewFile());

        File arbitraryProfile = new File(dir, "arbitrary");
        assertTrue(arbitraryProfile.createNewFile());

        Map<String, File> map = collector.collect(dir);
        assertEquals(4, map.size());
        assertEquals(defaultProfile, map.get(BootstrapCollector.DEFAULT_PROFILE));
        assertEquals(specifiedProfile, map.get("profile"));
        assertEquals(specifiedProfileWithShSuffix, map.get("shell"));
        assertEquals(arbitraryProfile, map.get("arbitrary"));

        assertTrue(defaultProfile.delete());
        assertTrue(specifiedProfile.delete());
        assertTrue(specifiedProfileWithShSuffix.delete());
        assertTrue(arbitraryProfile.delete());
        assertTrue(dir.delete());
    }

    @Test
    void conflict() throws Exception {
        File dir = new File(playground, "conflict");
        assertTrue(dir.mkdir());

        File defaultProfile = new File(dir, "default");
        assertTrue(defaultProfile.createNewFile());
        File specifiedProfile = new File(dir, "bootstrap.sh");
        assertTrue(specifiedProfile.createNewFile());

        assertThrows(DuplicateBootstrapProfileException.class, () -> collector.collect(dir));

        assertTrue(specifiedProfile.delete());
        assertTrue(defaultProfile.delete());
        assertTrue(dir.delete());
    }

}