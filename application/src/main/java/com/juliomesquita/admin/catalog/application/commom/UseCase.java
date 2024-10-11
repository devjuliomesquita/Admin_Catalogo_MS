package com.juliomesquita.admin.catalog.application.commom;


public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN aCommand);
}
