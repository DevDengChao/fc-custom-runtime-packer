package dev.dengchao.bootstrap.collector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDirBootstrapCollectorTest {

    private static File playground;
    private static ProjectDirBootstrapCollector collector;

    @BeforeAll
    static void beforeAll() {
        playground = new File(System.getProperty("java.io.tmpdir"), "project-bootstrap-collector-playground");
        if (playground.exists()) {
            assertTrue(playground.delete());
        }
        assertTrue(playground.mkdirs());

        collector = new ProjectDirBootstrapCollector();
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
        File specifiedProfile = new File(dir, "bootstrap-profile.sh");
        assertTrue(specifiedProfile.createNewFile());

        Map<String, File> map = collector.collect(dir);
        assertEquals(2, map.size());
        assertEquals(defaultProfile, map.get(BootstrapCollector.DEFAULT_PROFILE));
        assertEquals(specifiedProfile, map.get("profile"));

        assertTrue(defaultProfile.delete());
        assertTrue(specifiedProfile.delete());
        assertTrue(dir.delete());
    }

    @Test
    void conflict() throws Exception {
        File dir = new File(playground, "conflict");
        assertTrue(dir.mkdir());
        File defaultProfile = new File(dir, "bootstrap");
        assertTrue(defaultProfile.createNewFile());
        File specifiedProfile = new File(dir, "bootstrap.sh");
        assertTrue(specifiedProfile.createNewFile());

        assertThrows(RuntimeException.class, () -> collector.collect(dir));

        assertTrue(defaultProfile.delete());
        assertTrue(specifiedProfile.delete());
        assertTrue(dir.delete());
    }
}