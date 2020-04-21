package dev.dengchao.bootstrap.collector;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Collect bootstrap files under {@link Project#getProjectDir() project dir}/bootstrap .
 * <p>
 * <ul>
 * <li> Bootstrap file's name MAY start with 'bootstrap'. </li>
 * <li> Bootstrap file's name MUST have a profile identifier. </li>
 * <li> Bootstrap file's name MAY end with '.sh'. </li>
 * </ul>
 */
class BootstrapDirBootstrapCollector extends AbstractBootstrapCollector {

    @Override
    protected @NotNull String filterPattern() {
        //language=RegExp
        return "([a-zA-Z0-9\\-]+)(\\.sh)?";
    }

    @Override
    protected @NotNull String profileExtractionPattern() {
        //language=RegExp
        return "(?<profile>[a-zA-Z0-9\\-]+)(\\.sh)?";
    }

    @Override
    protected @NotNull String fixProfile(@NotNull String profile) {
        return profile.replaceFirst("bootstrap(-)?", "");
    }
}
