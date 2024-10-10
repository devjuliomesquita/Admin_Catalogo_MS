package com.juliomesquita.admin.catalog.domain.commom.abstractions;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID id) {
        super(id);
    }
}
