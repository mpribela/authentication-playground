package org.example.authentication.integration.endpoint;

import org.example.authentication.integration.DatabaseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class NoAuthenticationEndpointIntegrationTest extends DatabaseBase {

    @Autowired
    protected MockMvc mockMvc;

}
