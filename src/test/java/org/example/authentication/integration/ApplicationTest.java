package org.example.authentication.integration;

import org.example.authentication.integration.base.DatabaseBase;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest extends DatabaseBase {

    @Test
    @DisplayName("when application starts then initialize the context")
    void smokeTest() { }
}
