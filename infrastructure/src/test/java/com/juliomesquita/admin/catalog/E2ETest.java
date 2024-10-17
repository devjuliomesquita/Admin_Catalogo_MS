package com.juliomesquita.admin.catalog;

import com.juliomesquita.admin.catalog.infrastructure.App;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = App.class)
@ExtendWith(CleanUpExtension.class)
@Testcontainers
@AutoConfigureMockMvc
public @interface E2ETest {
}
