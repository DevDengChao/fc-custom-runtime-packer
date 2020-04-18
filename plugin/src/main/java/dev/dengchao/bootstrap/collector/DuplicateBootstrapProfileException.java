package dev.dengchao.bootstrap.collector;

import org.jetbrains.annotations.NotNull;

import java.io.File;

class DuplicateBootstrapProfileException extends RuntimeException {

    @NotNull
    private final File previous, current;
    @NotNull
    private final String profile;

    DuplicateBootstrapProfileException(@NotNull String profile, @NotNull File previous, @NotNull File current) {
        super(String.format("Profile [%s] appears more than once, please consider remove one form \n%s\nor\n%s\n", profile, previous, current));
        this.profile = profile;
        this.previous = previous;
        this.current = current;
    }

    @NotNull
    public File getPrevious() {
        return previous;
    }

    @NotNull
    public File getCurrent() {
        return current;
    }

    @NotNull
    public String getProfile() {
        return profile;
    }
}
