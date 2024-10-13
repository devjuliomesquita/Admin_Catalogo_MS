package com.juliomesquita.admin.catalog.infrastructure.category;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.infrastructure.MySqlGatewayTest;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity;
import com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MySqlGatewayTest
class CategoryMySqlGatewayTest {
    @Autowired
    private CategoryMySqlGateway categoryMySqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_thenReturnANewCategory() {
        //verify database
        assertEquals(0, this.categoryRepository.count());

        //given
        final String expectedName = "Animes";
        final String expectedDescription = "Animes de comédia";
        final boolean expectedActive = true;
        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

        //when
        final Category aCategoryCreated = this.categoryMySqlGateway.create(aCategory);

        //then
        assertEquals(1, this.categoryRepository.count());
        assertAll("Verify category created - level use case", () -> {
            assertEquals(aCategory.getId(), aCategoryCreated.getId());
            assertEquals(expectedName, aCategoryCreated.getName());
            assertEquals(expectedDescription, aCategoryCreated.getDescription());
            assertEquals(expectedActive, aCategoryCreated.isActive());
            assertEquals(aCategory.getCreatedAt(), aCategoryCreated.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), aCategoryCreated.getUpdatedAt());
            assertNull(aCategoryCreated.getDeletedAt());
        });

        //second when
        final CategoryEntity categoryEntityCreated =
                this.categoryRepository.findById(aCategoryCreated.getId().getValue()).get();

        //second then
        assertAll("Verify category created - level repository", () -> {
            assertEquals(aCategory.getId().getValue(), categoryEntityCreated.getId());
            assertEquals(expectedName, categoryEntityCreated.getName());
            assertEquals(expectedDescription, categoryEntityCreated.getDescription());
            assertEquals(expectedActive, categoryEntityCreated.isActive());
            assertEquals(aCategory.getCreatedAt(), categoryEntityCreated.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), categoryEntityCreated.getUpdatedAt());
            assertNull(categoryEntityCreated.getDeletedAt());
        });
    }

//    static class CleanUpExtensions implements BeforeEachCallback {
//        @Override
//        public void beforeEach(ExtensionContext extensionContext) {
//            final var repositories = SpringExtension
//                    .getApplicationContext(extensionContext)
//                    .getBeansOfType(CrudRepository.class)
//                    .values();
//        }
//
//        private void cleanUp(final Collection<CrudRepository> repositories) {
//            repositories.forEach(CrudRepository::deleteAll);
//        }
//    }
}