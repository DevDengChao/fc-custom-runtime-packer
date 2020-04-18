package dev.dengchao;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class FcCustomRuntimePackerPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        project.getLogger().info("hello world");
    }
}
