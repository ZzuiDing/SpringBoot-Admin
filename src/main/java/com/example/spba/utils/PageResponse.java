package com.example.spba.utils;

import lombok.Data;


import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> records;
    private long total;

    public PageResponse(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

}
