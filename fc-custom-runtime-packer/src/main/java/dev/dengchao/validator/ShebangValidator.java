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
public class ShebangValidator implements SmartValidator {
    private static final Logger logger = Logging.getLogger(ShebangValidator.class);
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
                logger.warn("End of file when reading the first line from file {}", file);
                return false;
            }
            line = line.trim();
            if (line.isEmpty()) {
                logger.warn("No content when reading the first line from file {}", file);
                return false;
            }

            for (String accept : accepts) {
                if (line.startsWith(accept)) {
                    return true;
                }
            }

            logger.warn("Unacceptable shebang [{}] found at file {}", line, file);
            return false;
        } catch (IOException e) {
            logger.warn("Unable to read shebang form file {}", file, e);
            return false;
        }
    }

    @Override
    public boolean fix(@NotNull File file) {
        // fix the bootstrap file by simply adding a header
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.write(accepts.get(0).getBytes());
            randomAccessFile.write("\n".getBytes());
            randomAccessFile.close();
            return true;
        } catch (IOException e) {
            logger.warn("Unable to fix {}", file, e);
            return false;
        }
    }
}
