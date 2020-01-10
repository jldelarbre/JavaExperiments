package com.github.jldelarbre.javaExperiments.misc;

import java.util.stream.IntStream;

public class IntStreamSample {

    public static void main(String[] args) {
        for (final int i : IntStream.rangeClosed(1, 10).toArray()) {
            System.out.println(i);
        }
        
        IntStream.range(1, 10).forEach(i -> {
            System.out.println(i);
        });
        
        IntStream intStream = IntStream.of(1, 2, 3, 5, 7).map(i -> {
            return 2 * i;
        });
        intStream.forEach(System.out::println);
    }
}
