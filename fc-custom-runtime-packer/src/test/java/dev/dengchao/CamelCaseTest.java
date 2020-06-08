package dev.dengchao;

import org.junit.jupiter.api.Test;

import static dev.dengchao.FcCustomRuntimePackerPlugin.camelCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CamelCaseTest {
    @Test
    void test() {
        assertEquals("Default", camelCase("default"));
        assertEquals("Part1Part2", camelCase("part1-part2"));
        assertEquals("Part1Part2", camelCase("part-1-part2"));
    }
}
