/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author dmytro
 */
public class GridResponse<R> {

    public final List<R> rows;
    public final Total total;

    public GridResponse(List<R> rows, long total) {
        this.rows = rows;
        this.total = new Total(total);
    }

    public <T> GridResponse(List<T> rows, Function<T, R> mapper, long total) {
        this.rows = rows.stream()
                .map(mapper)
                .collect(toList());

        this.total = new Total(total);
    }

    public static class Total {

        public final long count;

        public Total(long count) {
            this.count = count;
        }
    }
}
