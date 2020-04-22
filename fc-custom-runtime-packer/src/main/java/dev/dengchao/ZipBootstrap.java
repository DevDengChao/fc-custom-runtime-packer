package dev.dengchao;

import org.gradle.api.internal.file.collections.FileTreeAdapter;
import org.gradle.api.internal.file.collections.GeneratedSingletonFileTree;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Zip;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Zip build result together with bootstrap into a zip file.
 *
 * @see org.gradle.jvm.tasks.Jar
 */
@SuppressWarnings("UnstableApiUsage")
public class ZipBootstrap extends Zip {
    @NotNull
    private static final Logger logger = Logging.getLogger(ZipBootstrap.class);

    /**
     * The profile which this task is focusing on.
     */
    @Nullable
    private String profile;
    /**
     * The bootstrap file.
     */
    @Nullable
    private File bootstrap;
    /**
     * The bootJar archive file.
     */
    @Nullable
    private File bootJarArchive;

    public void setProfile(@NotNull String profile) {
        this.profile = profile;
    }

    public void setBootstrap(@NotNull File bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void setBootJarArchive(@NotNull File bootJarArchive) {
        this.bootJarArchive = bootJarArchive;
    }

    @TaskAction
    void taskAction() {
        Objects.requireNonNull(profile);
        Objects.requireNonNull(bootstrap);
        Objects.requireNonNull(bootJarArchive);

        getArchiveExtension().set(".zip");
        setMetadataCharset("UTF-8");

        File dir = new File(getProject().getBuildDir(), "generated/sources/bootstrap/" + profile);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        getMainSpec()
                .addChild()
                .from((Callable<FileTreeAdapter>) () -> {
                    GeneratedSingletonFileTree source = new GeneratedSingletonFileTree(getTemporaryDirFactory(), "bootstrap", out -> {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(bootstrap));
                            List<String> lines = reader.lines().collect(Collectors.toList());
                            for (String line : lines) {
                                out.write(line.replaceAll("\\$\\{archive}", bootJarArchive.getName()).getBytes());
                            }
                            out.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(String.format("Unable to transform bootstrap file %s", bootstrap), e);
                        }
                    });
                    return new FileTreeAdapter(source);
                })
                .into(dir);
        copy();

    }
}
