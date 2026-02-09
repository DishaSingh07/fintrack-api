package com.disha.fintrack.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> items;
    private long total;
    private int page;
    private int pageSize;
    private int totalPages;
    private boolean hasMore;

    public static <T> PaginatedResponse<T> from(Page<T> pageData) {
        PaginatedResponse<T> res = new PaginatedResponse<>();
        res.setItems(pageData.getContent());
        res.setTotal(pageData.getTotalElements());
        res.setPage(pageData.getNumber() + 1);
        res.setPageSize(pageData.getSize());
        res.setTotalPages(pageData.getTotalPages());
        res.setHasMore(pageData.hasNext());
        return res;
    }
}
