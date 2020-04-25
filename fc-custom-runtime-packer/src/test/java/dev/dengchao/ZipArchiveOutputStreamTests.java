package dev.dengchao;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ZipArchiveOutputStreamTests {
    // https://www.computerhope.com/unix/uls.htm#long-listing
    // https://www.computerhope.com/unix/uchmod.htm

    private static void print(@NotNull String... cmd) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(cmd).start();
        process.waitFor();

        InputStream inputStream = process.getInputStream();
        int size = inputStream.available();
        byte[] bytes = new byte[size];
        assertEquals(size, inputStream.read(bytes));
        System.out.println(new String(bytes));
    }

    private static void zip(@NotNull File dir, @NotNull File output) throws IOException {
        ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(new FileOutputStream(output));

        // gradlew is -rwxrwxr-x which equals to 'chmod 775 gradlew'

        int mode = 0x775;
        // mode    |permission  |bash executable  |self executable    |writable
        // 0x000    ----------   no                no                  protected
        // 0x111    -r---w---x   yes               no                  protected
        // 0x222    ----r---w-   no                no                  protected
        // 0x333    -r--rw--wx   yes               no                  protected
        // 0x444    ---x---r--   no                yes                 protected
        // 0x555    -r-x-w-r-x   yes               yes                 protected   (optional)
        // 0x666    ---xr--rw-   no                yes                 protected
        // 0x755    -r-x-w-r-x   yes               yes                 protected   (optional)
        // 0x775    -r-xrw-r-x   yes               yes                 protected   (optional)  (prefer)
        // 0x777    -r-xrw-rwx   yes               yes                 protected   (optional)
        // 0x888    --w---x---   no                no                  protected
        // 0x995    -rw--w-r-x   yes               no                  yes
        // 0x999    -rw--wx--x   yes               no                  yes

        File gradlew = new File(dir, "gradlew");
        assertTrue(gradlew.canExecute());
        FileInputStream in = new FileInputStream(gradlew);
        int size = in.available();
        byte[] bytes = new byte[size];
        Assertions.assertEquals(size, in.read(bytes));
        ZipArchiveEntry entry = new ZipArchiveEntry("gradlew.sh");
        entry.setUnixMode(mode);
        outputStream.putArchiveEntry(entry);
        outputStream.write(bytes);
        outputStream.closeArchiveEntry();

        outputStream.flush();
        outputStream.close();
        System.out.println(String.format("Zip %1$s into %2$s as gradlew.sh success", gradlew, output));
    }

    private static void unzip(@NotNull File dir, @NotNull File input) throws IOException, InterruptedException {
        if (!System.getProperties().getOrDefault("os.name", "Linux").equals("Linux")) {
            System.out.println(String.format("unzip command may not available on your os.\n" +
                    "Please unzip %s manually to verify whether gradlew.sh is executable or not.", input));
            return;
        }
        File previous = new File(dir, "gradlew.sh");
        if (previous.exists()) {
            Assertions.assertTrue(previous.delete());
        }
        String outputDir = dir.getAbsolutePath();
        new ProcessBuilder("unzip", "-d", outputDir, input.getAbsolutePath()).start().waitFor();
    }

    private static void verify(@NotNull String outputDir) throws IOException, InterruptedException {
        System.out.println(String.format("Executing 'ls -l %s'", outputDir));
        print("ls", "-l", outputDir);

        System.out.println(String.format("Executing '%s/gradlew.sh --version'", outputDir));
        print(outputDir + "/gradlew.sh", "--version");

        System.out.println(String.format("Executing 'bash %s/gradlew.sh --version'", outputDir));
        print("bash", outputDir + "/gradlew.sh", "--version");
    }

    private static void cleanup(@NotNull File dir) {
        assertTrue(new File(dir, "gradlew.zip").delete());
        assertTrue(new File(dir, "gradlew.sh").delete());
    }

    @Test
    void test() throws Exception {
        File dir = new File("../");

        File gradlewZip = new File(dir, "gradlew.zip");
        if (!gradlewZip.exists()) {
            Assertions.assertTrue(gradlewZip.createNewFile());
        }

        zip(dir, gradlewZip);
        unzip(dir, gradlewZip);

        verify(dir.getAbsolutePath());
        cleanup(dir);
    }
}
