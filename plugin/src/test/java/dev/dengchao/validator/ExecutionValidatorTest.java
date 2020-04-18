package dev.dengchao.validator;


import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ExecutionValidatorTest {

    @Test
    void test() throws Exception {
        File file = File.createTempFile("bootstrap", "");
        file.deleteOnExit();

        ExecutionValidator validator = new ExecutionValidator();

        assertFalse(validator.test(file));
        assertTrue(validator.fix(file));
        assertTrue(validator.test(file));
    }
}