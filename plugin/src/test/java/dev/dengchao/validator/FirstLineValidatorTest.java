package dev.dengchao.validator;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FirstLineValidatorTest {

    private SmartValidator validator;
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("bootstrap", "");
        validator = new FirstLineValidator();
    }

    @AfterEach
    void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    public void bashIsSupported() throws Exception {
        String content = "#!/bin/bash\n" +
                "\n" +
                "java -jar hello-world.jar";
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        assertTrue(validator.test(file));
    }

    @Test
    public void shIsSupported() throws Exception {
        String content = "#!/usr/bin/env sh\n" +
                "\n" +
                "java -jar hello-world.jar";
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        assertTrue(validator.test(file));
    }

    @Test
    public void headerNotFound() throws Exception {
        String content = "\n" +
                "\n" +
                "java -jar hello-world.jar";
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        assertFalse(validator.test(file));

        // fixing missed header
        assertTrue(validator.fix(file));
        assertTrue(validator.test(file));
    }
}