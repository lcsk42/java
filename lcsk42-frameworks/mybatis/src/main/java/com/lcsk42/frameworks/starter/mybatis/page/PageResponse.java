package com.lcsk42.frameworks.starter.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements IPage<T> {

    /**
     * Current page
     */
    private long current;

    /**
     * Number of items displayed per page
     */
    private long size = 10L;

    /**
     * Total
     */
    private long total;

    /**
     * Query data list
     */
    @SuppressWarnings("squid:S1948")
    private List<T> records = Collections.emptyList();

    public PageResponse(long current, long size) {
        this(current, size, 0);
    }

    public PageResponse(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    @Override
    public List<OrderItem> orders() {
        return List.of();
    }

    @SuppressWarnings("squid:S3740")
    @Override
    public PageResponse<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public <R> PageResponse<R> convert(Function<? super T, ? extends R> mapper) {
        List<? extends R> mapped = this.getRecords().stream()
                .map(mapper)
                .toList();
        List<R> collect = new ArrayList<>(mapped);
        return PageResponse.<R>builder()
                .current(this.current)
                .size(this.size)
                .total(this.total)
                .records(collect)
                .build();
    }
}
