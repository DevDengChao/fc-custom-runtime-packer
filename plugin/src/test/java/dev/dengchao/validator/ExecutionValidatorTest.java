package dev.dengchao.validator;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExecutionValidatorTest {

    @Test
    public void test() throws Exception {
        File file = File.createTempFile("bootstrap", "");
        file.deleteOnExit();

        ExecutionValidator validator = new ExecutionValidator();

        assertFalse(validator.test(file));
        assertTrue(validator.fix(file));
        assertTrue(validator.test(file));
    }
}