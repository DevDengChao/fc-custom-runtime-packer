package dev.dengchao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringTests {

    @Test
    void replaceAll() {
        String replaced = "sample-1.0.0.jar".replaceFirst("\\.jar", "\\.zip");
        Assertions.assertEquals("sample-1.0.0.zip", replaced);


        String expected = "java -jar hello-world-1.0.0.jar --spring.profiles.active=dev";
        String archive = "hello-world-1.0.0.jar";
        String pattern = "archive|boot\\.jar";
        String bootJarTemplate = "java -jar boot.jar --spring.profiles.active=dev";
        String archiveTemplate = "java -jar archive --spring.profiles.active=dev";
        Assertions.assertEquals(expected, bootJarTemplate.replaceAll(pattern, archive));
        Assertions.assertEquals(expected, archiveTemplate.replaceAll(pattern, archive));
    }
}
