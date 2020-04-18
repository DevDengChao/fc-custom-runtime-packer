package dev.dengchao.bootstrap.collector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectBootstrapCollectorTest {
    private static File playground;

    private static ProjectBootstrapCollector collector;

    @BeforeAll
    static void beforeAll() {
        playground = new File(System.getProperty("java.io.tmpdir"), "project-bootstrap-collector-playground");
        if (playground.exists()) {
            assertTrue(playground.delete());
        }
        assertTrue(playground.mkdirs());

        collector = new ProjectBootstrapCollector();
    }

    @AfterAll
    static void afterAll() {
        assertTrue(playground.delete());
    }

    @Test
    void gatherTogether() throws Exception {
        File defaultProfile = new File(playground, "bootstrap.sh");
        assertTrue(defaultProfile.createNewFile());
        File profile = new File(playground, "bootstrap-profile");
        assertTrue(profile.createNewFile());

        Map<String, File> map = new ProjectDirBootstrapCollector().collect(playground);
        assertEquals(2, map.size());
        assertEquals(defaultProfile, map.get(BootstrapCollector.DEFAULT_PROFILE));
        assertEquals(profile, map.get("profile"));


        File bootstrapDir = new File(playground, "bootstrap");
        assertTrue(bootstrapDir.mkdir());
        File subProfile = new File(bootstrapDir, "sub-profile.sh");
        assertTrue(subProfile.createNewFile());

        map = new BootstrapDirBootstrapCollector().collect(bootstrapDir);
        assertEquals(1, map.size());
        assertEquals(subProfile, map.get("sub-profile"));


        map = collector.collect(playground);
        assertEquals(3, map.size());

        assertEquals(defaultProfile, map.get(BootstrapCollector.DEFAULT_PROFILE));
        assertEquals(profile, map.get("profile"));
        assertEquals(subProfile, map.get("sub-profile"));

        assertTrue(subProfile.delete());
        assertTrue(bootstrapDir.delete());
        assertTrue(profile.delete());
        assertTrue(defaultProfile.delete());
    }

    @Test
    void conflict() throws Exception {
        File defaultProfile = new File(playground, "bootstrap.sh");
        assertTrue(defaultProfile.createNewFile());

        File bootstrapDir = new File(playground, "bootstrap");
        assertTrue(bootstrapDir.mkdir());
        File subDefaultProfile = new File(bootstrapDir, "default");
        assertTrue(subDefaultProfile.createNewFile());

        assertThrows(DuplicateBootstrapProfileException.class, () -> collector.collect(playground));

        assertTrue(subDefaultProfile.delete());
        assertTrue(bootstrapDir.delete());
        assertTrue(defaultProfile.delete());
    }
}