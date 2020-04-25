package dev.dengchao;

import dev.dengchao.bootstrap.collector.ProjectBootstrapCollector;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class FcCustomRuntimePackerPlugin implements Plugin<Project> {

    private static final Logger log = Logging.getLogger(FcCustomRuntimePackerPlugin.class);

    @Override
    public void apply(@NotNull Project project) {
        Object version = project.getVersion();
        if (Project.DEFAULT_VERSION.equals(version)) {
            throw new RuntimeException("Unable to determine project version, please move \"apply plugin: 'dev.dengchao.fc-custom-runtime-packer'\" below version.\n" +
                    "See https://stackoverflow.com/questions/13198358/how-to-get-project-version-in-custom-gradle-plugin for more details");
        }

        TaskContainer tasks = project.getTasks();
        Task bootJar = tasks.findByName("bootJar");
        if (bootJar == null) {
            log.warn("Task 'bootJar' not found, are you forget to add 'org.springframework.boot' plugin?");
            return;
        }
        if (!(bootJar instanceof AbstractArchiveTask)) {
            throw new RuntimeException("Task 'bootJar' has changed its specification, please contact https://github.com/XieEDeHeiShou to update this plugin.");
        }
        AbstractArchiveTask archiveTask = (AbstractArchiveTask) bootJar;
        File archive = archiveTask.getArchiveFile().get().getAsFile();
        log.info("Found bootJar.archiveFile at {}", archive);

        Map<String, File> bootstraps = new ProjectBootstrapCollector().collect(project.getProjectDir());
        Set<Map.Entry<String, File>> entries = bootstraps.entrySet();
        if (entries.isEmpty()) {
            log.warn("Bootstrap files not found");
            return;
        }

        Task zipBootstrap = tasks.create("zipBootstrap");
        zipBootstrap.setGroup(BasePlugin.BUILD_GROUP);
        zipBootstrap.setDescription(String.format("Zip each bootstrap file together with %s", archive.getName()));

        for (Map.Entry<String, File> entry : entries) {
            String profile = entry.getKey();

            char c = profile.charAt(0);
            if (c > 'a' && c < 'z') {
                c += 'A' - 'a';
            }

            String name = "zipBootstrap" + c + profile.substring(1);
            tasks.create(name, ZipBootstrap.class, it -> {
                it.getLogger().info("Configuring " + it.getName());
                it.setGroup(BasePlugin.BUILD_GROUP);
                it.setDescription(String.format("Zip bootstrap file %1$s together with %2$s", profile, archive.getName()));
                it.setProfile(profile);
                it.setBootstrap(entry.getValue());
                it.setBootJarArchive(archive);
                it.dependsOn(bootJar);
            });
            zipBootstrap.dependsOn(name);
        }
    }
}
