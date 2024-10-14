package com.juliomesquita.admin.catalog.infrastructure.category.persistence;

import com.juliomesquita.admin.catalog.domain.category.Category;
import com.juliomesquita.admin.catalog.MySqlGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySqlGatewayTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_thenReturnError() {
        final String expectedPropertyName = "name";
        final String expectedMessageError = "not-null property references a null or transient value : com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity.name";

        //given
        final Category aCategory = Category.newCategory("Horror", "Description", true);
        final CategoryEntity anEntity = CategoryEntity.from(aCategory);
        anEntity.setName(null);

        //when
        final DataIntegrityViolationException exception =
                assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(anEntity));

        //then
        final PropertyValueException propertyValueException = assertInstanceOf(PropertyValueException.class, exception.getCause());
        assertEquals(expectedPropertyName, propertyValueException.getPropertyName());
        assertEquals(expectedMessageError, propertyValueException.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallsSave_thenReturnError(){
        final String expectedPropertyName = "createdAt";
        final String expectedMessageError = "not-null property references a null or transient value : com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity.createdAt";

        //given
        final Category aCategory = Category.newCategory("Si-Fi", "Mais alucinantes", true);
        final CategoryEntity anEntity = CategoryEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        //when
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(anEntity));

        //then
        final PropertyValueException propertyValueException = assertInstanceOf(PropertyValueException.class, exception.getCause());
        assertEquals(expectedPropertyName, propertyValueException.getPropertyName());
        assertEquals(expectedMessageError, propertyValueException.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallsSave_thenReturnError(){
        final String expectedPropertyName = "updatedAt";
        final String expectedMessageError = "not-null property references a null or transient value : com.juliomesquita.admin.catalog.infrastructure.category.persistence.CategoryEntity.updatedAt";

        //given
        final Category aCategory = Category.newCategory("Si-Fi", "Mais alucinantes", true);
        final CategoryEntity anEntity = CategoryEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        //when
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(anEntity));

        //then
        final PropertyValueException propertyValueException = assertInstanceOf(PropertyValueException.class, exception.getCause());
        assertEquals(expectedPropertyName, propertyValueException.getPropertyName());
        assertEquals(expectedMessageError, propertyValueException.getMessage());
    }


}