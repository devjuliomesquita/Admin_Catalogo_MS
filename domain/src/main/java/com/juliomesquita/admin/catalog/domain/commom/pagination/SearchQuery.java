package com.juliomesquita.admin.catalog.domain.commom.pagination;

public record SearchQuery(
        int currentPage,
        int itemsPerPage,
        String terms,
        String sort,
        String direction
) {
}
