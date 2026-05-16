package com.example.demo.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static <T> PagedResponse<T> from(Page<T> pageData) {
        PagedResponse<T> response = new PagedResponse<>();
        response.content = pageData.getContent();
        response.page = pageData.getNumber();
        response.size = pageData.getSize();
        response.totalElements = pageData.getTotalElements();
        response.totalPages = pageData.getTotalPages();
        response.last = pageData.isLast();
        return response;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }
}
