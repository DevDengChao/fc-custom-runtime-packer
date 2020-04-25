package dev.dengchao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    void namedGroup() {
        String s = "bootstrap-pro.sh";
        //language=RegExp
        String exp = "bootstrap(-(?<profile>[a-zA-Z0-9\\-]+))?(\\.sh)?";
        Matcher matcher = Pattern.compile(exp).matcher(s);
        Assertions.assertTrue(matcher.find());

        int count = matcher.groupCount();
        for (int i = 0; i < count; i++) {
            System.out.println(matcher.group(i));
        }

        Assertions.assertEquals("pro", matcher.group("profile"));
    }
}
