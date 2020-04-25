package dev.dengchao.content.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShebangInterceptorTest {

    private ShebangInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new ShebangInterceptor();
    }

    @Test
    void shebangNotFound() {
        assertEquals("#!/bin/bash", interceptor.intercept(""));
    }

    @Test
    void bash() {
        String bash = "#!/bin/bash";
        assertEquals("#!/bin/bash", interceptor.intercept(bash));
    }

    @Test
    void shell() {
        String shell = "#!/usr/bin/env sh";
        assertEquals(shell, interceptor.intercept(shell));
    }

    @Test
    void invalidShebang() {
        assertEquals("#!/bin/bash", interceptor.intercept("#!/bin/invalid-shebang"));
    }
}
