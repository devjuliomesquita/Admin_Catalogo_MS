package com.juliomesquita.admin.catalog.application.commom;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN aCommand);
}
