package com.juliomesquita.admin.catalog.application.usecases.genre.create;

import com.juliomesquita.admin.catalog.application.commom.UseCase;

public abstract class CreateGenreUseCase
        extends UseCase<CreateGenreCommand, CreateGenreOutput> {

    public abstract CreateGenreOutput execute(CreateGenreCommand aCommand);
}
