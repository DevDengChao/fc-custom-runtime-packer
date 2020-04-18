package dev.dengchao.bootstrap.collector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void singleBootstrap() throws Exception {
        File dir = new File(playground, "single-bootstrap");
        assertTrue(dir.mkdir());
        File bootstrap = new File(dir, "bootstrap");
        assertTrue(bootstrap.createNewFile());

        Map<String, File> map = collector.collect(dir);
        assertEquals(1, map.size());
        assertEquals(bootstrap, map.get(""));

        assertTrue(bootstrap.delete());
        assertTrue(dir.delete());
    }
}