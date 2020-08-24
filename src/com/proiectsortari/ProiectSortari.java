package com.proiectsortari;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ProiectSortari {

    public static void bubbleSort(ArrayList<Integer> array) {

        int ok, aux;

        do {
            ok = 0;
            for (int i = 0; i < array.size() - 1; i++)
                if (array.get(i) > array.get(i + 1)) {
                    aux = array.get(i);
                    array.set(i, array.get(i + 1));
                    array.set(i + 1, aux);
                    ok = 1;
                }
        } while (ok == 1);

    }

    private static void interclass(ArrayList<Integer> array, int left, int right) {

        int mid = (left + right) / 2;
        ArrayList<Integer> firstArray = new ArrayList<Integer>();
        ArrayList<Integer> secondArray = new ArrayList<Integer>();
        int i;
        int j;

        for (i = left; i < (mid + 1); i++)
            firstArray.add(array.get(i));

        for (j = mid + 1; j <= right; j++)
            secondArray.add(array.get(j));

        i = j = 0;
        int k = left;
        while (i < firstArray.size() && j < secondArray.size()) {
            if (firstArray.get(i) < secondArray.get(j)) {
                array.set(k, firstArray.get(i));
                i++;
            } else {
                array.set(k, secondArray.get(j));
                j++;
            }
            k++;
        }

        while (i < firstArray.size()) {
            array.set(k, firstArray.get(i));
            i++;
            k++;
        }

        while (j < secondArray.size()) {
            array.set(k, secondArray.get(j));
            j++;
            k++;
        }

    }

    private static void mrgSort(ArrayList<Integer> array, int left, int right) {

        if (left < right) {
            int mid = (left + right) / 2;
            mrgSort(array, left, mid);
            mrgSort(array, mid + 1, right);
            interclass(array, left, right);
        }
    }

    public static void mergeSort(ArrayList<Integer> array) {
        mrgSort(array, 0, array.size() - 1);
    }

    public static void insertionSort(ArrayList<Integer> array) {

        int aux;
        int j;

        for (int i = 1; i < array.size(); i++) {
            aux = array.get(i);
            j = i - 1;
            while (j != -1 && array.get(j) > aux) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, aux);
        }
    }


    public static void quickSortClassic(ArrayList<Integer> array) {
        qckSortClassic(array, 0, array.size()-1);
    }

    private static void qckSortClassic(ArrayList<Integer> array, int l, int r) {
        if (l < r) {
            int pivotPos = partitionClassic(array, l, r);
            qckSortClassic(array, l, pivotPos - 1);
            qckSortClassic(array, pivotPos + 1, r);
        }
    }

    private static int partitionClassic(ArrayList<Integer> array, int l, int r) {
        int pivot = array.get(r);
        int i = l - 1;
        for (int j = l; j < r; j++)
            if (array.get(j) < pivot) {
                i++;
                Collections.swap(array, i, j);
            }
        Collections.swap(array, i + 1, r);
        return i + 1;
    }

    public static void countingSort(ArrayList<Integer> array) {

        int min = array.get(0);
        int max = array.get(0);

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max)
                max = array.get(i);
            if (array.get(i) < min)
                min = array.get(i);
        }

        int[] frequenceArray = new int[max - min + 1];
        for (int i = 0; i < array.size(); i++)
            frequenceArray[array.get(i) - min]++;

        int k = 0;
        for (int i = 0; i < frequenceArray.length; i++)
            for (int j = 0; j < frequenceArray[i]; j++) {
                array.set(k, i + min);
                k++;
            }
    }

    public static void radixSort(ArrayList<Integer> array) {

        // An integer in composed by a maximum of 4 bytes = 32 bits = 2^32 numbers.
        // We can decompose an integer in maximum of 4 masks of 2^8 values;

        // We first find out the maximum number
        int max = 0;
        for (int i = 0; i < array.size(); i++)
            if (array.get(i) > max)
                max = array.get(i);

        // And the final number of steps we need.
        int steps = 0;
        while (max != 0) {
            steps++;
            max >>= 8;
        }

        // We need a vector for 256 (2^8) vectors;
        int mask = (1 << 8) - 1;
        Vector<Vector<Integer>> bucket = new Vector<>();
        for (int i = 0; i < 256; i++) {
            Vector<Integer> vector = new Vector<>();
            bucket.add(vector);
        }
        int stepLevel = 0;
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < array.size(); j++)
                bucket.get((array.get(j) >> stepLevel) & mask).add(array.get(j));
            int nr = 0;
            for (int k = 0; k < 256; k++) {
                for (int j = 0; j < bucket.get(k).size(); j++) {
                    array.set(nr, bucket.get(k).get(j));
                    nr++;
                }
            }
            for (int t = 0; t < 256; t++)
                bucket.get(t).clear();
            stepLevel += 8;
        }
    }

    public static void analyzeMethod(ArrayList<Integer> array, ArrayList<Integer> sortedArray, String nameOfMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method sortingMethod = ProiectSortari.class.getMethod(nameOfMethod, ArrayList.class);
        ArrayList<Integer> arrayCopy;
        arrayCopy = (ArrayList<Integer>) array.clone();

        final long startTime = System.nanoTime();
        sortingMethod.invoke(null, arrayCopy);
        final long endTime = System.nanoTime();

        if (arrayCopy.equals(sortedArray))
            System.out.println(nameOfMethod + " in: " + ((endTime - startTime) / 10_000));
        else System.out.println(" Something went wrong with " + nameOfMethod);
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        String root = System.getProperty("user.dir");
        root += "\\src" + "\\com" + "\\proiectsortari" + "\\Teste";

        File folder = new File(root);
        File[] files = folder.listFiles();

        for (File file : files) {
            System.out.println("File: " + file.getName() + " is Open.");
            Scanner scanner = new Scanner(file);
            ArrayList<Integer> array = new ArrayList<>();

            int max = 0;
            int i = 0;
            while (scanner.hasNext()) {
                array.add(scanner.nextInt());
                if (array.get(i) > max)
                    max = array.get(i);
                i++;
            }

            // Native sort:
            ArrayList<Integer> sortedArray;
            sortedArray = (ArrayList<Integer>) array.clone();
            final long start = System.nanoTime();
            Collections.sort(sortedArray);
            final long end = System.nanoTime();
            System.out.println("Native Sorting in: " + ((end - start) / 10_000));

            if (array.size() < 10_000)
                analyzeMethod(array, sortedArray, "bubbleSort");
            analyzeMethod(array, sortedArray, "mergeSort");
            analyzeMethod(array, sortedArray, "insertionSort");
            analyzeMethod(array, sortedArray, "quickSortClassic");
            if (max < 10_000_000)
                analyzeMethod(array, sortedArray, "countingSort");
            analyzeMethod(array, sortedArray, "radixSort");
            System.out.println();
        }
    }

    /*
     FoartePutineMici: 10 de numere intre 0 si 100
     Putine identice: 100 de 5
     PutineMici: 1_000 de numere intre 0 - 1_000
     PutineMari: 1_000 de numere intre 0 - 1_000_000_000

     */
}
