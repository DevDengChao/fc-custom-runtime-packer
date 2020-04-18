package dev.dengchao.bootstrap.collector;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Collect bootstrap files under {@link Project#getProjectDir() project dir}.
 * <p>
 * <ul>
 * <li> Bootstrap file's name MUST start with 'bootstrap'. </li>
 * <li> Bootstrap file's name MAY have a profile identifier. </li>
 * <li> Bootstrap file's name MAY end with '.sh'. </li>
 * </ul>
 */
class ProjectDirBootstrapCollector extends AbstractBootstrapCollector {

    @Override
    protected @NotNull String filterPattern() {
        //language=RegExp
        return "bootstrap(-[a-zA-Z0-9\\-]+)?(\\.sh)?";
    }

    @Override
    protected @NotNull String profileExtractionPattern() {
        //language=RegExp
        return "bootstrap(-(?<profile>[a-zA-Z0-9\\-]+))?(\\.sh)?";
    }

    @Override
    protected @NotNull String fixProfile(@NotNull String profile) {
        return profile;
    }
}
