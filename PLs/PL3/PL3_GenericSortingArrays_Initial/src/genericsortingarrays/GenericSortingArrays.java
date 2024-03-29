/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericsortingarrays;

import java.util.Arrays;

/**
 *
 * @author DEI-ISEP
 */


public class GenericSortingArrays {

    /**
     * Swaps two vector positions                  O(1)
     */
    public static <E> void swap(E[] v, int i, int j) {

        E temp = v[i];
        v[i] = v[j];
        v[j] = temp;
    }

    //  printArray
    public static <E> void printArray(E[] v) {
        for (E element : v)
            System.out.println(", " + element);
    }

    /**
     * Selection Sort Algorithm
     */
    public static <E extends Comparable<E>> void selectionSort(E[] v) {
        int n = v.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (v[j].compareTo(v[min]) < 0) {
                    min = j;
                }
            }
            E temp = v[min];
            v[min] = v[i];
            v[i] = temp;
        }
    }

    /**
     * Bubble Sort Algorithm
     *
     * @param v
     */
    public static <E extends Comparable<E>> void bubbleSort(E[] v) {
        int num = v.length;
        for (int i = 0; i < num - 1; i++) {
            for (int j = 0; j < num - i - 1; j++) {
                if (v[j].compareTo(v[j + 1]) > 0) {
                    E temp = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = temp;
                }
            }
        }
    }

    /**
     * insertionSort Algorithm
     */
    public static <E extends Comparable<E>> void insertionSort(E[] v) {
        for (int i = 0; i < v.length; i++) {
            int j = i;
            E x = v[i];
            while (j > 0 && x.compareTo(v[j - 1]) < 0) {
                v[j] = v[j - 1];
                j = j - 1;
            }
            v[j] = x;
        }
    }

    /**
     * Mergesort Algorithm
     */
    private static <E extends Comparable<E>> void merge(E[] S1, E[] S2, E[] S) {
        int nL = S1.length;
        int nR = S2.length;
        int i = 0, j = 0, k = 0;
        while (i < nL && j < nR) {
            if (S1[i].compareTo(S2[j]) < 0) {
                S[k] = S1[i];
                i++;
            } else if (S1[i].compareTo(S2[j]) > 0) {
                S[k] = S2[j];
                j++;
            }
            else {
                S[k] = S1[i];
                i++;
                k++;
                S[k] = S2[j];
                j++;
            }
            k++;
        }
        while (i < nL) {
            S[k] = S1[i];
            i++;
            k++;
        }
        while (j < nR) {
            S[k] = S2[j];
            j++;
            k++;
        }
    }

    public static <E extends Comparable<E>> void mergeSort(E[] S) {
        int n = S.length;
        if (n >= 2) {
            int mid = n / 2;
            E[] S1 = Arrays.copyOfRange(S, 0, mid);
            E[] S2 = Arrays.copyOfRange(S, mid, n);

            mergeSort(S1);
            mergeSort(S2);

            merge(S1, S2, S);
        }
    }

    /**
     * Quicksort Algorithm
     */
    public static <E extends Comparable<E>> void quickSort(E v[]) {
        E[] numbers;
        int number;

        if (v == null || v.length == 0){
            return;
        }
        numbers = v;
        number = v.length;
        quickSort(v, 0, number - 1);

    }

    private static <E extends Comparable<E>> void quickSort(E v[], int left, int right) {
        E pivot = v[(left+right)/2];
        int i = left;
        int j = right;
        while (i <= j) {
            while (v[i].compareTo(pivot) < 0) {
                i++;
            }
            while (v[j].compareTo(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                swap(v, i, j);
                i++;
                j--;
            }
        }
        if (left < j) {
            quickSort(v, left, j);
        }
        if (right > i) {
            quickSort(v, i, right);
        }
    }
}