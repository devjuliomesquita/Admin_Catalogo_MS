package com.juliomesquita.admin.catalog;

import com.juliomesquita.admin.catalog.infrastructure.App;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = App.class)
@ExtendWith(CleanUpExtension.class)
public @interface IntegrationTest {
}
