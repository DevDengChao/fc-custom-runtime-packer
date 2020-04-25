package dev.dengchao.validator;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface SmartValidator extends Validator {

    /**
     * Attempt to fix invalid parts.
     *
     * @param file the bootstrap file tobe fixed
     * @return whether patch is applied
     */
    boolean fix(@NotNull File file);
}
