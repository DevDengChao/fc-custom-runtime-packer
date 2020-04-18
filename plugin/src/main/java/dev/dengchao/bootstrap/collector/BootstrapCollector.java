package dev.dengchao.bootstrap.collector;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

/**
 * Collect bootstrap file from specified dir
 */
public interface BootstrapCollector {

    /**
     * Collect bootstrap file from specified dir
     *
     * @param dir where we search for bootstrap files
     * @return a map who's key is the bootstrap's profile, and the value is the bootstrap file's path.
     */
    @NotNull
    Map<String, File> collect(@NotNull File dir);
}
