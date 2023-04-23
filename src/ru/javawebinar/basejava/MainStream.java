package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] array1 = {1, 2, 3, 3, 2, 3};
        int[] array2 = {9, 9, 8};
        System.out.println(minValue(array1));
        System.out.println(minValue(array2));
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);
        System.out.println(oddOrEven(list1));
        System.out.println(oddOrEven(list2));
    }

    static int minValue(int[] values) {
        return Arrays
                .stream(values)
                .distinct()
                .sorted()
                .reduce(0, (subtotal, element) -> (element + subtotal * 10));
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = getMap(integers);
        return map.get(map.get(false).size() % 2 == 0);
    }

    static Map<Boolean, List<Integer>> getMap(List<Integer> integers) {
        return integers
                .stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
}
