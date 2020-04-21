package dev.dengchao;

import dev.dengchao.bootstrap.collector.ProjectBootstrapCollector;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class FcCustomRuntimePackerPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        Map<String, File> bootstraps = new ProjectBootstrapCollector().collect(project.getProjectDir());
        Set<Map.Entry<String, File>> entries = bootstraps.entrySet();
        TaskContainer tasks = project.getTasks();

        for (Map.Entry<String, File> entry : entries) {
            String profile = entry.getKey();

            char c = profile.charAt(0);
            if (c > 'a' && c < 'z') {
                c += 'A' - 'a';
            }

            String name = "zipBootstrap" + c + profile.substring(1);
            tasks.create(name, ZipBootstrap.class, it -> {
                it.setProfile(profile);
                it.setBootstrap(entry.getValue());
                it.setBuild(project.getBuildFile());
            }).dependsOn("build");
        }
    }
}
