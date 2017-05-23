package com.appdynamics.tools.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abey.tom on 10/19/16.
 */
public class Filters {

    public static <T> T filterOne(T[] items, Filter<T> filter) {
        if (items != null) {
            for (T item : items) {
                if (filter.filter(item)) {
                    return item;
                }
            }
        }
        return null;
    }

    public static <T> List<T> filter(T[] items, Filter<T> filter) {
        List<T> selected = new ArrayList<>();
        if (items != null) {
            for (T item : items) {
                if (filter.filter(item)) {
                    selected.add(item);
                }
            }
        }
        return selected;
    }

    public static <T> List<T> filter(Iterable<T> items, Filter<T> filter) {
        List<T> selected = new ArrayList<>();
        if (items != null) {
            for (T item : items) {
                if (filter.filter(item)) {
                    selected.add(item);
                }
            }
        }
        return selected;
    }

    public interface Filter<T> {
        boolean filter(T item);
    }
}
