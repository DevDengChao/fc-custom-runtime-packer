package dev.dengchao;

import dev.dengchao.content.interceptor.ContentInterceptor;
import dev.dengchao.content.interceptor.ReplacePlaceHolderContentInterceptor;
import dev.dengchao.content.interceptor.ShebangInterceptor;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

/**
 * Zip build result together with bootstrap into a zip file.
 */
public class ZipBootstrap extends DefaultTask {

    private static final ContentInterceptor SHEBANG_INTERCEPTOR = new ShebangInterceptor();
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
    private File output;

    void setProfile(@NotNull String profile) {
        this.profile = profile;
    }

    void setBootstrap(@NotNull File bootstrap) {
        this.bootstrap = bootstrap;
    }

    void setBootJarArchive(@NotNull File bootJarArchive) {
        this.bootJarArchive = bootJarArchive;
    }

    @OutputFile
    public File getOutput() {
        Objects.requireNonNull(bootJarArchive);

        File dir = new File(getProject().getBuildDir(), "libs");
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        output = new File(dir, bootJarArchive.getName().replaceFirst("\\.jar", "-" + profile + "\\.zip"));

        return output;
    }

    @TaskAction
    public void taskAction() throws IOException {
        Objects.requireNonNull(profile);
        Objects.requireNonNull(bootstrap);
        Objects.requireNonNull(bootJarArchive);

        getLogger().debug("Output into {}", output);

        ContentInterceptor replacePlaceHolderContentInterceptor = new ReplacePlaceHolderContentInterceptor(bootJarArchive.getName());

        ZipArchiveOutputStream out = new ZipArchiveOutputStream(new FileOutputStream(output));

        //region zip bootstrap
        ZipArchiveEntry bootstrapEntry = new ZipArchiveEntry("bootstrap");
        bootstrapEntry.setUnixMode(0x775);
        out.putArchiveEntry(bootstrapEntry);
        List<String> lines = new BufferedReader(new FileReader(this.bootstrap)).lines().collect(Collectors.toList());
        byte[] newLine = "\n".getBytes();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == 0) {
                line = SHEBANG_INTERCEPTOR.intercept(line);
            } else {
                line = replacePlaceHolderContentInterceptor.intercept(line);
            }
            out.write(line.getBytes());
            out.write(newLine);
        }
        out.closeArchiveEntry();
        getLogger().debug("Bootstrap file zipped");
        //endregion

        //region zip boot jar
        FileInputStream bootJarInputStream = new FileInputStream(bootJarArchive);
        byte[] bytes = new byte[bootJarInputStream.available()];
        if (bootJarInputStream.read(bytes) != bytes.length) {
            throw new RuntimeException(String.format("Unable to read %s fully", bootJarArchive));
        }
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(bootJarArchive.getName());
        archiveEntry.setMethod(ZipArchiveEntry.STORED);
        archiveEntry.setCompressedSize(bootJarArchive.length());
        archiveEntry.setSize(bootJarArchive.length());
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        archiveEntry.setCrc(crc32.getValue());
        out.putArchiveEntry(archiveEntry);
        out.write(bytes);
        out.closeArchiveEntry();
        getLogger().debug("Boot jar file zipped");
        //endregion

        out.flush();
        out.close();
    }
}
