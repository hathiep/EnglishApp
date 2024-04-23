package com.example.applayout.core.main_class;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomArray {
    private int size;
    private int[] numbers;

    public RandomArray() {
    }

    public RandomArray(int size) {
        this.size = size;
        this.numbers = new int[size];
        for(int i=0; i<size; i++){
            this.numbers[i] = i;
        }
    }

    public List<List<Integer>> generateRandomPermutations() {
        List<List<Integer>> permutations = generatePermutations();

        // Sử dụng thuật toán Fisher-Yates để hoán đổi vị trí các chỉnh hợp
        java.util.Random random = new java.util.Random();
        for (int i = permutations.size() - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            List<Integer> temp = permutations.get(i);
            permutations.set(i, permutations.get(j));
            permutations.set(j, temp);
        }

        return permutations;
    }

    public List<Integer> generateRandomCombination(int x) {
        List<Integer> combination = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            combination.add(i);
        }
        Collections.shuffle(combination);
        return combination.subList(0, x);
    }


    public List<List<Integer>> generatePermutations() {
        List<List<Integer>> permutations = new ArrayList<>();
        generatePermutationsHelper(new ArrayList<>(), permutations);
        return permutations;
    }

    public void generatePermutationsHelper(List<Integer> currentPermutation, List<List<Integer>> permutations) {
        if (currentPermutation.size() == size) {
            permutations.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (int i = 0; i < numbers.length; i++) {
            if (!currentPermutation.contains(numbers[i])) {
                currentPermutation.add(numbers[i]);
                generatePermutationsHelper(currentPermutation, permutations);
                currentPermutation.remove(currentPermutation.size() - 1);
            }
        }
    }
    public int[] generateRandomNumbersNotInArray(int[] excludedNumbers, int n) {
        int[] result = new int[4];
        Random random = new Random();
        Set<Integer> excludedSet = new HashSet<>();

        // Chuyển mảng số loại trừ sang một tập hợp để kiểm tra dễ dàng hơn
        for (int num : excludedNumbers) {
            excludedSet.add(num);
        }

        int index = 0;
        // Tạo 4 số ngẫu nhiên và kiểm tra xem chúng đã tồn tại trong mảng loại trừ hay không
        while (index < 4) {
            int randomNumber = random.nextInt(n); // Tạo số từ 0 đến n
            if (!excludedSet.contains(randomNumber)) {
                result[index++] = randomNumber;
                excludedSet.add(randomNumber); // Thêm số đã chọn vào mảng loại trừ để không lặp lại
            }
        }

        return result;
    }
}
