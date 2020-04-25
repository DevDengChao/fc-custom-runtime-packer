package dev.dengchao.content.interceptor;

import org.jetbrains.annotations.NotNull;

public interface ContentInterceptor {
    @NotNull
    String intercept(@NotNull String line);
}
