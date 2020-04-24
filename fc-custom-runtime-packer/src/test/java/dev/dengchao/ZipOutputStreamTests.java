package dev.dengchao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


class ZipOutputStreamTests {

    @Test
    void test() throws Exception {
        File dir = new File("../gradle/wrapper");

        File output = new File(dir, "gradle-wrapper.zip");
        if (!output.exists()) {
            Assertions.assertTrue(output.createNewFile());
        }
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(output));

        zip(dir, outputStream, "gradle-wrapper.properties");
        zip(dir, outputStream, "gradle-wrapper.jar");

        outputStream.flush();
        outputStream.close();
    }

    private void zip(File dir, ZipOutputStream outputStream, String name) throws IOException {
        outputStream.putNextEntry(new ZipEntry(name));
        FileInputStream in = new FileInputStream(new File(dir, name));
        int size = in.available();
        byte[] bytes = new byte[size];
        Assertions.assertEquals(size, in.read(bytes));
        outputStream.write(bytes);
        outputStream.closeEntry();
    }
}
