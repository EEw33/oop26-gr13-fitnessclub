package edu.aitu.oop3.CoreComponent.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Filter {

    private Filter() {}

    public static <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
