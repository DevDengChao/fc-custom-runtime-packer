package dev.dengchao.content.interceptor;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ShebangInterceptor implements ContentInterceptor {

    /**
     * Accepted values
     */
    @NotNull
    private static final List<String> ACCEPTABLE_SHEBANG = Arrays.asList("#!/bin/bash", "#!/usr/bin/env sh");

    @Override
    public @NotNull String intercept(@NotNull String line) {
        return ACCEPTABLE_SHEBANG.contains(line) ? line : ACCEPTABLE_SHEBANG.get(0);
    }
}
