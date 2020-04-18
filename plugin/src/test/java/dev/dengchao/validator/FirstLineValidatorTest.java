package dev.dengchao.validator;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FirstLineValidatorTest {

    private SmartValidator validator;
    private File file;

    @Before
    public void setUp() throws IOException {
        file = File.createTempFile("bootstrap", "");
        validator = new FirstLineValidator();
    }

    @Test
    public void bashIsSupported() throws Exception {
        String content = "#!/bin/bash\n" +
                "\n" +
                "java -jar hello-world.jar";
        new FileWriter(file).write(content);
        assertTrue(validator.test(file));
    }

    @Test
    public void shIsSupported() throws Exception {
        String content = "#!/usr/bin/env sh\n" +
                "\n" +
                "java -jar hello-world.jar";
        new FileWriter(file).write(content);
        assertTrue(validator.test(file));
    }

    @Test
    public void headerNotFound() throws Exception {
        String content = "\n" +
                "\n" +
                "java -jar hello-world.jar";
        new FileWriter(file).write(content);
        assertFalse(validator.test(file));

        // fixing missed header
        assertTrue(validator.fix(file));
        assertTrue(validator.test(file));
    }
}