package com.team5.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI conferenceManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Conference Management System API")
                        .description("""
                            API documentation for the Conference Management System.
                            This API allows you to manage sessions, papers, and user assignments.
                            
                            ## Authentication
                            The API uses JWT authentication. Include the token in the `Authorization` header.
                            
                            ## Versioning
                            Current API version: 1.0.0
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Conference Management Team")
                                .email("support@conference-system.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
