package dev.dengchao.bootstrap.collector;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collect bootstrap files under {@link Project#getProjectDir() project dir}.
 * <p>
 * <ul>
 * <li> Bootstrap file's name MUST start with 'bootstrap'. </li>
 * <li> Bootstrap file's name MAY have a profile identifier. </li>
 * <li> Bootstrap file's name MAY end with '.sh'. </li>
 * </ul>
 */
public class ProjectDirBootstrapCollector implements BootstrapCollector {

    @NotNull
    private static final Logger logger = Logging.getLogger(ProjectDirBootstrapCollector.class);

    @Override
    public @NotNull Map<String, File> collect(@NotNull File dir) {
        File[] files = dir.listFiles((file) ->
                file.isFile() && file.getName().matches("bootstrap(-[a-zA-Z0-9\\-]+)?(\\.sh)?"));

        HashMap<String, File> map = new HashMap<>();
        if (files == null || files.length == 0) {
            logger.warn("Bootstrap files not found under {}", dir);
            return map;
        }

        // for most case, there is only one bootstrap file.
        if (files.length == 1 && files[0].getName().equals("bootstrap")) {
            logger.info("Found single bootstrap at {}", files[0]);
            map.put(DEFAULT_PROFILE, files[0]);
            return map;
        }

        //language=RegExp
        String exp = "bootstrap(-(?<profile>[a-zA-Z0-9\\-]+))?(\\.sh)?";
        Pattern pattern = Pattern.compile(exp);

        Matcher matcher;
        String profile;
        for (File file : files) {
            matcher = pattern.matcher(file.getName());
            if (matcher.find()) {
                profile = matcher.group("profile");
                profile = profile == null || profile.isEmpty() ? DEFAULT_PROFILE : profile;
                File existing = map.get(profile);
                if (existing != null) {
                    String template = "Profile [%s] appears more than once, please consider remove one form \n%s\nor\n%s\n";
                    String message = String.format(template, profile, existing, file);
                    throw new RuntimeException(message);
                }
                logger.info("Found profiled bootstrap [{}] at {}", profile, files[0]);
                map.put(profile, file);
            }
        }

        return map;
    }
}
