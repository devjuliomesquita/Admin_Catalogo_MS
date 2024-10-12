package com.juliomesquita.admin.catalog.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

public class AppTest {
    @Test
    void testApp() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
        Assertions.assertNotNull(new App());
        App.main(new String[]{});
    }

}
