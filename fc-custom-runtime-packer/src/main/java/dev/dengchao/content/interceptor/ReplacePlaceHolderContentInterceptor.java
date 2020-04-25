package dev.dengchao.content.interceptor;

import org.jetbrains.annotations.NotNull;

public class ReplacePlaceHolderContentInterceptor implements ContentInterceptor {
    @NotNull
    private final String bootJarArchive;

    public ReplacePlaceHolderContentInterceptor(@NotNull String bootJarArchive) {
        this.bootJarArchive = bootJarArchive;
    }

    @NotNull
    @Override
    public String intercept(@NotNull String line) {
        return line.replaceAll("archive|boot\\.jar", bootJarArchive);
    }
}
