package com.juliomesquita.admin.catalog.domain.commom.pagination;

import java.util.List;

public record Pagination<T>(
        List<T> items,
        int currentPage,
        int itemsPerPage,
        long totalItems,
        int totalPages
) {
}
