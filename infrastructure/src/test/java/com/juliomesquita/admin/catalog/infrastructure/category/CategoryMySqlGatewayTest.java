package com.juliomesquita.admin.catalog.infrastructure.category;

import com.juliomesquita.admin.catalog.infrastructure.MySqlGatewayTest;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySqlGatewayTest
class CategoryMySqlGatewayTest {
    @Autowired
    private CategoryMySqlGateway categoryMySqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void given_when_then() {
        Assertions.assertNotNull(categoryMySqlGateway);
        Assertions.assertNotNull(categoryRepository);
    }

}