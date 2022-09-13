package vectest;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Random;

public class Main {
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    static int size = 10*1000*1000;
    //static int size = 3000000;
    static Random rand = new Random();

    static double[] buildArr(int size) {
        var arr = new double[size];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = rand.nextDouble();
        }
        return arr;
    }

    static void brute(double[] a, double b[], double result[]) {
        for (int i = 0; i < a.length; ++i) {
            result[i] = Math.abs(-Math.pow(a[i] + b[i], 13)*a[i]);
        }
    }

    static void vector(DoubleVector first, DoubleVector second, double[] result) {
        var res=  first
                .add(second)
                .pow(13)
                .neg().mul(first).abs();
        res.intoArray(result, 0 );
    }

    static void runTest() {
        var a = buildArr(size);
        var b = buildArr(size);
        var s = new double[size];

        long start = System.nanoTime();
        brute(a, b, s);
        long dur = System.nanoTime() - start;
        System.out.println(dur);

        DoubleVector first = DoubleVector.fromArray(DoubleVector.SPECIES_128, a, 0);
        DoubleVector second = DoubleVector.fromArray(DoubleVector.SPECIES_128, b, 0);
        start = System.nanoTime();
        vector(first, second, s);
        long dur2 = System.nanoTime() - start;
        System.out.println(dur2);

        System.out.println("speed up: " + (double)(dur - dur2)/(double)dur*100.0 + "%");
    }

    public static void main(String[] args) {
        runTest();
    }
}
