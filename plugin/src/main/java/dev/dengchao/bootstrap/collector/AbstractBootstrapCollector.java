package dev.dengchao.bootstrap.collector;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractBootstrapCollector implements BootstrapCollector {

    @NotNull
    private static final Logger logger = Logging.getLogger(AbstractBootstrapCollector.class);

    @Override
    public final @NotNull Map<String, File> collect(@NotNull File dir) {
        File[] files = dir.listFiles((file) ->
                file.isFile() && file.getName().matches(filterPattern()));

        HashMap<String, File> map = new HashMap<>();
        if (files == null || files.length == 0) {
            logger.warn("Bootstrap files not found under {}", dir);
            return map;
        }

        // for most case, there is only one bootstrap file.
        if (files.length == 1) {
            logger.info("Found single bootstrap at {}", files[0]);
            map.put(DEFAULT_PROFILE, files[0]);
            return map;
        }

        Pattern pattern = Pattern.compile(profileExtractionPattern());

        Matcher matcher;
        String profile;
        for (File file : files) {
            matcher = pattern.matcher(file.getName());
            if (matcher.find()) {
                profile = matcher.group("profile");
                if (profile == null) {
                    profile = DEFAULT_PROFILE;
                }
                profile = fixProfile(profile);
                profile = profile.isEmpty() ? DEFAULT_PROFILE : profile;
                File existing = map.get(profile);
                if (existing != null) {
                    throw new DuplicateBootstrapProfileException(profile, existing, file);
                }
                logger.info("Found profiled bootstrap [{}] at {}", profile, files[0]);
                map.put(profile, file);
            }
        }

        return map;
    }

    @NotNull
    protected abstract String filterPattern();

    @NotNull
    protected abstract String profileExtractionPattern();

    @NotNull
    protected abstract String fixProfile(@NotNull String profile);
}
