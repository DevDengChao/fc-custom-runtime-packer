package dev.dengchao;

import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

@Slf4j
@SuppressWarnings("unused")
public class FcCustomRuntimePackerPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        log.info("hello world");
    }
}
