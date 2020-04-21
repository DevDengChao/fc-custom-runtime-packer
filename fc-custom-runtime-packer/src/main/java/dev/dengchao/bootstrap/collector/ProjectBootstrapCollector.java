package dev.dengchao.bootstrap.collector;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

/**
 * Gather bootstrap files from {@link Project#getProjectDir() project dir} and
 * {@link Project#getProjectDir() project dir}/bootstrap together.
 *
 * @see ProjectDirBootstrapCollector
 * @see BootstrapDirBootstrapCollector
 */
public class ProjectBootstrapCollector implements BootstrapCollector {

    @NotNull
    private final ProjectDirBootstrapCollector projectDirCollector;

    public ProjectBootstrapCollector() {
        projectDirCollector = new ProjectDirBootstrapCollector();
    }

    @Override
    public @NotNull Map<String, File> collect(@NotNull File dir) {
        Map<String, File> gathered = projectDirCollector.collect(dir);

        File bootstrap = new File(dir, "bootstrap");
        if (bootstrap.exists() && bootstrap.isDirectory()) {
            Map<String, File> map = new BootstrapDirBootstrapCollector().collect(bootstrap);
            for (Map.Entry<String, File> entry : map.entrySet()) {
                String key = entry.getKey();
                File existing = gathered.get(key);
                if (existing != null) {
                    throw new DuplicateBootstrapProfileException(key, existing, entry.getValue());
                }
                gathered.put(key, entry.getValue());
            }
        }
        return gathered;
    }
}
