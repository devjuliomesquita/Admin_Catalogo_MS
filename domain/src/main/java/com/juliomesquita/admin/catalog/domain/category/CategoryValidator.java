package com.juliomesquita.admin.catalog.domain.category;

import com.juliomesquita.admin.catalog.domain.commom.validation.Error;
import com.juliomesquita.admin.catalog.domain.commom.validation.ValidationHandler;
import com.juliomesquita.admin.catalog.domain.commom.validation.Validator;

public class CategoryValidator extends Validator {
    private final static int NAME_MAX_LENGTH = 255;
    private final static int NAME_MIN_LENGTH = 3;

    private final Category category;

    @Override
    public void validate() {
        this.checkNameConstraints();
    }

    private void checkNameConstraints() {
        final String aName = this.category.getName();
        if (aName == null) {
            this.validationHandler().append(new Error("'name' should be not null"));
            return;
        }

        if (aName.isBlank()) {
            this.validationHandler().append(new Error("'name' should be not empty"));
            return;
        }

        final int aNameLength = aName.trim().length();
        if (aNameLength > NAME_MAX_LENGTH || aNameLength < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }


    public CategoryValidator(
            final ValidationHandler aHandler,
            final Category aCategory
    ) {
        super(aHandler);
        this.category = aCategory;
    }
}
