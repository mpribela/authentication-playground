package org.example.library.integration;

import org.example.library.integration.base.DatabaseBase;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest extends DatabaseBase {

    @Test
    @DisplayName("when application starts then initialize the context")
    void smokeTest() { }
}
