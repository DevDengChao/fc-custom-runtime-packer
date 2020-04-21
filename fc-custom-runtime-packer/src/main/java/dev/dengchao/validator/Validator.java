package dev.dengchao.validator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Predicate;

public interface Validator extends Predicate<File> {
    @Override
    boolean test(@NotNull File file);
}
