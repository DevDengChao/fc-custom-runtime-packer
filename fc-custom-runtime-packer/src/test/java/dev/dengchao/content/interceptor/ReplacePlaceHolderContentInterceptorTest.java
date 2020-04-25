package dev.dengchao.content.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplacePlaceHolderContentInterceptorTest {


    private ReplacePlaceHolderContentInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new ReplacePlaceHolderContentInterceptor("demo-1.0.0.jar");
    }

    @Test
    void replaceArchive() throws Exception {
        assertEquals("java -jar demo-1.0.0.jar", interceptor.intercept("java -jar archive"));
    }

    @Test
    void replaceBootJar() throws Exception {
        assertEquals("java -jar demo-1.0.0.jar", interceptor.intercept("java -jar boot.jar"));
    }
}
