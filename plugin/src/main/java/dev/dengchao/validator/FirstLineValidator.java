package dev.dengchao.validator;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Validate the first line's content equals to known values.
 */
public class FirstLineValidator implements SmartValidator {
    private static final Logger log = Logging.getLogger(FirstLineValidator.class);
    /**
     * Accepted values
     */
    @NotNull
    private final List<String> accepts = Arrays.asList("#!/bin/bash", "#!/usr/bin/env sh");

    @Override
    public boolean test(@NotNull File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            if (line == null) {
                return false;
            }
            line = line.trim();
            return !line.isEmpty() && accepts.contains(line);
        } catch (IOException e) {
            log.warn("Unable to read the first line form {}", file, e);
            return false;
        }
    }

    @Override
    public boolean fix(@NotNull File file) {
        // fix the bootstrap file by simply adding a header
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "w");
            randomAccessFile.seek(0);
            randomAccessFile.writeUTF(accepts.get(0));
            randomAccessFile.writeUTF("\n");
            randomAccessFile.close();
            return true;
        } catch (IOException e) {
            log.warn("Unable to fix {}", file, e);
            return false;
        }
    }
}
