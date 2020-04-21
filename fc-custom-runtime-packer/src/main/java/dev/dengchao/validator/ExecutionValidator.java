package dev.dengchao.validator;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Validate the execution permission
 */
public class ExecutionValidator implements SmartValidator {

    @Override
    public boolean test(@NotNull File file) {
        return file.canExecute();
    }

    @Override
    public boolean fix(@NotNull File file) {
        return file.setExecutable(true, false);
    }
}
