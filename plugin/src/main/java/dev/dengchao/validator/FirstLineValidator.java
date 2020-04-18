package dev.dengchao.validator;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Validate the first line's content equals to known values.
 */
@Slf4j
public class FirstLineValidator implements Validator {
    /**
     * Accepted values
     */
    private final List<String> accepts = Arrays.asList("#!/bin/bash", "#!/usr/bin/env sh");

    @Override
    public boolean test(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            return accepts.contains(line);
        } catch (IOException e) {
            log.warn("Unable to read the first line form {}", file, e);
            return false;
        }
    }
}
